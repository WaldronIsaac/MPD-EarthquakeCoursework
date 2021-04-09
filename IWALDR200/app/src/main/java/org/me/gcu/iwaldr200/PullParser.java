// ISAAC WALDRON - IWALDR200 - S1715300
package org.me.gcu.iwaldr200;

import java.io.Serializable;

public class PullParser implements Serializable {
    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String geolat;
    private String geolong;
    private String depth;
    private  String location;
    private String magnitude;

    public PullParser() {
        title = "";
        description = "";
        link = "";
        pubDate = "";
        category = "";
        geolat = "";
        geolong = "";
    }

    public PullParser(String title,String description,String link,String pubDate,String category,String geolat, String geolong)
    {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.category = category;
        this.geolat = geolat;
        this.geolong = geolong;
    }

    public String toString() {
        String temp;

        temp = title + " " + description + " " + link + " " + pubDate + " " + category + " " + geolat + " " + geolong;

        return temp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        String[] split = description.split(";|: ");
        for(int i = 0 ; i < split.length ; i ++)
        {
            if(i == 3)
            {
                this.setLocation(split[i].trim());
            }
            else if (i == 7)
            {
                this.setDepth(split[i].trim());
            } else if( i == 9)
            {
                this.setMagnitude(split[i].trim());
            }
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGeolat() {
        return geolat;
    }

    public void setGeolat(String geolat) {
        this.geolat = geolat;
    }

    public String getGeolong() {
        return geolong;
    }

    public void setGeolong(String geolong) {
        this.geolong = geolong;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth.replace("km", "");
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }
}
