package com.example.deneme1.common

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.deneme1.R
import java.text.SimpleDateFormat
import java.util.*

/*
@BindingAdapter(value = ["bindParseObject", "textType"], requireAll = false)
fun bindParseObject(
    textView: TextView,
    parseObject: ParseObject,
    type: String?


) {
    textView.let {
        textView.text = type?.let { it1 -> parseObject.get(it1).toString() }
    }
}

 */

@BindingAdapter("showClock" )
fun showClock(
    textView: TextView,
    date: Date
) {
    textView.let {
        val sdf = SimpleDateFormat("HH:mm")
        textView.text  = sdf.format(date)
    }
}

@BindingAdapter("imageCircleFromUrl")
fun imageCircleFromUrl(view: ImageView, url: String?) {
    Glide.with(view.context).load(url).circleCrop().placeholder(R.drawable.ic_baseline_account_box_24).into(view)
}

@BindingAdapter("showDate")
fun showDate(view: TextView, date: Date?) {

    val sdf = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

    if(date!=null){
        view.text=sdf.format(date)
    }else{
        view.text=""
    }
}