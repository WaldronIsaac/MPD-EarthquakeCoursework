// ISAAC WALDRON - IWALDR200 - S1715300
package org.me.gcu.iwaldr200;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private LinearLayout dataDisplay;
    private Button searchButton;
    private Button mapButton;
    private String result = "";
    private String urlSource = "http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    LinkedList<PullParser> earthquakeList = new LinkedList<PullParser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag", "in onCreate");

        // Set up the raw links to the graphical components
        dataDisplay = (LinearLayout) findViewById(R.id.dataDisplay);
        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);
        Log.e("MyTag", "after searchButton");
        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);
        Log.e("MyTag", "after mapButton");

        // More Code goes here
        startProgress();
        Log.e("MyTag", "after startProgress");
    }

    public void onClick(View v) {
        Log.e("MyTag", "in onClick");
        if (v == searchButton) {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Items", earthquakeList);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (v == mapButton) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Items", earthquakeList);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void startProgress() {
        /* OLD THREAD METHOD
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
        */

        GetEarthquakes getEarthquakes = new GetEarthquakes();
        getEarthquakes.execute();
    }

    // Need separate thread to access the internet resource over network
    private class GetEarthquakes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag", "in run");

            try {
                Log.e("MyTag", "in try");
                url = new URL(urlSource);
                yc = url.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag", "after ready");
                //
                // Now read the data. Make sure that there are no specific headers
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null) {
                    result = result + inputLine;
                    Log.e("MyTag", inputLine);

                }
                in.close();
                return result;
            } catch (IOException ae) {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            LinkedList<PullParser> earthquakeList = parseData(s);

            // Now update the TextView to display raw XML data
            Log.d("UI thread", "I am the UI thread");
            for (int i = 0; i < earthquakeList.size(); i++) {

                // Create Button for Earthquake
                Button earthquakeItem = new Button(MainActivity.this);
                earthquakeItem.setText(earthquakeList.get(i).getLocation() + "\n  Magnitude: " + earthquakeList.get(i).getMagnitude());

                // Create On Click for Button (Use loop 'i' to get bundle values for DetailsActivity bundle)
                int finalI = i;
                earthquakeItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);
                        intent.putExtra("location", earthquakeList.get(finalI).getLocation());
                        intent.putExtra("magnitude", earthquakeList.get(finalI).getMagnitude());
                        intent.putExtra("pubDate", earthquakeList.get(finalI).getPubDate());
                        intent.putExtra("geoLat", earthquakeList.get(finalI).getGeolat());
                        intent.putExtra("geoLong", earthquakeList.get(finalI).getGeolong());
                        intent.putExtra("depth", earthquakeList.get(finalI).getDepth());
                        startActivity(intent);
                    }
                });

                // Style Button

                // Text Size
                earthquakeItem.setTextSize(16);

                // Set Button Text Colour based on the magnitude (Green = Weak, Yellow = Medium, Red = Strong)
                if (Float.parseFloat(earthquakeList.get(i).getMagnitude()) < 1) {
                    earthquakeItem.setTextColor(Color.parseColor("green"));
                } else if (Float.parseFloat(earthquakeList.get(i).getMagnitude()) >= 1 && Float.parseFloat(earthquakeList.get(i).getMagnitude()) <= 2) {
                    earthquakeItem.setTextColor(Color.parseColor("yellow"));
                } else if (Float.parseFloat(earthquakeList.get(i).getMagnitude()) > 2) {
                    earthquakeItem.setTextColor(Color.parseColor("red"));
                }

                // BackgroundTintList (Instead of BackgroundColor, to keep click ripple animation)
                earthquakeItem.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.purple_500)));

                // Center Button
                earthquakeItem.setGravity(Gravity.CENTER);

                dataDisplay.addView(earthquakeItem);
            }

        }

        private LinkedList<PullParser> parseData(String notNulled) {
            PullParser item = new PullParser();
            try {
                String dataToParse = notNulled.replace("null", "");
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(dataToParse));
                int eventType = xpp.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    // Found a start tag
                    if (eventType == XmlPullParser.START_TAG) {
                        // Check Tag
                        if (xpp.getName().equalsIgnoreCase("item")) {
                            Log.e("MyTag", "Item Start Tag found");
                            item = new PullParser();
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("title")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Title is " + temp);
                            item.setTitle(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("description")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Description is " + temp);
                            item.setDescription(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("link")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Link is " + temp);
                            item.setLink(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Publish Date is " + temp);
                            item.setPubDate(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("category")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Category is " + temp);
                            item.setCategory(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("geo:lat")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Latitude is lat " + temp);
                            item.setGeolat(temp);
                            // Check Tag
                        } else if (xpp.getName().equalsIgnoreCase("geo:long")) {
                            // Now just get the associated text
                            String temp = xpp.nextText();

                            // Do something with text
                            Log.e("MyTag", "Longitude is lat " + temp);
                            item.setGeolong(temp);
                        }
                    } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
                        earthquakeList.add(item);
                    }

                    // Get the next event
                    eventType = xpp.next();

                } // End of while
            }

            //return earthquakeList;

            catch (XmlPullParserException ae1) {
                Log.e("MyTag", "Parsing error" + ae1.toString());
            } catch (IOException ae1) {
                Log.e("MyTag", "IO error during parsing");
            }

            Log.e("MyTag", "End document");

            return earthquakeList;
        }

    }

}