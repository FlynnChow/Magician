package com.flynnchow.zero.magician.gallery.view

import android.os.Bundle
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ActivityGalleryBinding

class GalleryActivity : BindingActivity<ActivityGalleryBinding>(R.layout.activity_gallery) {
    override fun onInitView() {
        val fragment = TestFragment()
        supportFragmentManager.beginTransaction().replace(mBinding.galleryContainer.id,fragment).commitAllowingStateLoss()
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }
}