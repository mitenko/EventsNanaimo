package ca.mitenko.evn.ui.dest_list.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.R;
import ca.mitenko.evn.event.DestItemClickEvent;
import ca.mitenko.evn.model.Activity;
import ca.mitenko.evn.model.Destination;
import ca.mitenko.evn.ui.common.CategoryView;
import ca.mitenko.evn.ui.common.PriceView;

/**
 * Created by mitenko on 2017-04-30.
 */

public class DestViewHolder extends RecyclerView.ViewHolder {
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
     * the price
     */
    @BindView(R.id.dest_card_price)
    PriceView priceView;

    /**
     * the category
     */
    @BindView(R.id.dest_category_view)
    CategoryView categoryView;

    /**
     * Constructor
     * @param view
     */
    public DestViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    /**
     * Binds the destination data to the view
     * @param destination
     */
    public void bind(Destination destination, EventBus bus) {
        this.itemView.setOnClickListener(view ->
                bus.post(new DestItemClickEvent(destination)));

        thumbnail.setImageURI(destination.getDetail().getImageURL());
        title.setText(destination.getDetail().getName().toUpperCase());
        shortDesc.setText(destination.getDetail().getLongDesc());
        priceView.setCost(destination.getDetail().getCost());
        categoryView.setCategories(destination.getDetail().getActivities(), false);

        ArrayList<Activity> destActivities = destination.getDetail().getActivities();
        HashSet<String> activityNames = new HashSet<>();
        for (Activity activity : destActivities) {
            activityNames.add(activity.getName());
        }
        String joinedActivites = TextUtils.join(" • ", activityNames);
        activities.setText(joinedActivites);
    }
}
