package com.synagoguemanagement.synagoguemanagement.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.synagoguemanagement.synagoguemanagement.R
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentBookSeatsBinding
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentSeatsMapBinding

class SeatMapFragment : Fragment() {
//    private var _binding: FragmentSeatsMapBinding? = null
//    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seats_map, container, false)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
//        toolbar.setNavigationOnClickListener {
//            findNavController().navigateUp() // Go back to previous screen
//        }
//    }
}