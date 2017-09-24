package com.spitchenko.simplerssreader.model.atom;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:14
 *
 * @author anatoliy
 */

@Data
class Link {

    @SerializedName("rel")
    String rel;

    @SerializedName("href")
    String href;

}