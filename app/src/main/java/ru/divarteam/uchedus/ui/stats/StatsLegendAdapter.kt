package ru.divarteam.uchedus.ui.stats

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.faskn.lib.Slice
import com.faskn.lib.legend.LegendAdapter
import com.faskn.lib.legend.LegendItemViewHolder
import ru.divarteam.uchedus.R

class StatsLegendAdapter : LegendAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomLegendItemViewHolder {
        return CustomLegendItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_legend, parent, false)
        )
    }

    // CREATE YOUR OWN ITEM VIEW HOLDER
    class CustomLegendItemViewHolder(view: View) : LegendItemViewHolder(view) {
        override fun bind(slice: Slice) {
            this.boundItem = slice
            itemView.findViewById<ImageView>(R.id.course_icon).imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(itemView.context, slice.color))

            itemView.findViewById<TextView>(R.id.course_tasks).setText("${slice.percentage}%")

            itemView.findViewById<TextView>(R.id.course_tasks_title).apply {
                text = slice.name
            }
        }
    }
}