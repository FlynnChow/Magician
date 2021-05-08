package com.flynnchow.zero.magician.video_create.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentCreatorTransitionsBinding
import com.flynnchow.zero.magician.video_create.adapter.EffectListAdapter
import com.flynnchow.zero.magician.video_create.viewdata.EffectData
import com.flynnchow.zero.magician.video_create.viewmodel.VideoConfigViewModel
import com.flynnchow.zero.video_codec.TransitionProvider
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

class VideoTransitionFragment :
    MagicianFragment<FragmentCreatorTransitionsBinding>(R.layout.fragment_creator_transitions) {
    private val adapter = EffectListAdapter()
    private val configViewModel by lazy {
        getViewModel(requireActivity(), VideoConfigViewModel::class.java)
    }

    override fun onInitView() {
        mBinding.seekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                configViewModel.setVideoSpeed(seekParams?.progress ?: 2)
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

            }

        }
        mBinding.listView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mBinding.listView.adapter = adapter
        adapter.addData(TransitionProvider.transientList.map {
            val effect = EffectData(it, TransitionProvider.transientName[it] ?: "", TransitionProvider.transientIcons[it])
            effect.onCheckListener = { id ->
                configViewModel.setTransition(id)
            }
            effect
        })
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }
}