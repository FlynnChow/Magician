package com.flynnchow.zero.magician.album.view

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.album.adapter.ClassificationAdapter
import com.flynnchow.zero.magician.base.provider.MediaProvider
import com.flynnchow.zero.magician.databinding.FragmentClassificationBinding
import com.google.android.material.transition.platform.MaterialSharedAxis

class ClassificationFragment :
    MagicianFragment<FragmentClassificationBinding>(R.layout.fragment_classification) {
    companion object {
        fun createClassificationFragment(target: String,count:Int = 3): ClassificationFragment {
            val data = Bundle().apply {
                putString("target", target)
                putInt("count",count)
            }
            return ClassificationFragment().apply {
                arguments = data
            }
        }
    }

    private var adapter: ClassificationAdapter? = null

    override fun onInitView() {

    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {
        val target = arguments?.getString("target")
        val count = arguments?.getInt("count", 3) ?: 3
        target?.let {
            adapter = ClassificationAdapter(it)
            mBinding.listView.adapter = adapter
            mBinding.listView.layoutManager = GridLayoutManager(requireContext(), count)
        }
    }

    override fun onInitObserver() {
        super.onInitObserver()
        MediaProvider.instance.galleryData.observe(this, {
            startLaunch {
                adapter?.initData(it)
            }
        })
        MediaProvider.instance.galleryUpdate.observe(this, {
            startLaunch {
                adapter?.updateData(it)
            }
        })
    }

    override fun onCreateBefore() {
        super.onCreateBefore()
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ true)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, /* forward= */ false)
    }
}