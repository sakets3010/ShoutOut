package com.example.shoutout.post

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentSplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

const val SPLASH_TIME_OUT: Long = 2000

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


        Handler(Looper.getMainLooper()).postDelayed({
            if (Firebase.auth.currentUser != null) {
                viewModel.detail.observe(viewLifecycleOwner, {
                    if (it != null && it.profileSet) {
                        findNavController().navigate(R.id.action_splashFragment_to_postsFragment)   //if registered,signed in both
                    } else if (it == null) {
                        findNavController().navigate(R.id.action_splashFragment_to_detailsFragment) //if signed in but not registered
                    }
                })
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)  //if neither
            }
        }, SPLASH_TIME_OUT)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}