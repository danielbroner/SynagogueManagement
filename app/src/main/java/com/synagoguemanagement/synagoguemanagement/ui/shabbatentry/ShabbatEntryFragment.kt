package com.synagoguemanagement.synagoguemanagement.ui.shabbatentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.synagoguemanagement.synagoguemanagement.databinding.FragmentShabbatEntryBinding
import com.synagoguemanagement.synagoguemanagement.viewmodel.ShabbatViewModel

class ShabbatEntryFragment : Fragment() {
    private var _binding: FragmentShabbatEntryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShabbatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShabbatEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ShabbatEntryAdapter(emptyList())
        binding.shabbatEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.shabbatEntries.adapter = adapter

        // Fetch Shabbat times for Jerusalem (change geonameid if needed)
        viewModel.fetchShabbatTimes(281184)

        viewModel.shabbatTimes.observe(viewLifecycleOwner, Observer { shabbatList ->
            adapter.updateData(shabbatList)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
