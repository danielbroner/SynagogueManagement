package com.synagoguemanagement.synagoguemanagement.ui.book

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.data.ReservedSeats
import com.synagoguemanagement.synagoguemanagement.db.DbManager
import java.util.Calendar
import java.util.Locale

class SeatMapFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeatAdapter
    private lateinit var selectedDate: String
    private var reservedSeats = ReservedSeats(ReservedSeats.today(), listOf())
    private var allSeats = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_seats_map, container, false)

        backToReservedListView(view)
        datePickerView(view)
        seatsMapView(view)
        confirmSelectedSeatsView(view)
        cancelSelectedView(view)

        return view
    }

    private fun seatsMapView(view: View) {
        recyclerView = view.findViewById(R.id.iconRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 9) // 9 columns
        recyclerView.setHasFixedSize(true)

        adapter = SeatAdapter(allSeats)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(CustomItemDecoration(50))

        getReservedSeats(ReservedSeats.today())
    }

    private fun datePickerView(view: View) {
        val datePickerDropdown: TextView = view.findViewById(R.id.date_picker_dropdown)
        selectedDate = datePickerDropdown.text.toString()

        datePickerDropdown.setOnClickListener {
            showDatePickerDialog(datePickerDropdown)
        }

        datePickerDropdown.doAfterTextChanged { newText ->
            reservedSeats = ReservedSeats(newText.toString(), listOf())
            getReservedSeats(newText.toString())
        }
    }

    private fun backToReservedListView(view: View) {
        val backButton: Button = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            goBack()
        }
    }

    private fun confirmSelectedSeatsView(view: View) {
        val bookSeatsButton: Button = view.findViewById(R.id.confirm_selected_seats)

        bookSeatsButton.setOnClickListener {
            DbManager.getInstance()?.reserveSeats(reservedSeats.add(adapter.getSelectedItems()))
            goBack()
        }
    }

    private fun cancelSelectedView(view: View) {
        val cancelButton: Button = view.findViewById(R.id.cancel_selected_seats)
        cancelButton.setOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        parentFragmentManager.popBackStack()
    }

    private fun getReservedSeats(date: String) {
        DbManager.getInstance()?.getReservedSeats(date, { documentSnapshot: DocumentSnapshot ->
            if (documentSnapshot.exists()) {
                reservedSeats = documentSnapshot.toObject(ReservedSeats::class.java)!!
                allSeats = reservedSeats.seats.map { seat -> seat.position }.toList().toMutableList()
                adapter.updateList(allSeats)
            } else {
                println("Does not exist")
                adapter.updateList(listOf())
            }
        })
    }

    private fun showDatePickerDialog(textView: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", selectedDay, selectedMonth + 1, selectedYear)
            textView.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }
}