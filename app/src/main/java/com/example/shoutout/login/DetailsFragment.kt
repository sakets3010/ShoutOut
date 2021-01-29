package com.example.shoutout.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel by viewModels<DetailsViewmodel>()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        setDropdownAdapter()

        binding.getStartedButton.setOnClickListener {
            if(binding.organizationDropdownText.text.isEmpty()){
                binding.organizationDropdownText.error = "required field"
                binding.organizationDropdownText.requestFocus()
                return@setOnClickListener
            }
            viewModel.saveUserToFirebaseDatabase(binding.loginUsernameEdit.text.toString(),setCategory(),ifClub())
            findNavController().navigate(R.id.action_detailsFragment_to_postsFragment)
        }

        return binding.root
    }

    private fun setDropdownAdapter() {
        val items = arrayOf("Club","Department","Regional Assoc","General Body")

        val dropDownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.type_list_item, items
        )

        binding.organizationDropdownText.setAdapter(dropDownAdapter)
    }

    private fun setCategory() = viewModel.setCategory(binding.organizationDropdownText.text.toString())
    private fun ifClub()=viewModel.determineWhetherClub(setCategory())

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}