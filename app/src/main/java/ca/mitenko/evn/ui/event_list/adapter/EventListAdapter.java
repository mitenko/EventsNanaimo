package ca.mitenko.evn.ui.event_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Event;

/**
 * Created by mitenko on 2017-04-30.
 */

public class EventListAdapter extends RecyclerView.Adapter<EventViewHolder> {
    /**
     * Destination List
     */
    private ArrayList<Event> events;

    /**
     * The Event Bus
     */
    private EventBus bus;

    /**
     * Constructor
     */
    public EventListAdapter(EventBus bus) {
        this.bus = bus;
        events = new ArrayList<>();
    }

    /**
     * Fragment Callbacks
     */
    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    /**
     * Adapter Methods
     */
    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dest_list, parent, false);
        return new EventViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
    }



}
