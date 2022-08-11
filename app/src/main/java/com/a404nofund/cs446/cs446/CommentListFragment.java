package com.a404nofund.cs446.cs446;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a404nofund.cs446.cs446.utils.Constants;
import com.bumptech.glide.Glide;
import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.a404nofund.cs446.cs446.dataType.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

public class CommentListFragment extends Fragment {
    private View mRootView;
    private FirebaseRecyclerAdapter<Post,CommentListViewHolder> adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private String pos;


    public CommentListFragment() {
    }

    public static CommentListFragment newInstance() {
        CommentListFragment fragment = new CommentListFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_recyclerviewcomment, container, false);
        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.floatingActionButton);
        pos = getArguments().getString("Position");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentPopUp commentPopUp = new CommentPopUp();
                Bundle bundle = new Bundle();
                bundle.putString("Position",pos);
                commentPopUp.setArguments(bundle);
                commentPopUp.show(getFragmentManager(),null);
            }
        });
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerviewcomment);
        layoutManager = new LinearLayoutManager(mRootView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new FirebaseRecyclerAdapter<Post, CommentListViewHolder>(
                Post.class,
                R.layout.fragment_cardcomment,
                CommentListViewHolder.class,
                FireBaseUtils.getPostRef(pos)
        ){
            @Override
            protected void populateViewHolder(CommentListViewHolder viewHolder, final Post post, final int position){
                viewHolder.createrName.setText(post.getUser().getUser());
                viewHolder.postLike.setText(String.valueOf(post.getLikes()));
                viewHolder.postTime.setText(DateUtils.getRelativeTimeSpanString(post.getTime()));
                viewHolder.postNum.setText(String.valueOf(post.getComments()));
                viewHolder.postContent.setText(post.getContent());

                Glide.with(getActivity())
                        .load(post.getUser().getPhoto())
                        .into(viewHolder.postPhoto);

                if(post.getUrl() != null){
                    viewHolder.postPhoto.setVisibility(View.VISIBLE);
                    StorageReference storageReference = FirebaseStorage.getInstance()
                            .getReference(post.getUrl());
                    Glide.with(getActivity())
                            .using(new FirebaseImageLoader())
                            .load(storageReference).into(viewHolder.postPhoto);

                } else{
                    viewHolder.postPhoto.setImageBitmap(null);
                    viewHolder.postPhoto.setVisibility(View.GONE);
                }

                viewHolder.postLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLikeClick(post.getPostId());
                    }
                });

                viewHolder.postLayout.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(getContext(),PostActivity.class);
                        intent.putExtra("FOLLOW YOUR COMMENT BELOW",post);
                        intent.putExtra("Position",pos);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
         return mRootView;

    }



    private void onLikeClick(final String postId){
        FireBaseUtils.getPostLikedRef(postId,pos)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() !=null ){

                            FireBaseUtils.getPostRef(pos)
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num-1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                            FireBaseUtils.getPostLikedRef(postId,pos)
                                                    .setValue(null);
                                        }
                                    });
                        } else {
                            FireBaseUtils.getPostRef(pos)
                                    .child(postId)
                                    .child(Constants.NUM_LIKES_KEY)
                                    .runTransaction(new Transaction.Handler() {
                                        @NonNull
                                        @Override
                                        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                            long num = (long) mutableData.getValue();
                                            mutableData.setValue(num+1);
                                            return Transaction.success(mutableData);
                                        }

                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                                            FireBaseUtils.getPostLikedRef(postId,pos)
                                                    .setValue(true);
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


    public static class CommentListViewHolder extends  RecyclerView.ViewHolder {
        ImageView createrPhoto;
        ImageView postPhoto;
        TextView createrName;
        TextView postTime;
        TextView postContent;
        TextView postLike;
        TextView postNum;
        LinearLayout postLikeLayout;
        LinearLayout postLayout;


        public CommentListViewHolder(final View itemView){
            super(itemView);

            createrPhoto = (ImageView) itemView.findViewById(R.id.iv_poster_image);
            postPhoto = (ImageView) itemView.findViewById(R.id.iv_post_display);
            createrName = (TextView) itemView.findViewById(R.id.tv_poster_username);
            postTime = (TextView) itemView.findViewById(R.id.tv_postTime);
            postContent = (TextView) itemView.findViewById(R.id.tv_post_content);
            postLike = (TextView) itemView.findViewById(R.id.tv_post_like);
            postNum = (TextView) itemView.findViewById(R.id.tv_commentNum);

            postLikeLayout = (LinearLayout) itemView.findViewById(R.id.layout_post_like);
            postLayout = (LinearLayout) itemView.findViewById(R.id.layout_comment);


        }




    } //view holder
}
