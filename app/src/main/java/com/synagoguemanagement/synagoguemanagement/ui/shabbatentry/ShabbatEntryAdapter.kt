package com.synagoguemanagement.synagoguemanagement.ui.shabbatentry

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R

class ShabbatEntryAdapter(private val items: List<String>) : RecyclerView.Adapter<ShabbatEntryAdapter.ItemViewHolder>() {

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
        fun bind(item: String) {
            title.text = item
            start.text = "Entry: Friday, 5:34 PM"
            exit.text = "Exit: Saturday, 6:48 PM"
        }
    }
}
