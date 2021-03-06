package com.example.shoutout.post.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.shoutout.R
import com.example.shoutout.adapters.PostPagingAdapter
import com.example.shoutout.databinding.FragmentFeedBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel by viewModels<FeedViewmodel>()

    private var _binding: FragmentFeedBinding? = null

    private val binding get() = _binding!!

    private val postAdapter = PostPagingAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postRecycler.adapter = postAdapter

        postAdapter.listener = { postItem ->
            viewModel.postOpened(postItem.postId)
            val action = FeedFragmentDirections.actionPostsFragmentToPostDetailFragment(postItem)
            findNavController().navigate(action)
        }

        postAdapter.viewListener = { postId ->
            viewModel.updateViews(postId)
        }

        Picasso.get().load(GoogleSignIn.getLastSignedInAccount(requireContext())?.photoUrl)
            .into(binding.profileImage)

        binding.profileImage.setOnLongClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.sign_out_title))
                .setMessage(resources.getString(R.string.sign_out_body))
                .setNeutralButton(resources.getString(R.string.sign_out_negative)) { _, _ ->
                    // Do nothing
                }
                .setPositiveButton(resources.getString(R.string.sign_out_positive)) { _, _ ->
                    viewModel.signUserOut()
                    findNavController().navigate(R.id.action_postsFragment_to_loginFragment)

                }
                .show()
            return@setOnLongClickListener true
        }

        setLoading()

        lifecycleScope.launch {
            viewModel.flow.collect {
                setNotLoading()
                postAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            postAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.progressBar.isVisible = loadStates.append is LoadState.Loading
            }
        }

        binding.postRecycler.adapter?.registerAdapterDataObserver(object :       //will observe and check for updates and if there are new posts,will scroll smoothly to pos 0
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) {
                    binding.postRecycler.smoothScrollToPosition(0)
                }
            }
        })



        viewModel.posts.observe(viewLifecycleOwner, {
            postAdapter.refresh()
        })


        viewModel.canAdd.observe(viewLifecycleOwner, { isClub ->
            if (isClub) {
                binding.addPostButton.visibility = View.VISIBLE
            } else {
                binding.addPostButton.visibility = View.GONE
            }
        })

        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_addPostFragment)
        }

    }


    private fun setLoading() {
        binding.loadingProgress.visibility = View.VISIBLE
        binding.postRecycler.visibility = View.GONE
    }

    private fun setNotLoading() {
        binding.loadingProgress.visibility = View.GONE
        binding.postRecycler.visibility = View.VISIBLE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}