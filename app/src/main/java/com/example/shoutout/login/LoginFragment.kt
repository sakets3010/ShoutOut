package com.example.shoutout.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {
    private val viewModel by viewModels<LoginViewmodel>()
    private lateinit var _googleSignInClient: GoogleSignInClient
    private lateinit var _googleSignInOptions: GoogleSignInOptions

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        _googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("359389111159-sm3g4rqsqn0mrmj5otqiqq5fpnd4mdt6.apps.googleusercontent.com")
            .requestEmail()
            .build()
        _googleSignInClient = GoogleSignIn.getClient(requireContext(), _googleSignInOptions)

        binding.googleButton.setOnClickListener {
            val signInIntent: Intent = _googleSignInClient.signInIntent
            _signInLauncher.launch(signInIntent)
        }

        viewModel.isValidUser.observe(viewLifecycleOwner, { isValid ->
            if (isValid == true) {
                Navigation.findNavController(
                    requireView()
                ).navigate(R.id.action_loginFragment_to_postsFragment)

                Snackbar.make(requireView(), "sign in successful", Snackbar.LENGTH_LONG).show()

                viewModel.userSignedIn()
            } else if (isValid == false) {

                Snackbar.make(requireView(), "sign in unsuccessful", Snackbar.LENGTH_LONG).show()
            }

        })

        return binding.root
    }

    private val _signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { it1 -> viewModel.getAccount(it1) }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}