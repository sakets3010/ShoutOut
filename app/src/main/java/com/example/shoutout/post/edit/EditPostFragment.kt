package com.example.shoutout.post.edit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentEditPostBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditPostFragment : Fragment() {

    private val args by navArgs<EditPostFragmentArgs>()
    private val viewModel by viewModels<EditPostViewModel>()
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)

        binding.contentUpdateButton.setOnClickListener {
            viewModel.updateContent(args.post.postId, binding.contentEdit.text.toString())
            viewModel.updateHistory(args.post.postId, binding.contentEdit.text.toString())
            findNavController().navigate(R.id.action_editPostFragment_to_postsFragment)
        }

        viewModel.content.observe(viewLifecycleOwner, { content ->
            binding.contentEdit.setText(content)
            binding.contentEdit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    binding.contentUpdateButton.isEnabled =
                        binding.contentEdit.text.toString() != content
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //nothing
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //nothing
                }
            })
        })


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}