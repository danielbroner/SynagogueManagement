package com.synagoguemanagement.synagoguemanagement.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.auth.AuthManager
import com.synagoguemanagement.synagoguemanagement.data.ReservedSeats
import com.synagoguemanagement.synagoguemanagement.data.Seat
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentBookSeatsBinding
import com.synagoguemanagement.synagoguemanagement.db.DbManager
import java.util.Calendar

class BookSeatsFragment : Fragment() {
    private var _binding: FragmentBookSeatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookSeatsBinding.inflate(inflater, container, false)

        val button: Button = binding.root.findViewById(R.id.book_seats_button)
        button.setOnClickListener {
            openSeatMapFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookSeatsEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.bookSeatsEntries.adapter = BookAdapter(mutableListOf())

        getUserReservedSeats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openSeatMapFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SeatMapFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun getUserReservedSeats() {
        val maxDaysToSearch = 30
        val result: MutableList<ReservedSeats> = mutableListOf()

        for (i in 0..maxDaysToSearch) {
            val date = getNextDay(i)

            DbManager.getInstance()!!.getReservedSeats(date) { document ->
                val seats = filterByUser(document)

                if (seats.isNotEmpty()) {
                    val element = ReservedSeats(date, seats)
                    result.add(element)
                    (binding.bookSeatsEntries.adapter as BookAdapter).updateData(mutableListOf(element))
                }
            }
        }
    }

    private fun getNextDay(i: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, i)

        return ReservedSeats.dateToString(calendar.time)
    }

    private fun filterByUser(document: DocumentSnapshot): List<Seat> {
        if (!document.exists()) {
            return emptyList()
        }

        val userId = AuthManager.getCurrentUser().uid
        val reserved = document.toObject(ReservedSeats::class.java)!!

        return reserved.seats.filter { seat -> seat.userId == userId }.toList()
    }
}