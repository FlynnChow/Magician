package com.flynnchow.zero.common.helper

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.flynnchow.zero.common.R
import com.flynnchow.zero.model.MediaModel
import com.luck.picture.lib.PictureSelectionModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.animators.AnimationType
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.stfalcon.imageviewer.StfalconImageViewer

object ImageHelper {
    private val imageOption = RequestOptions()

    private val headOption = RequestOptions()

    private const val miniSize = 300

    private const val mediumSize = 450

    private const val highSize = 1000

    @JvmStatic
    @BindingAdapter("res")
    fun loadIcon(imageView: ImageView,@RawRes src: Int?) {
        src?.run {
            Glide.with(imageView).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).load(src).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("img")
    fun loadImageView(imageView: ImageView, src: Any?) {
        src?.run {
            Glide.with(imageView).load(src).apply(imageOption).thumbnail(0.1f).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("head_img")
    fun loadHeader(imageView: ImageView, src: Any?) {
        src?.run {
            Glide.with(imageView).load(src).apply(headOption).thumbnail(0.1f).into(imageView)
        }
    }

    @JvmStatic
    @BindingAdapter("img_op")
    fun loadImageViewOption(imageView: ImageView, src: Any?) {
        Glide.with(imageView).load(src).apply(imageOption).thumbnail(0.1f).into(imageView)
    }

    @JvmStatic
    @BindingAdapter("head_img_op")
    fun loadHeaderOption(imageView: ImageView, src: Any?) {
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
            if (src != null){
                Glide.with(imageView).asDrawable().load(src).apply(RequestOptions().apply {
                }).override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
            }else{
                Glide.with(imageView).asDrawable().load(R.drawable.picture).apply(RequestOptions().apply {
                }).override(width / 2, height / 2).thumbnail(0.1f).into(imageView)
            }
        }
    }

    fun viewImage(view: ImageView, images: Array<String?>, position: Int) {
        StfalconImageViewer.Builder(view.context, images) { view, image ->
            Glide.with(view).load(image).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withStartPosition(position)
            .withHiddenStatusBar(true).show()
    }

    fun viewImage(view: ImageView, images: Array<MediaModel>, position: Int) {
        StfalconImageViewer.Builder(view.context, images) { view, image ->
            Glide.with(view).load(image.getUri()).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withStartPosition(position)
            .withHiddenStatusBar(true).show()
    }

    fun viewImage(view: ImageView, image: MediaModel) {
        StfalconImageViewer.Builder(view.context, arrayOf(image)) { view, image ->
            Glide.with(view).load(image.getUri()).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withHiddenStatusBar(true).show()
    }

    fun viewImage(view: ImageView, images: Array<Uri>, position: Int) {
        StfalconImageViewer.Builder(view.context, images) { view, image ->
            Glide.with(view).load(image).into(view)
        }.withTransitionFrom(view).withBackgroundColor(Color.WHITE).withStartPosition(position)
            .withHiddenStatusBar(true).show()
    }

    fun selectPicture(context: Fragment, requestCode: Int) {
        selectorImage(PictureSelector.create(context), requestCode)
    }

    fun selectPicture(context: Activity, requestCode: Int) {
        selectorImage(PictureSelector.create(context), requestCode)
    }

    private fun selectorImage(selector: PictureSelector, requestCode: Int) {
        selectorConfig(selector.openGallery(PictureMimeType.ofImage()), requestCode)
    }

    private fun selectorConfig(selector: PictureSelectionModel, requestCode: Int) {
        selector
            .imageEngine(GlideEngine.createGlideEngine())
            .theme(R.style.picture_default_style)
            .selectionMode(PictureConfig.MULTIPLE)
            .isCamera(false)
            .imageFormat(PictureMimeType.ofJPEG())
            .maxSelectNum(100)
            .minSelectNum(4)
            .isGif(false)
            .isPreviewImage(false)
            .setRecyclerAnimationMode(AnimationType.SLIDE_IN_BOTTOM_ANIMATION)
            .forResult(requestCode)
    }
}