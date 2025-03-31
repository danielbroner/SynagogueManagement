package com.synagoguemanagement.synagoguemanagement.ui.prayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R

class PrayerTimeFragment : Fragment(R.layout.fragment_prayer_time) {
    private lateinit var prayerTimeRecyclerView: RecyclerView
    private lateinit var prayerTimeAdapter: PrayerTimeAdapter
    private val prayerTimesList = mutableListOf<PrayerTime>()
    private var isSuperAdmin = false  // Set this based on your user authentication logic

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prayerTimeRecyclerView = view.findViewById(R.id.prayerTimeRecyclerView)

        // Add a header item for the title
        val headerPrayerTime = PrayerTime("Header", "זמני התפילות והשיעורים")

        // Initialize the RecyclerView
        prayerTimeAdapter = PrayerTimeAdapter(prayerTimesList, isSuperAdmin)
        prayerTimeRecyclerView.layoutManager = LinearLayoutManager(context)
        prayerTimeRecyclerView.adapter = prayerTimeAdapter

        // Example: Populate some prayer times (this should come from your data source)
        prayerTimesList.add(PrayerTime("Fajr", "Message for Fajr"))
        prayerTimesList.add(PrayerTime("Dhuhr", "Message for Dhuhr"))
        prayerTimesList.add(PrayerTime("Asr", "Message for Asr"))
        prayerTimesList.add(PrayerTime("Maghrib", "Message for Maghrib"))
        prayerTimesList.add(PrayerTime("Isha", "Message for Isha"))
    }
}

data class PrayerTime(val prayerName: String, val message: String)

class PrayerTimeAdapter(
    private val prayerTimes: List<PrayerTime>,
    private val isSuperAdmin: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Define constants for the header and item types
    private val HEADER_TYPE = 0
    private val ITEM_TYPE = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER_TYPE -> {
                // Inflate the header layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                // Inflate the prayer time item layout
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prayer_time_card, parent, false)
                PrayerTimeViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PrayerTimeViewHolder) {
            val prayerTime = prayerTimes[position - 1] // Offset by 1 to account for the header
            holder.bind(prayerTime)

            // Show or hide the "Edit" button based on whether the user is superadmin
            if (isSuperAdmin) {
                holder.editButton.visibility = View.VISIBLE
                holder.editButton.setOnClickListener {
                    // Handle the edit action (e.g., open a dialog to edit the message)
                    Toast.makeText(holder.itemView.context, "Editing ${prayerTime.prayerName} message", Toast.LENGTH_SHORT).show()
                }
            } else {
                holder.editButton.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return prayerTimes.size+1 // Include the header in the count
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) HEADER_TYPE else ITEM_TYPE
    }

    // ViewHolder for the header
    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.headerTitle)

        fun bind() {
            titleText.text = "זמני התפילות והשיעורים"
        }
    }

    // ViewHolder for the prayer time items
    class PrayerTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val prayerMessageText: TextView = itemView.findViewById(R.id.prayerMessageText)
        val editButton: Button = itemView.findViewById(R.id.editPrayerMessageButton)

        fun bind(prayerTime: PrayerTime) {
            prayerMessageText.text = prayerTime.message
        }
    }
}