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

import java.util.List;

/**
 * Created by HUONGVU on 3/9/2016.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<PhotoItem> {
    private OnViewCommentListener listener;

    // View lookup cache
    private static class ViewHolder {
        TextView username;
        TextView caption;
        TextView likeCount;
        TextView allComments;
        TextView comment1;
        TextView author1;
        TextView timeStamp;
        ImageView imageUrl;
        ImageView profileUrl;
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
        ViewHolder viewHolder; // view lookup cache stored in tag

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_photo, parent, false);

            viewHolder.username = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.caption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.imageUrl = (ImageView) convertView.findViewById(R.id.ivImageUrl);
            viewHolder.profileUrl = (ImageView) convertView.findViewById(R.id.ivProfileUrl);
            viewHolder.likeCount = (TextView) convertView.findViewById(R.id.tvLike);
            viewHolder.timeStamp = (TextView) convertView.findViewById(R.id.tvTimestamp);
            viewHolder.allComments = (TextView)convertView.findViewById(R.id.tvAllcomments);
            viewHolder.comment1 = (TextView)convertView.findViewById(R.id.tvComment1);
            viewHolder.author1 = (TextView)convertView.findViewById(R.id.tvAuthor1);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object

        viewHolder.username.setText(aphoto.username);



        //viewHolder.caption.setText(aphoto.caption);
        setTags(viewHolder.caption, aphoto.caption);


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

}
