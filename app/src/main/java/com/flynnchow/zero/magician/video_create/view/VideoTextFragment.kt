package com.flynnchow.zero.magician.video_create.view

import android.os.Bundle
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentCreatorTextBinding
import com.flynnchow.zero.magician.video_create.viewmodel.VideoConfigViewModel

class VideoTextFragment:MagicianFragment<FragmentCreatorTextBinding>(R.layout.fragment_creator_text) {
    private val configViewModel by lazy {
        getViewModel(requireActivity(),VideoConfigViewModel::class.java)
    }
    override fun onInitView() {
        mBinding.viewModel = configViewModel
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }
}