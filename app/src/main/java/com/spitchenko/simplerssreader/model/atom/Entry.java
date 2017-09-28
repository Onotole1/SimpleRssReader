package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 15:46
 *
 * @author anatoliy
 */

@Entity(foreignKeys = @ForeignKey(entity = Feed.class, parentColumns = "id", childColumns = "feed_id"))
@Data
public class Entry {

    @Embedded
    @SerializedName("author")
    private Author author;

    @SerializedName("link")
    private Link link;

    @PrimaryKey
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @Embedded
    @SerializedName("category")
    private Category category;

    @SerializedName("updated")
    private String updated;

    @SerializedName("content")
    private Content content;

    @ColumnInfo(name = "feed_id")
    private String feedId;
}
