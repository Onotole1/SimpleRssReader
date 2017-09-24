package com.spitchenko.simplerssreader.model.atom;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * Date: 21.09.17
 * Time: 16:20
 *
 * @author anatoliy
 */

@Data
class Content {

    @SerializedName("$")
    private String textValue;

    @SerializedName("type")
    private String type;

}
