package com.example.top10downloader;

import android.util.Log;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class ParseApps {
    private static final String TAG = "ParseApps";
    private ArrayList<FeedEntry> apps;

    public ParseApps() {
        this.apps = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApps() {
        return apps;
    }

    public boolean parse(String xmlString) {
        boolean status = true;
        boolean inEntry = false;
        FeedEntry cur = null;
        String textVal = "";
        try {
            //create factory for xml parser
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);//namespace is a name of the xml object
            //create pp
            XmlPullParser xpp = factory.newPullParser();
            //set xml input
            xpp.setInput(new StringReader(xmlString));

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse: starting tag for " + tagName);
                        if("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            cur = new FeedEntry();

                        }


                        break;

                    case XmlPullParser.TEXT:
                        textVal = xpp.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                apps.add(cur);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                cur.setName(textVal);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                cur.setArtist(textVal);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                cur.setReleaseDate(textVal);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                cur.setSummary(textVal);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                cur.setImageURL(textVal);
                            }
                        }
                        break;
                    default:
                        //nothing


                }
                eventType = xpp.next();

            }
            for (FeedEntry app : apps) {
                Log.d(TAG, "parse: ********************");
                Log.d(TAG, app.toString());
            }
            return true;
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
            return false;
        }

    }

}
