package com.spitchenko.simplerssreader.model.atom;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 15:46
 *
 * @author anatoliy
 */

@Data
class Entry {

    @SerializedName("author")
    Author author;

    @SerializedName("link")
    Link link;

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("category")
    Category category;

    @SerializedName("updated")
    String updated;

    @SerializedName("content")
    Content content;
}
