package com.example.shoutout.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.shoutout.R
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

        binding.postRecycler.apply {
            adapter = postAdapter
        }

        lifecycleScope.launch {
            viewModel.flow.collect {
                postAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            postAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
                binding.progressBar.isVisible = loadStates.append is LoadState.Loading
            }
        }



        viewModel.canAdd.observe(viewLifecycleOwner, { isClub ->
            Log.d("bool", "value:${isClub}")
            if (isClub) {
                binding.addPostButton.visibility = View.VISIBLE
            } else {
                binding.addPostButton.visibility = View.GONE
            }
        })

        binding.addPostButton.setOnClickListener {
            findNavController().navigate(R.id.action_postsFragment_to_addPostFragment)
        }


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}