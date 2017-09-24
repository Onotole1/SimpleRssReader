package com.spitchenko.simplerssreader.model.atom;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class Feed {

    @SerializedName("entry")
    private List<Entry> entry;

    @SerializedName("author")
    private Author author;

    @SerializedName("subtitle")
    private String subtitle;

    @SerializedName("link")
    private List<Link> link;

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("updated")
    private String updated;

}