package com.example.shoutout.post

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentAddPostBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPostFragment : Fragment() {

    private val viewModel by viewModels<AddPostViewModel>()
    private var _selectedPhotoUri: Uri? = null
    private var _binding: FragmentAddPostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPostBinding.inflate(inflater, container, false)

        binding.addImageButton.setOnClickListener {
            _pickImages.launch("image/*")
        }

        binding.addPost.setOnClickListener {
            viewModel.savePostToFirebaseDatabase(binding.titleEdit.text.toString(),_selectedPhotoUri,binding.MatterEdit.text.toString())
            findNavController().navigate(R.id.action_addPostFragment_to_postsFragment)
        }

        return binding.root
    }

    private val _pickImages = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { it ->
                _selectedPhotoUri = it
                Picasso.get().load(it).into(binding.postImage)
                binding.addImageButton.isEnabled = false
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}