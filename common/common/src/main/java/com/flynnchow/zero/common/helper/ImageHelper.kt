package com.flynnchow.zero.common.helper

import android.graphics.Color
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.flynnchow.zero.model.MediaModel
import com.stfalcon.imageviewer.StfalconImageViewer

object ImageHelper {
    private val imageOption = RequestOptions()

    private val headOption = RequestOptions()

    private const val miniSize = 300

    private const val mediumSize = 450

    private const val highSize = 1000

    @JvmStatic
    @BindingAdapter("img")
    fun loadImageView(imageView: ImageView, src: Any?) {
        Glide.with(imageView).load(src).apply(imageOption).thumbnail(0.1f).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("head_img")
    fun loadHeader(imageView: ImageView, src: Any?) {
        Glide.with(imageView).load(src).apply(headOption).thumbnail(0.1f).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("img_mini")
    fun loadCompressMiniImageView(imageView: ImageView, src: Any?) {
        imageView.post {
            var width = imageView.width
            var height = imageView.height
            if (width > height && width > miniSize) {
                height = miniSize * height / width
                width = miniSize
            } else if (height > width && height > miniSize) {
                width = miniSize * width / height
                height = miniSize
            }

            Glide.with(imageView).asDrawable().load(src).apply(imageOption)
                .override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("img_medium")
    fun loadCompressMediumImageView(imageView: ImageView, src: Any?) {
        imageView.post {
            var width = imageView.width
            var height = imageView.height
            if (width > height && width > mediumSize) {
                height = mediumSize * height / width
                width = mediumSize
            } else if (height > width && height > mediumSize) {
                width = mediumSize * width / height
                height = mediumSize
            }

            Glide.with(imageView).asDrawable().load(src).apply(imageOption)
                .override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("img_high")
    fun loadCompressHighImageView(imageView: ImageView, src: Any?) {
        imageView.post {
            var width = imageView.width
            var height = imageView.height
            if (width > height && width > highSize) {
                height = highSize * height / width
                width = highSize
            } else if (height > width && height > highSize) {
                width = highSize * width / height
                height = highSize
            }

            Glide.with(imageView).asDrawable().load(src).apply(imageOption)
                .override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("album")
    fun loadCompressAlbum(imageView: ImageView, src: Any?) {
        imageView.post {
            var width = imageView.width
            var height = imageView.height
            if (width > height && width > mediumSize) {
                height = mediumSize * height / width
                width = mediumSize
            } else if (height > width && height > mediumSize) {
                width = mediumSize * width / height
                height = mediumSize
            }

            Glide.with(imageView).asDrawable().load(src).apply(RequestOptions().apply {

            }).override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
        }
    }

    fun viewImage(view: ImageView, images: Array<MediaModel>,position:Int) {
        StfalconImageViewer.Builder(view.context, images) { view, image ->
            Glide.with(view).load(image.getUri()).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withStartPosition(position).withHiddenStatusBar(true).show()
    }

    fun viewImage(view: ImageView, image: MediaModel) {
        StfalconImageViewer.Builder(view.context, arrayOf(image)) { view, image ->
            Glide.with(view).load(image.getUri()).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withHiddenStatusBar(true).show()
    }
}