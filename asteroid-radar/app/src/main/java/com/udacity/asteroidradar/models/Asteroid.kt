package com.udacity.asteroidradar.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String, val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double, val isPotentiallyHazardous: Boolean
): Parcelable

/**
 * extension helper function to convert Array list of asteroid objects to list of database asteroid objects
 */

