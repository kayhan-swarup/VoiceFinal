package com.swarup.kayhan.voice;

/**
 * Created by Kayhan on 1/13/2015.
 */
public class Comment  {

    int commentId;
    String comment;
    int postId;
    String userID;

    public Comment(){}
    public Comment(String comment,String userID,int postId){
        this.comment = comment;
        this.postId = postId;
        this.userID = userID;
    }


}
