package ca.mitenko.evn.ui.dest_map;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Destination;

/**
 * Created by mitenko on 2017-04-29.
 */

public class DestCardView extends CardView {
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
    @BindView(R.id.dest_card_short_desc)
    TextView shortDesc;

    /**
     * Constructor
     */
    public DestCardView(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     */
    public DestCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public DestCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Inflate layout
        LayoutInflater.from(context).inflate(R.layout.item_dest_single, this, true);
        ButterKnife.bind(this);
    }

    /**
     * Binds the destination data to the view
     * @param destination
     */
    public void bind(Destination destination) {
        thumbnail.setImageURI(destination.detail().thumbURL());
        title.setText(destination.detail().name());
        shortDesc.setText(destination.detail().shortDesc());
    }

    /**
     * Resets the bound data
     */
    public void unbind() {
        thumbnail.setImageURI("");
    }
}
