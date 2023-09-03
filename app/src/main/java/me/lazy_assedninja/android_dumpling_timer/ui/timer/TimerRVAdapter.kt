package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.ItemTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseListAdapter
import timber.log.Timber
import kotlin.math.roundToInt

class TimerRVAdapter(private val maxProgress: Int = 0) :
    BaseListAdapter<Time, ItemTimerBinding>(diffCallback = object : DiffUtil.ItemCallback<Time>() {
        override fun areItemsTheSame(oldItem: Time, newItem: Time) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Time, newItem: Time) = oldItem.percentage == newItem.percentage
    }) {

    override fun createBinding(parent: ViewGroup) = ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemTimerBinding, item: Time) {
        Timber.tag("xxxxx").d("time: $item, max progress: $maxProgress")
        binding.max = maxProgress
        binding.progress = item.percentage.roundToInt()
    }
}
