package com.example.huongvu.instagramphoto.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huongvu.instagramphoto.R;
import com.example.huongvu.instagramphoto.interfaces.OnViewCommentListener;
import com.example.huongvu.instagramphoto.models.PhotoItem;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.SimpleMainThreadMediaPlayerListener;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by HUONGVU on 3/9/2016.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<PhotoItem> {
    private OnViewCommentListener listener;

    // View lookup cache
    static class ViewHolder {
        @Bind(R.id.tvUserName) TextView username;
        @Bind(R.id.tvCaption) TextView caption;
        @Bind(R.id.tvLike) TextView likeCount;
        @Bind(R.id.tvAllcomments) TextView allComments;
        @Bind(R.id.tvComment1) TextView comment1;
        @Bind(R.id.tvAuthor1) TextView author1;
        @Bind(R.id.tvTimestamp) TextView timeStamp;
        @Bind(R.id.ivImageUrl) ImageView imageUrl;
        @Bind(R.id.ivProfileUrl) ImageView profileUrl;
        @Bind(R.id.video_player_1) VideoPlayerView mVideoPlayer_1;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setListener(OnViewCommentListener listener){
        this.listener = listener;
    }

    public InstagramPhotoAdapter(Context context, List<PhotoItem> objects) {
        super(context, R.layout.item_photo, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the photo item for this position
        final PhotoItem aphoto = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);

            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.username.setText(aphoto.username);

        //viewHolder.caption.setText(aphoto.caption);
        if(aphoto.caption != null) {
            setTags(viewHolder.caption, aphoto.caption);
        }
        viewHolder.likeCount.setText(String.valueOf(aphoto.likesCount) + " likes");
        viewHolder.timeStamp.setText(aphoto.timestamp);
        viewHolder.allComments.setText("view all " + aphoto.commentsCount + " comment");
        viewHolder.author1.setText(aphoto.author1);

        //viewHolder.comment1.setText(aphoto.comments);
        setTags(viewHolder.comment1," :" + aphoto.comment1);

        //Loading image from below url into imageView
        if(aphoto.imageUrl != null) {

            Picasso.with(getContext())
                    .load(aphoto.imageUrl)
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(viewHolder.imageUrl);
        }
        //Loading image from below url into imageView
        if(aphoto.profileUrl != null) {
            Picasso.with(getContext())
                    .load(aphoto.profileUrl)
                    .placeholder(R.mipmap.ic_launcher) // can also be a drawable
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .resize(120, 120)
                    .transform(new CircleTransform())
                    .into(viewHolder.profileUrl);
        }
        // Return the completed view to render on screen

        //viewHolder.photoItem = aphoto;
        viewHolder.allComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(aphoto.imageId);
            }
        });

        //mVideoPlayer_1 = (VideoPlayerView)root.findViewById(R.id.video_player_1);
        viewHolder.mVideoPlayer_1.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener(){
            @Override
            public void onVideoPreparedMainThread() {
                // We hide the cover when video is prepared. Playback is about to start
                viewHolder.imageUrl.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onVideoStoppedMainThread() {
                // We show the cover when video is stopped
                viewHolder.imageUrl.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVideoCompletionMainThread() {
                // We show the cover when video is completed
                viewHolder.imageUrl.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.imageUrl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(aphoto.videoUrl != null) {
                    mVideoPlayerManager.playNewVideo(null, viewHolder.mVideoPlayer_1, aphoto.videoUrl);
                }
                Log.d("DEBUG", "onClick: Watch Video" + aphoto.videoUrl );
            }
        });

        return convertView;
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if ((pTagString.charAt(i) == '#') || (pTagString.charAt(i) == '@')) {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(Color.parseColor("#3F51B5"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }

    public VideoPlayerManager<MetaData> mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
        @Override
        public void onPlayerItemChanged(MetaData metaData) {

        }
    });


}
