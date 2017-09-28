package com.spitchenko.simplerssreader.model.rss;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Date: 21.09.17
 * Time: 14:34
 *
 * @author anatoliy
 */

@EqualsAndHashCode
@RequiredArgsConstructor
@Entity
public class Channel {

    @Embedded
    @SerializedName("image")
    private Image image;

    //@Relation(parentColumn = "channel_link", entityColumn = "channel_link")
    @SerializedName("item")
    private List<Item> item;

    @SerializedName("lastBuildDate")
    private String lastBuildDate;

    @PrimaryKey
    @ColumnInfo(name = "channel_link")
    @SerializedName("link")
    private String link;

    @SerializedName("description")
    private String description;

    @ColumnInfo(name = "channel_title")
    @SerializedName("title")
    private String title;

    public Image getImage() {
        return image;
    }

    public void setImage(final Image image) {
        this.image = image;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(final List<Item> item) {
        this.item = item;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(final String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
