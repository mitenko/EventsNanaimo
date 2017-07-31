package ca.mitenko.evn.ui.event_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Event;

/**
 * Created by mitenko on 2017-04-30.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    /**
     * the thumbnail image
     */
    @BindView(R.id.dest_card_thumbnail)
    SimpleDraweeView thumbnail;

    /**
     * the title text
     */
    @BindView(R.id.dest_card_title)
    TextView title;

    /**
     * the short description text
     */
    @BindView(R.id.dest_card_desc)
    TextView shortDesc;

    /**
     * the short description text
     */
    @BindView(R.id.dest_card_activities)
    TextView activities;

    /**
     * Constructor
     * @param view
     */
    public EventViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    /**
     * Binds the event data to the view
     * @param event
     */
    public void bind(Event event) {
        thumbnail.setImageURI(event.detail().imageURL());
        title.setText(event.detail().name().toUpperCase());
        shortDesc.setText(event.detail().shortDesc());

        ArrayList<Activity> eventActivities = event.detail().activities();
        ArrayList<String> activityNames = new ArrayList<>();
        for (Activity activity : eventActivities) {
            if (!activityNames.contains(activity.name())) {
                activityNames.add(activity.name());
            }
        }
        activities.setText(TextUtils.join(" ● ", activityNames));
    }
}
