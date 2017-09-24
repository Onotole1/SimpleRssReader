package com.spitchenko.simplerssreader.model.atom;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:19
 *
 * @author anatoliy
 */

@Data
class Category {
    @SerializedName("term")
    private String term;
}
