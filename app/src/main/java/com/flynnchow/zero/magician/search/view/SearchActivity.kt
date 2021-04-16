package com.flynnchow.zero.magician.search.view

import android.os.Bundle
import android.os.Parcelable
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.flynnchow.zero.common.activity.BindingActivity
import com.flynnchow.zero.component.view.ViewUtils
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.view.ClassificationFragment
import com.flynnchow.zero.magician.databinding.ActivitySearchBinding
import com.flynnchow.zero.magician.search.viewmodel.SearchViewModel
import com.hw.ycshareelement.YcShareElement
import com.hw.ycshareelement.transition.IShareElements
import com.hw.ycshareelement.transition.ShareElementInfo

class SearchActivity : BindingActivity<ActivitySearchBinding>(R.layout.activity_search) {
    private val searchViewModel by lazy { getViewModel(SearchViewModel::class.java) }
    private val fragment = SearchFragment()
    private var lastFragment:Fragment? = null

    override fun onInitView() {
        ViewUtils.showSoftInput(mBinding.search)
        mBinding.hint.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ViewUtils.hideSoftInput(this@SearchActivity)
                    searchViewModel.onSearch(mBinding.hint.text.toString())
                    hideFragment()
                    switchFragment(fragment)
                    return true
                }
                return false
            }
        })
    }

    override fun onInitData(savedInstanceState: Bundle?) {

    }

    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            transaction.add(mBinding.container.id, fragment)
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment!!)
        }
        lastFragment = fragment
        transaction.show(fragment).commitAllowingStateLoss()
    }

    private fun hideFragment(){
        val transaction = supportFragmentManager.beginTransaction()
        if (lastFragment != null) {
            transaction.hide(lastFragment!!)
        }
        transaction.show(fragment).commitAllowingStateLoss()
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        YcShareElement.setEnterTransitions(this, object : IShareElements {
            override fun getShareElements(): Array<ShareElementInfo<Parcelable>> {
                return arrayOf(
                    ShareElementInfo(mBinding.search)
                )
            }
        })
        YcShareElement.startTransition(this)
    }
}