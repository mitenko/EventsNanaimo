package ca.mitenko.evn.ui.common

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ca.mitenko.evn.CategoryConstants
import ca.mitenko.evn.R
import ca.mitenko.evn.model.Activity
import java.util.*

/**
 * Created by mitenko on 2017-07-28.
 */

class CategoryView
/**
 * {@inheritDoc}
 */
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        this.orientation = LinearLayoutCompat.HORIZONTAL
        this.gravity = Gravity.RIGHT
    }

    /**
     * Takes the Detail defined cost and maps it to a dsplayable value
     */
    fun setCategories(activities: ArrayList<Activity>, showText: Boolean) {
        this.removeAllViews()
        val inflater = LayoutInflater.from(context)

        val categories = HashSet<String>()
        for ((_, _, category) in activities) {
            categories.add(category ?: "")
        }
        for (category in categories) {
            inflater.inflate(
                    R.layout.item_category_icon, this, true)
            val iconLayout = this.getChildAt(this.childCount - 1) as LinearLayout

            val icon = iconLayout.findViewById(R.id.category_icon) as ImageView
            val image = ContextCompat.getDrawable(context,
                    CategoryConstants.categoryIconMap[category] ?: R.drawable.ic_lifestyle)
            icon.setImageDrawable(image)

            if (showText) {
                val textView = iconLayout.findViewById(R.id.category_text) as TextView
                val capitalizedCategory = category.substring(0, 1).toUpperCase() + category.substring(1).toLowerCase()
                textView.text = capitalizedCategory
            }
        }
    }
}
/**
 * Constructor
 */
/**
 * Constructor
 */
