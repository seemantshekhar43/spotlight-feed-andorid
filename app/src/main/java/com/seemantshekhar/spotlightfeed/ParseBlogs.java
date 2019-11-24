package com.seemantshekhar.spotlightfeed;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseBlogs {
    private static final String TAG ="parseBlogs";
    private ArrayList<FeedEntry> blogs;

    public ParseBlogs() {
        this.blogs = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getBlogs() {
        return blogs;
    }

    public boolean parse(String xmlData){

        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inItem = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String tagName = xpp.getName();

                switch (eventType){
                    case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: starting tag for: " + tagName);
                        if("item".equalsIgnoreCase(tagName)){
                            currentRecord = new FeedEntry();
                            inItem = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: ending tag for: " + tagName);
                        if(inItem){
                            if("item".equalsIgnoreCase(tagName)){
                                blogs.add(currentRecord);
                                inItem = false;
                            } else if("title".equalsIgnoreCase(tagName)){
                                currentRecord.setName(textValue);
                            } else if("creator".equalsIgnoreCase(tagName)){
                                currentRecord.setAuthor(textValue);
                            } else if("pubDate".equalsIgnoreCase(tagName)){
                                currentRecord.setPubDate(textValue.substring(5, 16));
                            } else if("description".equalsIgnoreCase(tagName)){
                                currentRecord.setSummary(textValue);
                            }
                        }
                        break;

                    default:
                        // do nothing
                }

                eventType = xpp.next();

            }
                for(FeedEntry blog: blogs){
                    Log.d(TAG, "*****************");
                    Log.d(TAG, blog.toString());
                }


        } catch (Exception e){
            status = false;
            e.printStackTrace();
        }
        return status;
    }
}
