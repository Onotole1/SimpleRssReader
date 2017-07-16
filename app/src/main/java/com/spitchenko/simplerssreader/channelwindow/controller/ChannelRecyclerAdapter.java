package com.spitchenko.simplerssreader.channelwindow.controller;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.base.view.BaseActivity;
import com.spitchenko.simplerssreader.channelitemwindow.view.ChannelItemFragment;
import com.spitchenko.simplerssreader.model.Channel;
import com.spitchenko.simplerssreader.utils.ThemeController;

import java.util.ArrayList;

import lombok.Getter;
import lombok.NonNull;

/**
 * Date: 24.02.17
 * Time: 0:18
 *
 * @author anatoliy
 */
final class ChannelRecyclerAdapter extends RecyclerView.Adapter<ChannelRecyclerViewHolder> {
	private final @Getter ArrayList<Channel> channels;
	private Context context;

	ChannelRecyclerAdapter(final ArrayList<Channel> channels) {
		this.channels = channels;
	}

	@Override
	public final ChannelRecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent
            , final int viewType) {
		context = parent.getContext();
		final View channelElement = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_element, parent, false);
		return new ChannelRecyclerViewHolder(channelElement);
	}

	@Override
	public final void onBindViewHolder(@NonNull final ChannelRecyclerViewHolder holder
            , final int position) {
		final Channel bindChannel = channels.get(position);

		final ThemeController themeController = new ThemeController(context);
		final Integer textColorContent = themeController.getCurrentTheme().getTextColorContent();

		holder.getTitleChannel().setText(bindChannel.getTitle());
		holder.getSubtitleChannel().setText(bindChannel.getSubtitle());
		holder.getSubtitleChannel().setTextColor(textColorContent);


        if (null != bindChannel.getImage()) {
            holder.getImageChannel().setImageBitmap(bindChannel.getImage());
        } else {
            holder.getImageChannel().setImageResource(R.drawable.ic_rss_feed_amber_50_36dp);
        }

		if (!bindChannel.isRead()) {
			holder.getTitleChannel().setTypeface(null, Typeface.BOLD);
		} else {
            holder.getTitleChannel().setTypeface(null, Typeface.NORMAL);
        }

		holder.getTitleChannel().setTextColor(textColorContent);

		holder.itemView.setOnClickListener (new View.OnClickListener() {
			@Override
			public void onClick(@NonNull final View v) {
                holder.getTitleChannel().setTypeface(null, Typeface.NORMAL);
				if (!channels.get(holder.getAdapterPosition()).isRead()) {
                    RssChannelIntentService.start(RssChannelIntentService.getReadCurrentChannelKey()
                            , context, channels.get(holder.getAdapterPosition()), null);
				}

				ChannelItemFragment.start((channels.get(holder.getAdapterPosition()))
                        , (BaseActivity) context);
			}
		});
	}

	@Override
	public final int getItemCount() {
		return channels == null ? 0 : channels.size();
	}

	void removeItem(final int index) {
		channels.remove(index);
	}
}
