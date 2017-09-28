package com.spitchenko.simplerssreader.model;

import com.spitchenko.simplerssreader.BuildConfig;
import com.spitchenko.simplerssreader.model.atom.Entry;
import com.spitchenko.simplerssreader.model.atom.Feed;
import com.spitchenko.simplerssreader.model.atom.Link;
import com.spitchenko.simplerssreader.model.rss.Channel;
import com.spitchenko.simplerssreader.model.rss.Item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import lombok.NonNull;

/**
 * Date: 25.09.17
 * Time: 13:24
 *
 * @author anatoliy
 */

public class AbstractFeed {
    private final ArrayList<AbstractEntry> abstractEntries;
    private Feed feed;
    private Channel channel;

    public AbstractFeed(@NonNull final Feed feed) throws NullPointerException {
        if (BuildConfig.DEBUG && null == feed.getEntry() || feed.getEntry().isEmpty()) {
            throw new NullPointerException();
        }

        this.feed = feed;

        abstractEntries = new ArrayList<>(feed.getEntry().size());

        final List<Entry> entries = feed.getEntry();
        for (int i = 0, entry1Size = entries.size(); i < entry1Size; i++) {
            final Entry entry = entries.get(i);
            abstractEntries.add(new AbstractEntry(entry));
        }
    }

    public AbstractFeed(@NonNull final Channel channel) throws NullPointerException {
        if (BuildConfig.DEBUG && null == channel.getItem() || channel.getItem().isEmpty()) {
            throw new NullPointerException();
        }

        this.channel = channel;

        abstractEntries = new ArrayList<>(channel.getItem().size());

        final List<Item> channelItems = channel.getItem();
        for (int i = 0, entry1Size = channelItems.size(); i < entry1Size; i++) {
            final Item item = channelItems.get(i);
            abstractEntries.add(new AbstractEntry(item));
        }
    }

    public List<AbstractEntry> getAllEntries() {
        return abstractEntries;
    }

    @Nullable
    public String getDescription() {
        if (null != channel && null != channel.getDescription()) {
            return channel.getDescription();
        } else if (null != feed && null != feed.getSubtitle()) {
            return feed.getSubtitle();
        } else {
            return null;
        }
    }

    @Nullable
    public String getLink() {
        if (null != channel && null != channel.getLink()) {
            return channel.getDescription();
        } else if (null != feed && null != feed.getLink() && feed.getLink().size() != 0) {

            final List<Link> feedLink = feed.getLink();
            for (int i = 0, link1Size = feedLink.size(); i < link1Size; i++) {
                final Link link = feedLink.get(i);
                if (null != link.getRel() && link.getRel().equals("self")) {
                    return link.getHref();
                }
            }

        }

        return null;
    }
}
