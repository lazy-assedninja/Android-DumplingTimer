package me.lazy_assedninja.android_dumpling_timer.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.ItemTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseListAdapter
import timber.log.Timber

enum class Type { Timer, Done }

class TimerRVAdapter(private val type: Type, private val dismissDialog: () -> Unit = {}) :
    BaseListAdapter<Time, ItemTimerBinding>(diffCallback = object : DiffUtil.ItemCallback<Time>() {
        override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem.time == newItem.time
        }
    }) {

    override fun createBinding(parent: ViewGroup) = ItemTimerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemTimerBinding, item: Time) {
        when (type) {
            Type.Timer -> binding.string = item.time.toString()
            else -> {
                binding.root.setOnClickListener {
                    submitList(currentList.toMutableList().apply {
                        remove(item)
                        Timber.tag("yyy").d("${this.size}")
                        if (this.size == 0) dismissDialog()
                    })
                }
                binding.string = item.id.toString()
            }
        }
    }
}