package com.example.shoutout.post

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentFeedBinding
import com.example.shoutout.databinding.FragmentLoginBinding
import com.example.shoutout.login.LoginViewmodel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class FeedFragment : Fragment() {

    private val viewModel by viewModels<FeedViewmodel>()
    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)


        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}