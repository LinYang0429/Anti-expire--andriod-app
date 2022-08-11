package com.a404nofund.cs446.cs446.utils;

import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class FireBaseUtils {


    public static DatabaseReference getUserRef(String email,String pos){
        return FirebaseDatabase.getInstance()
                .getReference("postSection/" + pos + Constants.USER_KEY)
                .child(email);
    } // pos/user

    public static DatabaseReference getPostRef(String pos){
        return FirebaseDatabase.getInstance()
                .getReference("postSection/" +pos + Constants.POST_KEY);
    } // pos/posts

    public static DatabaseReference getPostLikedRef(String pos){
        return FirebaseDatabase.getInstance()
                .getReference("postSection/" +pos + Constants.POS_LIKED_KEY);
    } // pos/post_liked

    public static DatabaseReference getPostLikedRef(String postId, String pos){
        return getPostLikedRef(pos).child(getCurrentUser().getEmail().replace(".",",")).child(postId);
    } // pos/post_liked/email/postid

    public  static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String getUid(){
        String path = FirebaseDatabase.getInstance().getReference().push().toString();
        return path.substring(path.lastIndexOf("/")+1);
    }

    public static StorageReference getImageRef(){
        return FirebaseStorage.getInstance().getReference(Constants.POST_IMAGE);
    }

    public static DatabaseReference getMyPostRef(String pos){
        return FirebaseDatabase.getInstance().getReference("postSection/" +pos + Constants.MY_POST)
                .child(getCurrentUser().getEmail().replace(".",","));
    } // pos/my_post/email


    public static DatabaseReference getCommentRef(String postId,String pos){
        return FirebaseDatabase.getInstance().getReference("postSection/" +pos + Constants.COMMENT_KEY)
                .child(postId);
    } // pos/comment/id

    public static DatabaseReference getMyRecordRef(String pos){
        return FirebaseDatabase.getInstance().getReference("postSection/" +pos + Constants.USER_RECORD)
                .child(getCurrentUser().getEmail().replace(".",","));
    } // pos/user_record

    public static void addToMyRecord(String node,final String id, String pos){
        getMyRecordRef(pos).child(node).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                ArrayList<String> myRecordList;
                if(mutableData.getValue() == null){
                    myRecordList = new ArrayList<String>(1);
                    myRecordList.add(id);

                } else {
                    myRecordList= (ArrayList<String>) mutableData.getValue();
                    myRecordList.add(id);
                }
                mutableData.setValue(myRecordList);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
}
