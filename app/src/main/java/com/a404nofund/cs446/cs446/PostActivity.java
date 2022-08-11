package com.a404nofund.cs446.cs446;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.a404nofund.cs446.cs446.dataType.Post;
import com.a404nofund.cs446.cs446.dataType.Comment;
import com.a404nofund.cs446.cs446.dataType.User;
import com.a404nofund.cs446.cs446.utils.Constants;
import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;



public class PostActivity extends AppCompatActivity
implements View.OnClickListener{
    private static final String BUNDLE = "comments";
    private Post post;
    private EditText editText;
    private Comment comment;
    String position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        if(savedInstanceState != null){
            comment = (Comment) savedInstanceState.getSerializable(BUNDLE);
        } //?

//////////////////////////////////////////////////////////////////////////////////////
        Intent intent = getIntent();
        position = intent.getStringExtra("Position");
        post = (Post) intent.getSerializableExtra("FOLLOW YOUR COMMENT BELOW");

        editText = (EditText) findViewById(R.id.comment_edit);
        findViewById(R.id.iv_send).setOnClickListener(this);
////////////////////////////////////////////////////////////////////////////////////
        ImageView createrPhoto = (ImageView) findViewById(R.id.iv_poster_image);
        ImageView postPhoto = (ImageView) findViewById(R.id.iv_post_display);
        TextView createrName = (TextView) findViewById(R.id.tv_poster_username);
        TextView postTime = (TextView) findViewById(R.id.tv_postTime);
        TextView postContent = (TextView) findViewById(R.id.tv_post_content);
        TextView postLike = (TextView) findViewById(R.id.tv_post_like);
        TextView postNum = (TextView) findViewById(R.id.tv_commentNum);

        LinearLayout postLikeLayout = (LinearLayout) findViewById(R.id.layout_post_like);
        LinearLayout postLayout = (LinearLayout) findViewById(R.id.layout_comment);

        createrName.setText(post.getUser().getUser());
        postTime.setText(DateUtils.getRelativeTimeSpanString(post.getTime()));
        postContent.setText(post.getContent());
        postLike.setText(String.valueOf(post.getLikes()));
        postNum.setText(String.valueOf(post.getComments()));

        Glide.with(PostActivity.this)
                .load(post.getUser().getPhoto())
                .into(createrPhoto);

        if(post.getUrl() != null){
            postPhoto.setVisibility(View.VISIBLE);
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(post.getUrl());

            Glide.with(PostActivity.this)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(postPhoto);
        } else {
            postPhoto.setImageBitmap(null);
            postPhoto.setVisibility(View.GONE);
        }
/////////////////////////////////////////////////////////////////
        RecyclerView recyclerViewComment = (RecyclerView) findViewById(R.id.comment_recycler);
        recyclerViewComment.setLayoutManager((new LinearLayoutManager(PostActivity.this)));

        FirebaseRecyclerAdapter<Comment,CommentHolder> adapter = new FirebaseRecyclerAdapter<Comment, CommentHolder>(
                Comment.class,
                R.layout.comment_post,
                CommentHolder.class,
                FireBaseUtils.getCommentRef(post.getPostId(),position)
        ) {
            @Override
            protected void populateViewHolder(CommentHolder viewHolder, Comment model, int position) {
                viewHolder.setCommentUserName(model.getUser().getUser());
                viewHolder.setCommentTime(DateUtils.getRelativeTimeSpanString(model.getTime()));
                viewHolder.setCommentText(model.getComment());

                Glide.with(PostActivity.this)
                        .load(model.getUser().getPhoto())
                        .into(viewHolder.commentUserPhoto);



            }
        };
        recyclerViewComment.setAdapter(adapter);
    }

    @Override
    public void  onClick(View v){
        switch (v.getId()){
            case R.id.iv_send:
                send();
        }
    }

    private void send(){
        final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
        progressDialog.setMessage("SENDING COMMENT");
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        comment = new Comment();
        final String uid = FireBaseUtils.getUid();
        String stringComment = editText.getText().toString();
        comment.setCommentId(uid);
        comment.setComment(stringComment);
        comment.setTime(System.currentTimeMillis());
        FireBaseUtils.getUserRef(FireBaseUtils.getCurrentUser().getEmail().replace(".",","),position)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        comment.setUser(user);
                        FireBaseUtils.getCommentRef(post.getPostId(),position)
                                .child(uid)
                                .setValue(comment);

                        FireBaseUtils.getPostRef(position).child(post.getPostId())
                                .child(Constants.NUM_COMMENT_KEY)
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
                                        progressDialog.dismiss();
                                        FireBaseUtils.addToMyRecord(Constants.COMMENT_KEY,uid,position);
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressDialog.dismiss();
                    }
                });
    }

    public static class CommentHolder extends RecyclerView.ViewHolder{
        ImageView commentUserPhoto;
        TextView commentUserName;
        TextView commentTime;
        TextView commentText;

        public CommentHolder(View itemView){
            super(itemView);
            commentUserPhoto = (ImageView) itemView.findViewById(R.id.iv_comment_user_photo);
            commentUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
            commentTime = (TextView) itemView.findViewById(R.id.tv_time_post);
            commentText = (TextView) itemView.findViewById(R.id.tv_comments);

        }



        public void setCommentUserName(String userName) {
            commentUserName.setText(userName);
        }

        public void setCommentTime(CharSequence time) {
            commentTime.setText(time);
        }

        public void setCommentText(String text) {
            commentText.setText(text);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putSerializable(BUNDLE,comment);
        super.onSaveInstanceState(outState);

    }
}
