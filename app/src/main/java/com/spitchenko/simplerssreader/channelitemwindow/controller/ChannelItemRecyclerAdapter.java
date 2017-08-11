package com.spitchenko.simplerssreader.channelitemwindow.controller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.Toast;

import com.spitchenko.simplerssreader.R;
import com.spitchenko.simplerssreader.model.ChannelItem;
import com.spitchenko.simplerssreader.model.Theme;
import com.spitchenko.simplerssreader.utils.ThemeController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import lombok.NonNull;

/**
 * Date: 24.02.17
 * Time: 0:18
 *
 * @author anatoliy
 */
final class ChannelItemRecyclerAdapter extends RecyclerView.Adapter<ChannelItemRecyclerViewHolder> {
    private final static String ENCODING = "utf-8";
    private final static String START_TAGS_CUSTOM_COLOR
            = "<html><head><style>body{color: %s;}</style></head>"
            + "<body style='margin:0;padding:0;'>";
    private final static String START_TAGS = "<html><body style='margin:0;padding:0;'>";
    private final static String END_TAGS = "</body></html>";
    private final static String MIME_TYPE = "text/html";
    private final ArrayList<ChannelItem> channelItems;
    private Context context;
    private final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.ENGLISH);

    ChannelItemRecyclerAdapter(@NonNull final ArrayList<ChannelItem> channelItems) {
        this.channelItems = channelItems;
    }

    @Override
    public final ChannelItemRecyclerViewHolder onCreateViewHolder(@NonNull final ViewGroup parent
            , final int viewType) {
        context = parent.getContext();
        final View channelElement = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_item_element, parent, false);
        return new ChannelItemRecyclerViewHolder(channelElement);
    }

    @Override
    public void onViewRecycled(final ChannelItemRecyclerViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public final void onBindViewHolder(@NonNull final ChannelItemRecyclerViewHolder holder
            , final int position) {
        final ChannelItem bindChannel = channelItems.get(position);

        final ThemeController themeController = new ThemeController(context);
        final Theme currentTheme = themeController.getCurrentTheme();
        Integer textColorContent = null;
        if (null != currentTheme) {
            textColorContent = currentTheme.getTextColorContent();
        }

        holder.getTitleChannel().setText(bindChannel.getTitle());
        if (null != textColorContent) {
            holder.getTitleChannel().setTextColor(textColorContent);
        } else {
            holder.getTitleChannel().setTextColor(Color.BLACK);
        }

        if (null != currentTheme) {
            final String data
                    = String.format(START_TAGS_CUSTOM_COLOR, String.format("#%06X", (0xFFFFFF & currentTheme
                    .getTextColorContent()))) + bindChannel.getSubtitle()
                    + END_TAGS;

            holder.getSubtitleChannel().loadDataWithBaseURL(null, data
                    , MIME_TYPE, ENCODING, null);
        } else {
            final String data
                    = START_TAGS + bindChannel.getSubtitle()
                    + END_TAGS;

            holder.getSubtitleChannel().loadDataWithBaseURL(null, data
                    , MIME_TYPE, ENCODING, null);
        }

        final float fontSize
                = context.getResources().getDimension(R.dimen.channel_element_web_view_text_size);
        final WebSettings settings = holder.getSubtitleChannel().getSettings();
        settings.setDefaultTextEncodingName(ENCODING);
        settings.setDefaultFontSize((int) fontSize);
        holder.getSubtitleChannel().setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        if (null != bindChannel.getPubDate()) {
            final long edtTime = bindChannel.getPubDate().getTime();
            final long timezoneAlteredTime = edtTime + Calendar.getInstance().getTimeZone().getRawOffset();
            final Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timezoneAlteredTime);

            holder.getUpdateDate().setText(formatter.format(calendar.getTime()));
            if (null != textColorContent) {
                holder.getUpdateDate().setTextColor(textColorContent);
            }
        }
		if (!bindChannel.isRead()) {
			holder.getTitleChannel().setTypeface(null, Typeface.BOLD);
		} else {
            holder.getTitleChannel().setTypeface(null, Typeface.NORMAL);
        }

		holder.itemView.setOnClickListener (new View.OnClickListener() {
			@Override
			public void onClick(@NonNull final View v) {
				if (!channelItems.get(holder.getAdapterPosition()).isRead()) {
                    holder.getTitleChannel().setTypeface(null, Typeface.NORMAL);
                    RssChannelItemIntentService.start(channelItems.get(holder.getAdapterPosition())
                            , null
                            , RssChannelItemIntentService.getReadCurrentChannelKey(), context);
				}
				final Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse(channelItems.get(holder.getAdapterPosition()).getLink()));

				context.startActivity(intent);
			}
		});

        holder.getSubtitleChannel().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(@NonNull final View v, @NonNull final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if (!channelItems.get(holder.getAdapterPosition()).isRead()) {
                            holder.getTitleChannel().setTypeface(null, Typeface.NORMAL);
                            RssChannelItemIntentService.start(channelItems.get(holder.getAdapterPosition())
                                    , null
                                    , RssChannelItemIntentService.getReadCurrentChannelKey(), context);
                        }
                        final Intent intent = new Intent(Intent.ACTION_VIEW
                                , Uri.parse(channelItems.get(holder.getAdapterPosition()).getLink()));
                        try {
                            context.startActivity(intent);
                        } catch (final ActivityNotFoundException e) {
                            Toast.makeText(v.getContext(), v.getContext()
                                    .getString(R.string.channel_item_recycler_adapter_browser_missing), Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                return false;
            }
        });
	}

	@Override
	public final int getItemCount() {
		return channelItems == null ? 0 : channelItems.size();
	}
}
