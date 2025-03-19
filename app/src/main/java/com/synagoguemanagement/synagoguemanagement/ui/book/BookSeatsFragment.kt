package com.synagoguemanagement.synagoguemanagement.ui.book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sample list of items
        val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5", "Item 5")

        // Set RecyclerView properties
        binding.bookSeatsEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.bookSeatsEntries.adapter = BookAdapter(items)

//        binding.bookSeatsButton.setOnClickListener {
//            Log.d("NavigationDebug", "Current destination bla: ${findNavController().currentDestination?.displayName}")
//            findNavController().navigate(R.id.action_book_seats_button_to_seats_map_page)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}