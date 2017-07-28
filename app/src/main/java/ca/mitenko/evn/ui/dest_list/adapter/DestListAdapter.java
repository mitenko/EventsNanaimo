package ca.mitenko.evn.ui.dest_list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-04-30.
 */

public class DestListAdapter extends RecyclerView.Adapter<DestViewHolder> {
    /**
     * Destination List
     */
    private ArrayList<Destination> destinations;

    /**
     * The Event Bus
     */
    private EventBus bus;

    /**
     * Constructor
     */
    public DestListAdapter(EventBus bus) {
        this.bus = bus;
        destinations = new ArrayList<>();
    }

    /**
     * Fragment Callbacks
     */
    public void setDestinationResult(ArrayList<Destination> destinations) {
        this.destinations = destinations;
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
        return destinations.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dest_list, parent, false);
        return new DestViewHolder(view);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBindViewHolder(DestViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.bind(destination);
    }



}
