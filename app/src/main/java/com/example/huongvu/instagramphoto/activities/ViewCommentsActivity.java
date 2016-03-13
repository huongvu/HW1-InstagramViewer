package com.example.huongvu.instagramphoto.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.huongvu.instagramphoto.R;
import com.example.huongvu.instagramphoto.adapters.InstagramCommentAdapter;
import com.example.huongvu.instagramphoto.models.Comment;
import com.example.huongvu.instagramphoto.network.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ViewCommentsActivity extends AppCompatActivity {

    private ArrayList<Comment> commentItems = new ArrayList<>();
    private InstagramCommentAdapter aComment;
    private SwipeRefreshLayout swipeContainer;
    private String imageId;

    //https://api.instagram.com/v1/media/{media-id}/comments?access_token=ACCESS-TOKEN
    public static final String URL = "https://api.instagram.com/v1/media/";
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comments);

        imageId = getIntent().getStringExtra("imageId");

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getAllComments();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        aComment = new InstagramCommentAdapter(this, commentItems);

        ListView lvComments = (ListView) findViewById(R.id.lvComments);

        lvComments.setAdapter(aComment);

        //Get Popular Photo From Instagram for 1st time
        getAllComments();

    }

    public void onSubmit(View v) {
        // closes the activity and returns to first screen
        this.finish();
    }

    private void getAllComments() {

        InstagramClient clientRequest = new InstagramClient();
        String urlRequest = URL + imageId + "/comments?client_id=" + CLIENT_ID;

        clientRequest.fetchInstagramPhotos(urlRequest, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] hearders, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                JSONArray commentsJSON = null;

                // clear all old photos
                commentItems.clear();

                try {
                    commentsJSON = response.getJSONArray("data");
                    for (int i = 0; i < commentsJSON.length(); i++) {

                        JSONObject commentJSON = commentsJSON.getJSONObject(i);

                        Comment comment = new Comment();

                        commentItems.add(comment.fromJson(commentJSON));

                    }
                    aComment.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Log.i("DEBUG", "onSuccess: " + response.toString());
                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

            }
        });

    }
}
