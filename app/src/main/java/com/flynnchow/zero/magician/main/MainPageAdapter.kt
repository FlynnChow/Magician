package com.flynnchow.zero.magician.main

import android.graphics.Typeface
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import com.flynnchow.zero.base.BaseApplication
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.view.PhotoAlbumFragment
import com.flynnchow.zero.magician.databinding.ActivityMainBinding
import com.flynnchow.zero.magician.gallery.view.GalleryFragment
import com.flynnchow.zero.magician.store.view.StoreFragment

class MainPageAdapter(
    private val fragmentManager: FragmentManager,
    private val mainBinding: ActivityMainBinding
) : LifecycleObserver {
    private var lastView: View? = null

    private val fragmentArray: SparseArray<Fragment> = SparseArray()

    init {
        mainBinding.pageAdapter = this
        fragmentArray.put(mainBinding.mainNav.mainGallery.mainTab.id, GalleryFragment())
        fragmentArray.put(mainBinding.mainNav.mainPhotoAlbum.mainTab.id, PhotoAlbumFragment())
        fragmentArray.put(mainBinding.mainNav.mainStore.mainTab.id, StoreFragment())
    }

    fun switchPage(view: View) {
        if (lastView == view) {
            return
        }
        val lastFragment = if (lastView != null) fragmentArray[lastView!!.id] else null
        val nextFragment = fragmentArray[view.id]
        nextFragment?.run {
            replaceFragment(lastFragment, this)
            updateTabView(lastView, view)
            lastView = view
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

    private fun updateTabView(lastView: View?, nextView: View) {
        lastView?.run {
            val labText = lastView.findViewById<TextView>(R.id.main_tab_name)
            labText.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_black_2))
            labText.typeface = Typeface.DEFAULT
            val labIndicator = lastView.findViewById<View>(R.id.main_indicator)
            labIndicator.visibility = View.GONE
        }
        val labText = nextView.findViewById<TextView>(R.id.main_tab_name)
        labText.setTextColor(BaseApplication.instance.resources.getColor(R.color.font_black))
        labText.typeface = Typeface.DEFAULT_BOLD
        val labIndicator = nextView.findViewById<View>(R.id.main_indicator)
        labIndicator.visibility = View.VISIBLE
    }
}