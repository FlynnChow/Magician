package com.flynnchow.zero.magician.main

import android.util.SparseArray
import android.view.View
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.PhotoAlbumFragment
import com.flynnchow.zero.magician.databinding.ActivityMainBinding
import com.flynnchow.zero.magician.gallery.view.GalleryFragment
import com.flynnchow.zero.magician.store.StoreFragment

class MainPageAdapter(
    private val fragmentManager: FragmentManager,
    private val mainBinding: ActivityMainBinding
) {
    private var lastId: Int? = null

    private val fragmentArray: SparseArray<Fragment> = SparseArray()

    init {
        mainBinding.pageAdapter = this
        fragmentArray.put(R.id.main_gallery, GalleryFragment())
        fragmentArray.put(R.id.main_photo_album, PhotoAlbumFragment())
        fragmentArray.put(R.id.main_store, StoreFragment())
    }

    fun switchPage(view: View) {
        if (lastId == view.id) {
            return
        }
        val lastFragment = if (lastId != null) fragmentArray[lastId!!] else null
        val nextFragment = fragmentArray[view.id]
        nextFragment?.run {
            replaceFragment(lastFragment, this)
            lastId = view.id
        }
    }

    private fun replaceFragment(lastFragment: Fragment?, nextFragment: Fragment) {
        val transaction = fragmentManager.beginTransaction()
        lastFragment?.run {
            transaction.hide(this)
        }
        if (!nextFragment.isAdded) {
            transaction.add(
                mainBinding.mainContainer.id,
                nextFragment,
                nextFragment::class.java.name
            )
        }
        transaction.show(nextFragment).commitAllowingStateLoss()
    }
}