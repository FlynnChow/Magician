package com.flynnchow.zero.common.helper

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

object ImageHelper {
    private val imageOption = RequestOptions()
        .centerCrop()

    private val headOption = RequestOptions()

    @JvmStatic
    @BindingAdapter("url")
    fun loadImageView(imageView: ImageView, src: String?) {
        Glide.with(imageView).load(src).apply(imageOption).into(imageView)
    }


    @JvmStatic
    @BindingAdapter("res")
    fun loadImageView(imageView: ImageView, @RawRes src: Int?) {
        Glide.with(imageView).load(src).apply(imageOption).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("bitmap")
    fun loadImageView(imageView: ImageView, src: Bitmap?) {
        Glide.with(imageView).load(src).apply(imageOption).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("uri")
    fun loadImageView(image: ImageView, src: Uri?) {
        Glide.with(image).load(src).apply(imageOption).into(image)
    }

    @JvmStatic
    @BindingAdapter("head_url")
    fun loadHeader(imageView: ImageView, src: String?) {
        Glide.with(imageView).load(src).apply(headOption).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("head_res")
    fun loadHeader(imageView: ImageView, @RawRes src: Int?) {
        Glide.with(imageView).load(src).apply(headOption).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("head_bitmap")
    fun loadHeader(imageView: ImageView, src: Bitmap?) {
        Glide.with(imageView).load(src).apply(headOption).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("head_uri")
    fun loadHeader(imageView: ImageView, src: Uri?) {
        Glide.with(imageView).load(src).apply(headOption).into(imageView)
    }
}