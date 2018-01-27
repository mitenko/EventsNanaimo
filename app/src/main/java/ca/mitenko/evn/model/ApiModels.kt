package ca.mitenko.evn.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

/**
 * Created by mitenko on 2018-01-27.
 */
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