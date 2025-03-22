package com.synagoguemanagement.synagoguemanagement.ui.book

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.data.Seat

class SeatAdapter(private val seats: List<Seat>) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {
    private val hiddenPositions: Set<Int> = setOf(2, 3, 4, 5, 6, 12, 13, 14, 18, 19, 25, 26)
    private val selectedSeats = mutableSetOf<Int>()
    private val reservedSeats = seats.map { seat -> seat.position }

    class SeatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iconImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.seat_icon, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        if (hiddenPositions.contains(position)) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0) // Hide it completely
            return
        }

        holder.icon.setImageResource(R.drawable.chair_24px)
        updateIconColor(holder, getColor(position))

        // Handle Click to toggle selection
        holder.icon.setOnClickListener {
            if (isReservedSeat(position)) {
                return@setOnClickListener
            }

            if (selectedSeats.contains(position)) {
                selectedSeats.remove(position) // Unselect
            } else {
                selectedSeats.add(position)  // Select
            }
            notifyItemChanged(position) // Refresh UI for clicked item
        }
    }

    private fun isReservedSeat(position: Int): Boolean {
        return reservedSeats.contains(position)
    }

    private fun updateIconColor(holder: SeatViewHolder, color: Int) {
        holder.icon.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(holder.icon.context, color))
    }

    override fun getItemCount(): Int = 63

    private fun getColor(position: Int): Int {
        if (isReservedSeat(position)) {
            return R.color.red
        }

        if (selectedSeats.contains(position)) {
            return R.color.black
        }

        return R.color.button_bg
    }
}
