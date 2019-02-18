package ca.mitenko.evn.model

import android.os.Parcelable
import ca.mitenko.evn.CategoryConstants.UNKNOWN
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.google.maps.android.clustering.ClusterItem
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Created by mitenko on 2018-01-27.
 */

@Parcelize
data class Activity(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("category")
        val category: String? = UNKNOWN
): Parcelable

@Parcelize
data class Address(
        @SerializedName("id")
        val id: Int,
        @SerializedName("lineOne")
        val lineOne: String,
        @SerializedName("lineTwo")
        val lineTwo: String,
        @SerializedName("postalCode")
        val postalCode: String,
        @SerializedName("city")
        val city: String
): Parcelable {
    override fun toString() = "$lineOne $lineTwo $city $postalCode"
}

@Parcelize
data class Category(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("activities")
        val activities: @RawValue ArrayList<Activity>
): Parcelable

@Parcelize
data class Destination(
        @SerializedName("id")
        val id: Int,
        @SerializedName("latitude")
        val latitude: Float,
        @SerializedName("longitude")
        val longitude: Float,
        @SerializedName("address")
        val address: Address,
        @SerializedName("displayIcon")
        val displayIcon: String = UNKNOWN,
        @SerializedName("detail")
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
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("shortDesc")
        val shortDesc: String,
        @SerializedName("longDesc")
        val longDesc: String,
        @SerializedName("thumbURL")
        val thumbURL: String,
        @SerializedName("imageURL")
        val imageURL: String,
        @SerializedName("phone")
        val phone: String,
        @SerializedName("website")
        val website: String,
        @SerializedName("cost")
        val cost: Int,
        @SerializedName("email")
        val email: String,
        @SerializedName("activities")
        val activities: @RawValue ArrayList<Activity>
): Parcelable

@Parcelize
data class Event(
        @SerializedName("detail")
        val detail: Detail,
        @SerializedName("id")
        val id: Int,
        @SerializedName("priority")
        val priority: Int,
        @SerializedName("readablePriority")
        val readablePriority: String,
        @SerializedName("unixStartTime")
        val unixStartTime: Long,
        @SerializedName("readableStartTime")
        val readableStartTime: String,
        @SerializedName("unixEndTime")
        val unixEndTime: Long,
        @SerializedName("readableEndTime")
        val readableEndTime: String,
        @SerializedName("destinations")
        val destinations: ArrayList<Int>
): Parcelable