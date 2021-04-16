package com.flynnchow.zero.magician.album.viewdata

import android.net.Uri
import com.flynnchow.zero.magician.album.view.ClassificationFragment
import com.flynnchow.zero.model.MLPhotoAlbum

class AlbumTitleData(val album:MLPhotoAlbum) {
    val fragment = ClassificationFragment.createClassificationFragment(album.key,2)
    var position:Int = 0
    var uri:Uri? = null
}