package com.example.huongvu.instagramphoto.models;

import android.text.format.DateUtils;
import android.util.Log;

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
    public String videoUrl;
    public CharSequence timestamp;
    public int imageHeight;
    public int likesCount;

    public PhotoItem fromJson(JSONObject photoJSON) {
        PhotoItem photo = new PhotoItem();
        // Deserialize json into object fields
        try {
            if (photoJSON.optJSONObject("caption") != null){
                photo.caption = photoJSON.optJSONObject("caption").optString("text");}

            if (photoJSON.optJSONObject("user") != null){
                photo.username = photoJSON.optJSONObject("user").optString("username");
                photo.profileUrl = photoJSON.optJSONObject("user").optString("profile_picture");}

            if (photoJSON.optJSONObject("images") != null) {
                photo.imageUrl = photoJSON.optJSONObject("images").optJSONObject("standard_resolution").optString("url");
                photo.imageHeight = photoJSON.optJSONObject("images").optJSONObject("standard_resolution").optInt("height");
                photo.timestamp = dateConvert(photoJSON.optString("created_time"));
                photo.imageId = photoJSON.optString("id");
                //Comment infomation process
                String commentbuff = photoJSON.optString("comments");
                JSONObject comments = new JSONObject(commentbuff);
                photo.commentsCount = comments.optString("count");
                JSONArray commensArr = comments.getJSONArray("data");
                //Get 1st Comments
                JSONObject test = commensArr.getJSONObject(0);
                photo.comment1 = test.optString("text");
                photo.author1 = test.optJSONObject("from").optString("username");}

            if (photoJSON.optJSONObject("likes") != null){
                photo.likesCount = photoJSON.optJSONObject("likes").optInt("count");
            }

            if(photoJSON.optJSONObject("videos") != null){
                photo.videoUrl = photoJSON.optJSONObject("videos").optJSONObject("standard_resolution").optString("url");
                Log.d("VIDEO", "fromJson: " + photo.videoUrl);
        }



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return photo;
    }

    public CharSequence dateConvert(String date){
        if(date != null) {
            long dateConvert;
            CharSequence dateReturn;

            dateConvert = Long.valueOf(date) * 1000;

            dateReturn = DateUtils.getRelativeTimeSpanString(dateConvert, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);

            return dateReturn;
        }
        return  "Now";
    }

}
