package com.spitchenko.simplerssreader.model.atom;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Entity
@Data
public class Feed {

    @Ignore
    @SerializedName("entry")
    private List<Entry> entry;

    @Embedded
    @SerializedName("author")
    private Author author;

    @SerializedName("subtitle")
    private String subtitle;

    @Ignore
    @SerializedName("link")
    private List<Link> link;

    @PrimaryKey
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("updated")
    private String updated;

    @SerializedName("logo")
    private String logo;

    @SerializedName("icon")
    private String icon;

}