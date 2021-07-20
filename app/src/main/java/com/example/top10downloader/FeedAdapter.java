package com.example.top10downloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> apps;

    public FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> apps) {
        super(context, resource);
        layoutResource=resource;
        layoutInflater=LayoutInflater.from(context);
        this.apps = apps;
    }

    @Override
    public int getCount() {//returns the list size
        return apps.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {//return the next item on the list(in the "position" index)
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(layoutResource,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder) convertView.getTag();
        }

        FeedEntry curApp = apps.get(position);

        viewHolder.name.setText(curApp.getName());
        viewHolder.author.setText(curApp.getArtist());
        viewHolder.summary.setText(curApp.getSummary());
        new ImageLoadTask(curApp.getImageURL(),viewHolder.image).execute();
        return convertView;
    }
    private class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
    private class ViewHolder{
        final TextView name;
        final TextView author;
        final TextView summary;
        final ImageView image;

        public ViewHolder(View view) {
            this.name = view.findViewById(R.id.appName);
            this.author = view.findViewById(R.id.appAuthor);
            this.summary = view.findViewById(R.id.appSummary);
            this.image = view.findViewById(R.id.appImage);
        }
    }
}
