package com.flynnchow.zero.magician.video_create.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.SeekBar
import com.flynnchow.zero.base.helper.LogDebug
import com.flynnchow.zero.common.fragment.MagicianFragment
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.FragmentCreatorAudioBinding
import com.flynnchow.zero.magician.selecter.AudioSelectorActivity
import com.flynnchow.zero.magician.video_create.viewmodel.VideoConfigViewModel

class VideoAudioFragment :
    MagicianFragment<FragmentCreatorAudioBinding>(R.layout.fragment_creator_audio) {

    private val configViewModel by lazy {
        getViewModel(requireActivity(), VideoConfigViewModel::class.java)
    }

    override fun onInitView() {
        mBinding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                configViewModel.setVolume(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        mBinding.selectAudio.setOnClickListener {
            AudioSelectorActivity.startPage(this, requireContext(), configViewModel.mAudio)
        }
    }

    override fun onInitData(isFirst: Boolean, savedInstanceState: Bundle?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == 0) {
            val name = data?.getStringExtra("name") ?: "<unknow name>"
            val uriString = data?.getStringExtra("uri")
            LogDebug("测试",name,uriString)
            uriString?.let {
                configViewModel.setAudio(Uri.parse(uriString), name)
            }
        }
    }

    override fun onInitObserver() {
        super.onInitObserver()
        mBinding.viewModel = configViewModel
    }
}