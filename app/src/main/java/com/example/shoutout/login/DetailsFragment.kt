package com.example.shoutout.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
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
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        setDropdownAdapter()


        checkIfEmpty(binding.loginUsernameEdit, binding.getStartedButton)


        binding.getStartedButton.setOnClickListener {
            if (binding.organizationDropdownText.text.isEmpty()) {
                binding.organizationDropdownText.error = getString(R.string.required)
                binding.organizationDropdownText.requestFocus()
                return@setOnClickListener
            }

            viewModel.saveUserToFirebaseDatabase(
                binding.loginUsernameEdit.text.toString(),
                setCategory(),
                ifClub()
            )
            findNavController().navigate(R.id.action_detailsFragment_to_postsFragment)
        }

        return binding.root
    }

    private fun setDropdownAdapter() {
        val items = arrayOf(
            getString(R.string.club),
            getString(R.string.dept),
            getString(R.string.RegAssoc),
            getString(R.string.genBody)
        )

        val dropDownAdapter = ArrayAdapter(
            requireContext(),
            R.layout.type_list_item, items
        )

        binding.organizationDropdownText.setAdapter(dropDownAdapter)
    }

    private fun setCategory() = viewModel.setCategory(binding.organizationDropdownText.text.toString())

    private fun ifClub() = viewModel.determineWhetherClub(setCategory())

    private fun checkIfEmpty(editText: EditText, button: Button) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                button.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}