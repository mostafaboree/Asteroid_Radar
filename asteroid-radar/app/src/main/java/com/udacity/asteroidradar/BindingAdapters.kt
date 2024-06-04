package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.udacity.asteroidradar.models.PictureOfDay
import com.udacity.asteroidradar.ui.main.MainViewModel

/**
 * General purpose bindings.
 */

@BindingAdapter("bindText")
fun bindTextView(textView: TextView, text: String) {
    textView.text = text
    textView.contentDescription = text
}

/**
 * fragment_detail bindings
 */

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription = context.getString(R.string.hazardous_status_image_description)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_status_image_description)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.astronomical_unit_format), number)
    textView.text = text
    textView.contentDescription = text
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.km_unit_format), number)
    textView.text = text
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    val text = String.format(context.getString(R.string.km_s_unit_format), number)
    textView.text = text
    textView.contentDescription = text
}


/**
 * fragment_main bindings .
 */

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = context.getString(R.string.hazardous_status_image_description)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = context.getString(R.string.not_hazardous_status_image_description)
    }
}

@BindingAdapter("neoApiStatus")
fun bindStatus(progressBarView: ProgressBar, apiStatus: MainViewModel.ApiStatus) {
    progressBarView.visibility = if (apiStatus == MainViewModel.ApiStatus.LOADING) View.VISIBLE else View.GONE
}

@BindingAdapter("pictureOfDayImage")
fun bindPictureOfDayImage(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    pictureOfDay?.let {
        if (it.mediaType == "image") {
            Glide.with(imageView.context).load(pictureOfDay.url)
                .into(imageView)
            imageView.contentDescription = context.getString(R.string.nasa_picture_of_day_content_description_format)
        }

    }.run {
        imageView.setImageResource(R.drawable.ic_baseline_image_24)
        imageView.contentDescription = ("nasa_picture_of_day_download_error")
    }
}