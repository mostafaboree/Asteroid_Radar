package com.udacity.asteroidradar.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * POD used by the app and for parsing responses from api endpoint
 */

@Parcelize
data class PictureOfDay(
    val url: String,

    @Json(name = "media_type")
    val mediaType: String,
    val title: String
): Parcelable