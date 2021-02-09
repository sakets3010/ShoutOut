package com.example.shoutout.post.add

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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


        checkIfEmpty(binding.matterEdit, binding.addPost)

        binding.addPost.setOnClickListener {
            viewModel.savePostToFirebaseDatabase(
                binding.titleEdit.text.toString(),
                _selectedPhotoUri,
                binding.matterEdit.text.toString()
            )
            findNavController().navigate(R.id.action_addPostFragment_to_postsFragment)
        }


        return binding.root
    }

    private val _pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
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

    private fun checkIfEmpty(editText: EditText, button: Button) {

        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                button.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
                Log.d("viewsxd", "${button.isEnabled}")
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


}