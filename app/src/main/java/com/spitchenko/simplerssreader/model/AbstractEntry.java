package com.spitchenko.simplerssreader.model;

import com.spitchenko.simplerssreader.model.atom.Entry;
import com.spitchenko.simplerssreader.model.rss.Item;

/**
 * Date: 25.09.17
 * Time: 13:25
 *
 * @author anatoliy
 */

public class AbstractEntry {
    private Entry entry;
    private Item item;

    public AbstractEntry(Entry entry) {
        this.entry = entry;
    }

    public AbstractEntry(Item item) {
        this.item = item;
    }
}
