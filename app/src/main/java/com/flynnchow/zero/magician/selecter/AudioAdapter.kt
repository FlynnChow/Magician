package com.flynnchow.zero.magician.selecter

import android.view.View
import com.flynnchow.zero.component.multi_recyclerview.DataBindAdapter
import com.flynnchow.zero.magician.R
import com.flynnchow.zero.magician.databinding.ItemAudioBinding
import com.flynnchow.zero.model.AudioModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioAdapter : DataBindAdapter<ItemAudioBinding, MusicData>(R.layout.item_audio) {
    private var selectListener: ((AudioModel, repeat: Boolean) -> Boolean)? = null
    private var selectPosition = -1
    private var progress: Int = 0

    override fun onBind(binding: ItemAudioBinding, viewData: MusicData, position: Int) {
        binding.viewData = viewData
        binding.radio.isChecked = selectPosition == position
        binding.audioGroup.visibility = if (binding.radio.isChecked) View.VISIBLE else View.GONE
        binding.lottieView.visibility = binding.audioGroup.visibility
        binding.progress.progress = progress
        if (progress == 100){
            binding.player.setImageResource(R.drawable.player)
        }
        binding.rootView.setOnClickListener {
            val result = selectListener?.invoke(viewData.music, position == selectPosition)
            binding.player.setImageResource(if (result == true) R.drawable.pause else R.drawable.player)
            if (selectPosition in 0 until itemCount) {
                notifyItemChanged(selectPosition)
            }
            selectPosition = position
            binding.radio.isChecked = true
            binding.audioGroup.visibility = View.VISIBLE
            binding.lottieView.visibility = View.VISIBLE
            if (position == selectPosition) {
                if (result == true) {
                    binding.lottieView.playAnimation()
                } else {
                    binding.lottieView.pauseAnimation()
                }
            }else{
                binding.progress.progress = 0
                binding.lottieView.playAnimation()
            }
        }
    }

    fun setData(audioList: List<MusicData>) {
        data.clear()
        data.addAll(audioList)
        notifyDataSetChanged()
    }

    fun updateData(audio: MusicData) {
        for (position in 0 until data.size) {
            val viewData = data[position]
            if (audio.music.id == viewData.music.id) {
                data[position] = audio
                notifyItemChanged(position)
                break
            }
        }
    }

    fun addSelectListener(listener: ((AudioModel, repeat: Boolean) -> Boolean)) {
        selectListener = listener
    }

    suspend fun setProgress(position: Int, duration: Int) {
        withContext(Dispatchers.Main) {
            val nextProgress = ((position / duration.toFloat()) * 100).toInt()
            progress = nextProgress
            if (selectPosition in 0 until itemCount) {
                notifyItemChanged(selectPosition)
            }
        }
    }
}