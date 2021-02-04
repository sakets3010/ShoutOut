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

        with(binding) {
            titleText.text = args.post.titleText
            matterText.text = args.post.contentText
            postAuthor.text = args.post.ownerName
            authorCategory.text = args.post.ownerType
            postDateText.text = viewModel.getDateTime(args.post.timeStamp)
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

        binding.menuButton.setOnClickListener { v: View ->
            showMenu(v, R.menu.post_control_menu)
        }

//        commentsAdapter.replyListener = { comment ->
//            val reply = "Replying to '${comment.comment}'~${comment.author}: "
//            MaterialAlertDialogBuilder(requireContext())
//                .setTitle("Action")
//                .setMessage("Reply to text?")
//                .setNeutralButton("Cancel") { _, _ ->
//                    // Do nothing
//                }
//                .setPositiveButton("Yes,Reply") { _, _ ->
//                    binding.edittextComments.prefixText = reply
//                }
//                .show()
//        }

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

        binding.editHistoryButton.setOnClickListener {
            val action =
                PostDetailFragmentDirections.actionPostDetailFragmentToEditHistoryFragment(args.post)
            findNavController().navigate(action)
        }

        return binding.root
    }

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
                        .setTitle("Action")
                        .setMessage("Delete Post?")
                        .setNeutralButton("Cancel") { _, _ ->
                            // Do nothing
                        }
                        .setPositiveButton("Delete") { _, _ ->
                            viewModel.deletePost(args.post.postId)
                            findNavController().navigate(R.id.action_postDetailFragment_to_postsFragment)
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