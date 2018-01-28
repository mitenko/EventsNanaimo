package ca.mitenko.evn.ui.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import ca.mitenko.evn.CategoryConstants;
import ca.mitenko.evn.R;
import ca.mitenko.evn.model.Activity;

/**
 * Created by mitenko on 2017-07-28.
 */

public class CategoryView extends LinearLayoutCompat {
    /**
     * Constructor
     */
    public CategoryView(Context context) {
        this(context, null);
    }

    /**
     * Constructor
     */
    public CategoryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * {@inheritDoc}
     */
    public CategoryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.RIGHT);
    }

    /**
     * Takes the Detail defined cost and maps it to a dsplayable value
     */
    public void setCategories(ArrayList<Activity> activities, boolean showText) {
        this.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        HashSet<String> categories = new HashSet<>();
        for (Activity activity : activities) {
            categories.add(activity.getCategory());
        }
        for(String category : categories) {
            inflater.inflate(
                    R.layout.item_category_icon, this, true);
            LinearLayout iconLayout = (LinearLayout)
                    this.getChildAt(this.getChildCount()-1);

            ImageView icon = (ImageView) iconLayout.findViewById(R.id.category_icon);
            Drawable image = ContextCompat.getDrawable(getContext(),
                    CategoryConstants.categoryIconMap.get(category));
            icon.setImageDrawable(image);

            if (showText) {
                TextView textView = (TextView) iconLayout.findViewById(R.id.category_text);
                String capitalizedCategory =
                        category.substring(0,1).toUpperCase() + category.substring(1).toLowerCase();
                textView.setText(capitalizedCategory);
            }
        }
    }
}
