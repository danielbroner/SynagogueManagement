package com.synagoguemanagement.synagoguemanagement.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.data.ReservedSeats
import com.synagoguemanagement.synagoguemanagement.db.DbManager
import java.util.Locale

class BookAdapter(private val items: MutableList<ReservedSeats>) : RecyclerView.Adapter<BookAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.booked_seats_card, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.findViewById(R.id.book_seats_time)
        private val amount: TextView = itemView.findViewById(R.id.book_seats_amount)
        private val cancelButton: TextView = itemView.findViewById(R.id.cancel_button)

        fun bind(item: ReservedSeats) {
            date.text = item.date
            val size = item.seats.size
            amount.text = String.format(Locale.getDefault(), "%d seat%s", size, isPlural(size))

            cancelButton.setOnClickListener { cancelReservation(item) }
        }

        private fun isPlural(size: Int) = if (size == 1) "" else "s"

        private fun cancelReservation(item: ReservedSeats) {
            DbManager.getInstance()!!.removeReservation(item.date)
            items.remove(item)
            notifyDataSetChanged()
        }
    }

    fun updateData(items: MutableList<ReservedSeats>) {
        val reservedSeats: List<ReservedSeats> = (this.items + items).sortedBy { item -> item.date }
        this.items.clear()
        this.items.addAll(reservedSeats)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
