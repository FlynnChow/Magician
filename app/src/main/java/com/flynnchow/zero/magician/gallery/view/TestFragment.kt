package com.flynnchow.zero.magician.gallery.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynnchow.zero.common.fragment.BindingFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentTestBinding

class TestFragment:BindingFragment<FragmentTestBinding>(R.layout.fragment_test) {
    private val adapter = TestAdapter()
    override fun onInitView() {
        mBinding.itemView.adapter = adapter
        mBinding.itemView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        mBinding.layoutA.setOnClickListener {
            adapter.type = 0
            adapter.notifyItemRangeChanged(0,adapter.itemCount)
        }
        mBinding.layoutB.setOnClickListener {
            adapter.type = 1
            adapter.notifyItemRangeChanged(0,adapter.itemCount)
        }
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }
}