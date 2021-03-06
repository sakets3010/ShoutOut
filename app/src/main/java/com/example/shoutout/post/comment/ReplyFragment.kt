package com.example.shoutout.post.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.shoutout.adapters.ReplyAdapter
import com.example.shoutout.databinding.FragmentReplyBinding
import com.example.shoutout.helper.getDateTime
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReplyFragment : Fragment() {


    private val args by navArgs<ReplyFragmentArgs>()
    private val viewModel by viewModels<ReplyViewModel>()
    private var _binding: FragmentReplyBinding? = null
    private val binding get() = _binding!!

    private val replyAdapter = ReplyAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReplyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.replyRecycler.adapter = replyAdapter

        binding.addCommentButton.setOnClickListener {
            if (binding.commentEdit.text?.isNotEmpty() == true) {
                viewModel.saveReplyToFirebaseDatabase(
                    binding.commentEdit.text.toString(),
                    args.comment.commentId
                )
                binding.commentEdit.text?.clear()
            }
        }

        with(binding) {
            author.text = args.comment.author
            comment.text = args.comment.comment
            commentDate.text = getDateTime(args.comment.timeStamp)
        }

        viewModel.replies.observe(viewLifecycleOwner, { replies ->
            replyAdapter.data = replies
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}