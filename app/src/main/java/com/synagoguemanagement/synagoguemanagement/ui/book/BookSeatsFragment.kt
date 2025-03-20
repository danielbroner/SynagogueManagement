package com.synagoguemanagement.synagoguemanagement.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentBookSeatsBinding

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

        // Sample list of items
        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5")

        // Set RecyclerView properties
        binding.bookSeatsEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.bookSeatsEntries.adapter = BookAdapter(items)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openSeatMapFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, SeatMapFragment()) // Ensure this ID matches your activity layout
            .addToBackStack(null) // Allows navigating back
            .commit()
    }
}