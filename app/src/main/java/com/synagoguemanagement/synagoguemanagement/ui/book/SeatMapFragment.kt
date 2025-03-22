package com.synagoguemanagement.synagoguemanagement.ui.book

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.data.Seat
import java.util.Calendar

class SeatMapFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seats_map, container, false)

        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack() // Go back to the previous fragment
        }

        val datePickerDropdown: TextView = view.findViewById(R.id.date_picker_dropdown)

        datePickerDropdown.setOnClickListener {
            showDatePickerDialog(datePickerDropdown)
        }

        recyclerView = view.findViewById(R.id.iconRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 9) // 9 columns
        recyclerView.setHasFixedSize(true)

        adapter = SeatAdapter(getReservedSeats())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecoration(50))
        return view
    }

    private fun getReservedSeats(): List<Seat> {
        return listOf(
            Seat(32, 4),
            Seat(33, 4),
            Seat(34, 4),
            Seat(35, 4),
            Seat(45, 4),
            Seat(46, 4),
            Seat(47, 4),
            Seat(59, 4)
        )
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            textView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }
}