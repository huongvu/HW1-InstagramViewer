package com.example.huongvu.instagramphoto.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Created by HUONGVU on 3/9/2016.
 */
public class InstagramClient {


    public InstagramClient() {
        super();
    }

    public void fetchInstagramPhotos(String url, String paras, AsyncHttpResponseHandler handler){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, null , handler);
    }
}
