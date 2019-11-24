package com.seemantshekhar.spotlightfeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private  List <FeedEntry> blogs;

    public FeedAdapter(Context context, int resource, List<FeedEntry> blogs) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.blogs = blogs;
    }

    @Override
    public int getCount() {
        return blogs.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView =layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentBlog = blogs.get(position);
        viewHolder.blogName.setText(currentBlog.getName());
        viewHolder.blogAuthor.setText(currentBlog.getAuthor());
        viewHolder.blogPubDate.setText(currentBlog.getPubDate());
        viewHolder.blogSummary.setText(currentBlog.getSummary());

        return convertView;

        }

    private class ViewHolder{
        final TextView blogName;
        final TextView blogAuthor;
        final TextView blogPubDate;
        final TextView blogSummary;

        ViewHolder (View v){
            this.blogName = v.findViewById(R.id.blogName);
            this.blogAuthor = v.findViewById(R.id.blogAuthor);
            this.blogPubDate = v.findViewById(R.id.blogPubDate);
            this.blogSummary = v.findViewById(R.id.blogSummary);

        }
    }
}
