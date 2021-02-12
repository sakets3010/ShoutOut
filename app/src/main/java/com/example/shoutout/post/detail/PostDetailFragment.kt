package com.example.shoutout.post.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.shoutout.R
import com.example.shoutout.adapters.CommentsAdapter
import com.example.shoutout.databinding.FragmentPostDetailBinding
import com.example.shoutout.helper.Type
import com.example.shoutout.helper.getDateTime
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailFragment : Fragment() {

    private val args by navArgs<PostDetailFragmentArgs>()

    private val viewModel by viewModels<PostDetailViewModel>()
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    private val commentsAdapter = CommentsAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            titleText.text = args.post.titleText
            matterText.text = args.post.contentText
            postAuthor.text = args.post.ownerName
            authorCategory.text = args.post.ownerType
            postDateText.text = getDateTime(args.post.timeStamp)
            if (!args.post.imageUrI.isNullOrEmpty()) {
                Picasso.get().load(args.post.imageUrI).into(featuredImage)
            }
        }

        binding.commentsRecycler.adapter = commentsAdapter

        binding.addCommentButton.setOnClickListener {
            if (binding.commentEdit.text?.isNotEmpty() == true) {
                viewModel.saveCommentToFirebaseDatabase(
                    binding.commentEdit.text.toString(),
                    args.post.postId
                )
                binding.commentEdit.text?.clear()
            }
        }

        binding.reactButton.setOnClickListener {
            if (binding.emojiLayout.visibility == View.VISIBLE) {
                binding.emojiLayout.visibility = View.GONE
            } else if (binding.emojiLayout.visibility == View.GONE) {
                binding.emojiLayout.visibility = View.VISIBLE
            }
        }

        with(binding) {
            emojiTextView1.setOnClickListener { getEmoji(Type.LIKE) }
            emojiTextView2.setOnClickListener { getEmoji(Type.HEART) }
            emojiTextView3.setOnClickListener { getEmoji(Type.WOW) }
            emojiTextView4.setOnClickListener { getEmoji(Type.LAUGH) }
            emojiTextView5.setOnClickListener { getEmoji(Type.SAD) }
        }

        binding.menuButton.setOnClickListener { v: View ->
            showMenu(v, R.menu.post_control_menu)
        }

        commentsAdapter.replyListener = { comment ->
            val action =
                PostDetailFragmentDirections.actionPostDetailFragmentToReplyFragment(comment)
            findNavController().navigate(action)
        }

        viewModel.react.observe(viewLifecycleOwner, { type ->
            binding.reactedEmoji.text = viewModel.getEmojiType(requireContext(),type)
        })

        viewModel.owner.observe(viewLifecycleOwner, { isOwner ->
            if (isOwner) {
                binding.menuButton.visibility = View.VISIBLE
            } else {
                binding.menuButton.visibility = View.GONE
            }
        })

        viewModel.comments.observe(viewLifecycleOwner, { comments ->
            commentsAdapter.data = comments
        })

        viewModel.commentCount.observe(viewLifecycleOwner, { count ->
            val comments = "$count comments"
            binding.commentText.text = comments

        })

        binding.editHistoryText.setOnClickListener {
            val action =
                PostDetailFragmentDirections.actionPostDetailFragmentToEditHistoryFragment(args.post)
            findNavController().navigate(action)
        }
    }

    private fun getEmoji(type: Long) = viewModel.addEmoji(args.post.postId, type)

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.editPost -> {
                    val action =
                        PostDetailFragmentDirections.actionPostDetailFragmentToEditPostFragment(args.post)
                    findNavController().navigate(action)
                }
                R.id.deletePost -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.action))
                        .setMessage(getString(R.string.deletePost))
                        .setNeutralButton(getString(R.string.cancel)) { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton(getString(R.string.delete)) { _, _ ->
                            viewModel.deletePost(args.post.postId)
                            findNavController().navigate(R.id.action_postDetailFragment_to_postsFragment2)
                        }
                        .show()
                }
            }

            true
        }
        popup.setOnDismissListener {
            // Respond to popup being dismissed.
        }

        popup.show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}