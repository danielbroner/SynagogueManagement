package com.synagoguemanagement.synagoguemanagement.ui.shabbatentry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.data.ShabbatItem

class ShabbatEntryAdapter(private var items: List<ShabbatItem>) : RecyclerView.Adapter<ShabbatEntryAdapter.ItemViewHolder>() {

    // Update the data in the adapter
    fun updateData(newItems: List<ShabbatItem>) {
        // Make sure to assign the new list
        items = newItems
        notifyDataSetChanged()  // Refresh the RecyclerView with the new data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shabbat_entry_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.shabbat_entry_title)
        private val start: TextView = itemView.findViewById(R.id.shabbat_entry_start)
        private val exit: TextView = itemView.findViewById(R.id.shabbat_entry_exit)

        fun bind(item: ShabbatItem) {
            val dateTime = java.time.ZonedDateTime.parse(item.date)
            val dateFormatted = dateTime.toLocalDate().toString() // or format prettier if needed
            val timeFormatted = dateTime.toLocalTime().toString().substring(0, 5) // e.g., "19:05"

            if (item.category == "candles") {
                title.text = "Date: $dateFormatted"
                start.text = "Shabbat Entry: $timeFormatted"
            } else if (item.category == "havdalah") {
                exit.text = "Havdalah: $timeFormatted"
            }
        }

    }
}

