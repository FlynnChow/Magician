package com.flynnchow.zero.magician.gallery.view

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.flynnchow.zero.common.fragment.BindingFragment
import com.flynnchow.zero.common.helper.logDebug
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentGalleryBinding

class GalleryFragment:BindingFragment<FragmentGalleryBinding>(R.layout.fragment_gallery) {
    override fun onInitView() {

    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        mBinding.per.setOnClickListener {
            requestPermissions(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }
        mBinding.get.setOnClickListener {
            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
            Thread{
                val imageUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val imageStr = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT,
                    MediaStore.Images.Media.MIME_TYPE,
                )
                val cursor = requireActivity().contentResolver.query(
                    imageUri,
                    null,null,null,null
                )
                logDebug("开始  ${cursor?.count}")
                cursor?.run {
                    while (moveToNext()){
                        val id = cursor.getString(cursor.getColumnIndex(imageStr[0]))
                        val size = cursor.getInt(cursor.getColumnIndex(imageStr[1]))
                        val name = cursor.getString(cursor.getColumnIndex(imageStr[2]))
                        val data = cursor.getString(cursor.getColumnIndex(imageStr[3]))
                        val addDate = cursor.getString(cursor.getColumnIndex(imageStr[4]))
                        val modifiedDate = cursor.getString(cursor.getColumnIndex(imageStr[5]))
                        val width = cursor.getString(cursor.getColumnIndex(imageStr[6]))
                        val height = cursor.getString(cursor.getColumnIndex(imageStr[7]))
                        val type1 = cursor.getString(cursor.getColumnIndex(imageStr[8]))
                        logDebug("${id}  ${size}  ${name}  ${data} ${addDate} ${modifiedDate} ${width} ${height} ${type1}")
                    }
                }
            }.start()
        }
    }
}