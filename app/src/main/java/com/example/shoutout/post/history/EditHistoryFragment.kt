package com.example.shoutout.post.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.shoutout.adapters.EditHistoryAdapter
import com.example.shoutout.databinding.FragmentEditHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditHistoryFragment : Fragment() {

    private val args by navArgs<EditHistoryFragmentArgs>()
    private val viewModel by viewModels<EditHistoryViewModel>()
    private var _binding: FragmentEditHistoryBinding? = null
    private val binding get() = _binding!!

    private val editHistoryAdapter = EditHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditHistoryBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editHistoryRecycler.adapter = editHistoryAdapter

        viewModel.history.observe(viewLifecycleOwner, { history ->
            editHistoryAdapter.data = history
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}