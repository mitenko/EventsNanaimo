package ca.mitenko.evn.model

import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.LatLng

/**
 * Created by mitenko on 2017-07-16.
 */
data class Search(
        val SearchBounds: LatLngBounds =
            LatLngBounds(
                    LatLng(49.15938572687397,-123.9760036021471),
                    LatLng(49.20606374369103,-123.91420517116785)),
        val categories: ArrayList<String> = ArrayList<String>(),
        val activities: ArrayList<String> = ArrayList<String>())