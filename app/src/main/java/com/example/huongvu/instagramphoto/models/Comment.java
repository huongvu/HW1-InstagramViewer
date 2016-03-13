package com.example.huongvu.instagramphoto.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HUONGVU on 3/13/2016.
 */
public class Comment {

    public String comment;
    public String author;
    public String authorURL;


    public Comment fromJson(JSONObject commentJSON) {
        Comment commentnew = new Comment();
        // Deserialize json into object fields
        try {
            commentnew.author = nullCheck(commentJSON.getJSONObject("from").getString("username"));
            commentnew.comment = nullCheck(commentJSON.getString("text"));
            commentnew.authorURL = nullCheck(commentJSON.getJSONObject("from").getString("profile_picture"));

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return commentnew;
    }

    public String nullCheck(String text){
        if(text != null){
            return text;
        }
        else
            return "test";
    }

}
