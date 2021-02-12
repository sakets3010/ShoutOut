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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.contentUpdateButton.setOnClickListener {
            viewModel.updateContent(args.post.postId, binding.contentEdit.text.toString())
            viewModel.updateHistory(args.post.postId, binding.contentEdit.text.toString())
            findNavController().navigate(R.id.action_editPostFragment_to_postsFragment)
        }

        viewModel.opened.observe(viewLifecycleOwner, { count ->
            val countText = "Opened $count time(s)"
            binding.openedCounter.text = countText
        })

        viewModel.viewed.observe(viewLifecycleOwner, { count ->
            val viewText = "$count account(s) viewed"
            binding.viewCounter.text = viewText
        })

        viewModel.reacted.observe(viewLifecycleOwner, { count ->
            val reactText = "$count account(s) reacted"
            binding.reactCounter.text = reactText
        })

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}