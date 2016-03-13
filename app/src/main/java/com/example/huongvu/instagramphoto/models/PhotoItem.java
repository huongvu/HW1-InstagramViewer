package com.example.huongvu.instagramphoto.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HUONGVU on 3/9/2016.
 */
public class PhotoItem {

    public String username;
    public String imageUrl;
    public String profileUrl;
    public String caption;
    public String comment1;
    public String author1;
    public String commentsCount;
    public String imageId;
    public CharSequence timestamp;
    public int imageHeight;
    public int likesCount;

    public PhotoItem fromJson(JSONObject photoJSON) {
        PhotoItem photo = new PhotoItem();
        // Deserialize json into object fields
        try {
            photo.username = nullCheck(photoJSON.getJSONObject("user").getString("username"));
            photo.caption = nullCheck(photoJSON.getJSONObject("caption").getString("text"));
            photo.imageUrl = nullCheck(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
            photo.profileUrl = nullCheck(photoJSON.getJSONObject("user").getString("profile_picture"));
            photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
            photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
            photo.timestamp = dateConvert(photoJSON.getString("created_time"));
            photo.imageId = photoJSON.getString("id");

            //Comment infomation process
            String commentbuff = photoJSON.getString("comments");
            JSONObject comments = new JSONObject(commentbuff);
            photo.commentsCount = comments.getString("count");
            JSONArray commensArr = comments.getJSONArray("data");
            //Get 1st Comments
            JSONObject test = commensArr.getJSONObject(0);
            photo.comment1 = test.getString("text");
            photo.author1 = test.getJSONObject("from").getString("username");


            //JSONArray paramsArr = photoJSON.getJSONArray(photo.comments);
            //photo.comments = photoJSON.getJSONObject()
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return photo;
    }

    public String nullCheck(String text){
        if(text != null){
            return text;
        }
        else
            return "test";
    }

    public CharSequence dateConvert(String date){
        long dateConvert;
        CharSequence dateReturn;

        dateConvert = Long.valueOf(date)*1000;

        dateReturn = DateUtils.getRelativeTimeSpanString(dateConvert, System.currentTimeMillis(),DateUtils.SECOND_IN_MILLIS);

        return dateReturn;
    }

}
