package ca.mitenko.evn.ui.event_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.EventItemClickEvent;
import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Event;
import ca.mitenko.evn.ui.common.CategoryView;
import ca.mitenko.evn.ui.common.PriceView;

/**
 * Created by mitenko on 2017-04-30.
 */

public class EventViewHolder extends RecyclerView.ViewHolder {
    /**
     * the thumbnail image
     */
    @BindView(R.id.event_card_thumbnail)
    SimpleDraweeView thumbnail;

    /**
     * the short description text
     */
    @BindView(R.id.event_card_date)
    TextView date;

    /**
     * the title text
     */
    @BindView(R.id.event_card_title)
    TextView title;

    /**
     * the short description text
     */
    @BindView(R.id.event_card_desc)
    TextView shortDesc;

    /**
     * the short description text
     */
    @BindView(R.id.event_card_activities)
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
    public void bind(Event event, EventBus bus) {
        this.itemView.setOnClickListener(view ->
                bus.post(new EventItemClickEvent(event)));

        thumbnail.setImageURI(event.getDetail().getImageURL()   );
        title.setText(event.getDetail().getName().toUpperCase());
        shortDesc.setText(event.getDetail().getLongDesc());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        String formattedDate = dateFormat.format(new Date(event.getUnixStartTime() * 1000)) + " to "
                + dateFormat.format(new Date(event.getUnixEndTime() * 1000));
        date.setText(formattedDate);

        ArrayList<Activity> destActivities = event.getDetail().getActivities();
        HashSet<String> activityNames = new HashSet<>();
        for (Activity activity : destActivities) {
            activityNames.add(activity.getName());
        }
        String joinedActivites = TextUtils.join(" • ", activityNames);
        activities.setText(joinedActivites);
    }
}
