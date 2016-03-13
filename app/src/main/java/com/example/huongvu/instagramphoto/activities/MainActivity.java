package com.example.huongvu.instagramphoto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.huongvu.instagramphoto.R;
import com.example.huongvu.instagramphoto.adapters.InstagramPhotoAdapter;
import com.example.huongvu.instagramphoto.interfaces.OnViewCommentListener;
import com.example.huongvu.instagramphoto.models.PhotoItem;
import com.example.huongvu.instagramphoto.network.InstagramClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ArrayList<PhotoItem> photoItems = new ArrayList<>();
    private InstagramPhotoAdapter aPhoto;
    private SwipeRefreshLayout swipeContainer;

    public static final String URL = "https://api.instagram.com/v1/media/popular?client_id=";
    public static final String CLIENT_ID = "e05c462ebd86446ea48a5af73769b602";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getPopularPhoto();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        aPhoto = new InstagramPhotoAdapter(this, photoItems);

        aPhoto.setListener(new OnViewCommentListener() {
            @Override
            public void onClick(String id) {
                //ViewCommentDialogFragment dialogFragment = new ViewCommentDialogFragment();
                //dialogFragment.show(getFragmentManager(), "");
                //Log.d("DEBUG", "onClick: " + id);
                Intent i = new Intent(MainActivity.this, ViewCommentsActivity.class);
                // put "extras" into the bundle for access in the second activity
                i.putExtra("imageId", id);
                // brings up the second activity
                startActivity(i);
            }
        });

        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        lvPhotos.setAdapter(aPhoto);

        //Get Popular Photo From Instagram for 1st time
        getPopularPhoto();

    }

    private void getPopularPhoto() {

        InstagramClient clientRequest = new InstagramClient();
        String urlRequest = URL + CLIENT_ID;

        clientRequest.fetchInstagramPhotos(urlRequest, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] hearders, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                JSONArray photosJSON = null;

                // clear all old photos
                photoItems.clear();

                try {
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {

                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        PhotoItem photo = new PhotoItem();

                        photoItems.add(photo.fromJson(photoJSON));

                    }
                    aPhoto.notifyDataSetChanged();

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
