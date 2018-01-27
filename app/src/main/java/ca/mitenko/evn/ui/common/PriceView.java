package ca.mitenko.evn.ui.common;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

import ca.mitenko.evn.R;

/**
 * Created by mitenko on 2017-07-28.
 */

public class PriceView extends AppCompatTextView {
    private static HashMap<Integer,String> priceDisplayMap;
    static {
        priceDisplayMap = new HashMap<>();
        priceDisplayMap.put(0, "FREE");
        priceDisplayMap.put(1, "$");
        priceDisplayMap.put(2, "$$");
        priceDisplayMap.put(3, "$$$");
        priceDisplayMap.put(4, "$$$$");
    }

    /**
     * Constructor
     */
    public PriceView(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     */
    public PriceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public PriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTextColor(ContextCompat.getColor(context,R.color.white));
        this.setBackground(ContextCompat.getDrawable(context,R.drawable.price_background));
    }

    /**
     * Takes the Detail defined cost and maps it to a dsplayable value
     */
    public void setCost(Integer cost) {
        if (priceDisplayMap.containsKey(cost)) {
            this.setText(priceDisplayMap.get(cost));
            this.setVisibility(View.VISIBLE);
        } else {
            this.setVisibility(View.INVISIBLE);
        }
    }
}
