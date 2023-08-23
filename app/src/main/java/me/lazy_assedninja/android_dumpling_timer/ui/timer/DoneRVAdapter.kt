package me.lazy_assedninja.android_dumpling_timer.ui.timer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import me.lazy_assedninja.android_dumpling_timer.data.vo.Time
import me.lazy_assedninja.android_dumpling_timer.databinding.ItemDoneBinding
import me.lazy_assedninja.android_dumpling_timer.databinding.ItemTimerBinding
import me.lazy_assedninja.android_dumpling_timer.ui.base.BaseListAdapter
import kotlin.math.roundToInt

class DoneRVAdapter(private val dismissDialog: () -> Unit = {}) :
    BaseListAdapter<Time, ItemDoneBinding>(diffCallback = object : DiffUtil.ItemCallback<Time>() {
        override fun areItemsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Time, newItem: Time): Boolean {
            return oldItem.percentage == newItem.percentage
        }
    }) {

    override fun createBinding(parent: ViewGroup) = ItemDoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemDoneBinding, item: Time) {
        binding.root.setOnClickListener {
            submitList(currentList.toMutableList().apply {
                remove(item)
                if (this.size == 0) dismissDialog()
            })
        }
        binding.string = item.id.toString()
    }
}
