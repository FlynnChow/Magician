package com.flynnchow.zero.magician.search.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.FragmentSearchBinding
import com.flynnchow.zero.magician.search.adapter.SearchAdapter
import com.flynnchow.zero.magician.search.viewmodel.SearchViewModel
import com.flynnchow.zero.ml_kit.MLKitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchFragment:MagicianFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel by lazy {
        getViewModel(requireActivity(),SearchViewModel::class.java)
    }
    private val adapter = SearchAdapter()

    override fun onInitView() {
        mBinding.listView.adapter = adapter
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onInitObserver() {
        super.onInitObserver()
        viewModel.data.observe(this, Observer {
            adapter.setData(it)
        })
        MediaProvider.instance.galleryUpdate.observe(this, {
            startLaunch {
                if (MLKitHelper.isHitLabel(it,viewModel.keyword)){
                    withContext(Dispatchers.Main){
                        adapter.updateData(it,mBinding.listView)
                    }
                }
            }
        })
        (mBinding.listView.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
    }
}