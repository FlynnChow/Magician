package com.flynnchow.zero.magician.func.base

import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentScrollerBinding
import com.flynnchow.zero.magician.func.viewmodel.FuncViewModel

abstract class FuncFragment:MagicianFragment<FragmentScrollerBinding>(R.layout.fragment_scroller) {
    val funcViewModel by lazy {
        getViewModel(requireActivity(), FuncViewModel::class.java)
    }
}