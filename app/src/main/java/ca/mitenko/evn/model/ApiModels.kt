package ca.mitenko.evn.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Created by mitenko on 2018-01-27.
 */

@Parcelize
data class Activity(
        val id: Int,
        val name: String,
        val category: String
): Parcelable

@Parcelize
data class Address(
        val id: Int,
        val lineOne: String,
        val lineTwo: String,
        val postalCode: String,
        val city: String
): Parcelable {
    override fun toString() = "$lineOne $lineTwo $city $postalCode"
}

@Parcelize
data class Category(
        val id: Int,
        val name: String,
        val activities: @RawValue ArrayList<Activity>
): Parcelable

@Parcelize
data class Destination(
        val id: Int,
        val latitude: Float,
        val longitude: Float,
        val address: Address,
        val displayIcon: String,
        val detail: Detail
): Parcelable, ClusterItem {
    /**
     * The position of this marker. This must always return the same value.
     */
    override fun getPosition() = LatLng(latitude.toDouble(), longitude.toDouble())

    /**
     * The title of this marker.
     */
    override fun getTitle()= detail.name

    /**
     * The description of this marker.
     */
    override fun getSnippet() = ""
}

@Parcelize
data class Detail(
        val id: Int,
        val name: String,
        val shortDesc: String,
        val longDesc: String,
        val thumbURL: String,
        val imageURL: String,
        val phone: String,
        val website: String,
        val cost: Int,
        val activities: @RawValue ArrayList<Activity>
): Parcelable

@Parcelize
data class Event(
        val id: Int,
        val detail: Detail,
        val readableStartTime: String,
        val readableEndTime: String,
        val unixStartTime: Long,
        val unixEndTime: Long,
        val destinations: ArrayList<Int>
): Parcelable