package com.a404nofund.cs446.cs446;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.a404nofund.cs446.cs446.dataType.Post;
import com.a404nofund.cs446.cs446.dataType.User;
import com.a404nofund.cs446.cs446.utils.Constants;
import com.a404nofund.cs446.cs446.utils.FireBaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class CommentPopUp extends DialogFragment implements View.OnClickListener {
    private static final int RC_PHOTO_PICKER = 1;
    private Post post;
    private ProgressDialog progressDialog;
    private Uri uri;
    private ImageView imageView;
    private View rootView;
    String position;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        post = new Post();
        progressDialog = new ProgressDialog(getContext());
        rootView = getActivity().getLayoutInflater().inflate(R.layout.comment_dialogue, null);
        imageView = (ImageView) rootView.findViewById(R.id.dialogue_pic);
        rootView.findViewById(R.id.dialogue_send).setOnClickListener(this);
        rootView.findViewById(R.id.dialogue_camera).setOnClickListener(this);
        builder.setView(rootView);
        position = getArguments().getString("Position");
        return builder.create();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialogue_camera:
                camera();
                break;
            case R.id.dialogue_send:
                send();
                break;
        }
    }

    private void camera() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jepg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    private void send() {
        progressDialog.setMessage("SENDING YOUR COMMENT");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();



        DatabaseReference ref = FireBaseUtils.getUserRef(FireBaseUtils.getCurrentUser().getEmail().replace(".", ","),position);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        final String postId = FireBaseUtils.getUid();
                        TextView tvDialogue = (TextView) rootView.findViewById(R.id.dialogue_input);

                        post.setUser(user);
                        post.setComments(0);
                        post.setLikes(0);
                        post.setTime(System.currentTimeMillis());
                        post.setPostId(postId);
                        post.setContent(tvDialogue.getText().toString());

                        if (uri != null) {
                            FireBaseUtils.getImageRef()
                                    .child(uri.getLastPathSegment())
                                    .putFile(uri)
                                    .addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            String url = Constants.POST_IMAGE + "/" + uri.getLastPathSegment();
                                            post.setUrl(url);
                                            addTomyCommentList(postId);
                                        }
                                    });
                        } else {
                            addTomyCommentList(postId);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void addTomyCommentList(String postId) {
        FireBaseUtils.getPostRef(position)
                .child(postId)
                .setValue(post);
        FireBaseUtils.getMyPostRef(position)
                .child(postId)
                .setValue(true)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        dismiss();
                    }
                });
        FireBaseUtils.addToMyRecord(Constants.COMMENT_KEY, postId,position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {
                uri = data.getData();
                imageView.setImageURI(uri);
            }
        }
    }
}
