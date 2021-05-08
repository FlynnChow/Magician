package com.flynnchow.zero.magician.video_create.view

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentCreatorEffectsBinding
import com.flynnchow.zero.magician.video_create.adapter.EffectListAdapter
import com.flynnchow.zero.magician.video_create.viewdata.EffectData
import com.flynnchow.zero.magician.video_create.viewmodel.VideoConfigViewModel
import com.flynnchow.zero.video_codec.EffectProvider
import com.flynnchow.zero.video_codec.PanProvider
import com.flynnchow.zero.video_codec.ZoomProvider

class VideoEffectFragment:MagicianFragment<FragmentCreatorEffectsBinding>(R.layout.fragment_creator_effects) {
    private val zoomAdapter = EffectListAdapter()
    private val panAdapter = EffectListAdapter()
    private val effectAdapter = EffectListAdapter()
    private val configViewModel by lazy {
        getViewModel(requireActivity(),VideoConfigViewModel::class.java)
    }
    override fun onInitView() {
        mBinding.effectListView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        mBinding.zoomListView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        mBinding.panView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)

        mBinding.zoomListView.adapter = zoomAdapter
        mBinding.panView.adapter = panAdapter
        mBinding.effectListView.adapter = effectAdapter

        zoomAdapter.addData(ZoomProvider.zoomList.map {
            val effect = EffectData(it, ZoomProvider.zoomName[it] ?: "", ZoomProvider.zoomIcons[it])
            effect.onCheckListener = { id ->
                configViewModel.setZoom(id)
            }
            effect
        })
        panAdapter.addData(PanProvider.panList.map {
            val effect = EffectData(it, PanProvider.panName[it] ?: "", PanProvider.panIcons[it])
            effect.onCheckListener = { id ->
                configViewModel.setPan(id)
            }
            effect
        })
        effectAdapter.addData(EffectProvider.effectList.map {
            val effect = EffectData(it, EffectProvider.effectName[it] ?: "", EffectProvider.effectIcons[it])
            effect.onCheckListener = { id ->
                configViewModel.setEffect(id)
            }
            effect
        })
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }
}