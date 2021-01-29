package com.example.shoutout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoutout.databinding.FragmentSplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SplashViewmodel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        if (Firebase.auth.currentUser != null) {
            viewModel.detail.observe(viewLifecycleOwner, {
                if (it.profileSet) {
                    Log.d("navigate", "1 called")
                    findNavController().navigate(R.id.action_splashFragment_to_postsFragment)
                } else {
                    Log.d("navigate", "2 called")
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            })
        }
        else{
            Log.d("navigate", "3 called")
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}