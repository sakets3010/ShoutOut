package com.example.shoutout.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shoutout.R
import com.example.shoutout.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
            .requestIdToken("677003624-mfsfpc35lm4h9fpj0jp8u3sete266igu.apps.googleusercontent.com")
            .requestEmail()
            .build()
        _googleSignInClient = GoogleSignIn.getClient(requireContext(), _googleSignInOptions)

        binding.googleButton.setOnClickListener {
            val signInIntent: Intent = _googleSignInClient.signInIntent
            _signInLauncher.launch(signInIntent)
        }

        viewModel.isValidUser.observe(viewLifecycleOwner, { isValid ->
            if (isValid == true) {
                findNavController().navigate(R.id.action_loginFragment_to_detailsFragment)
                Snackbar.make(requireView(), getString(R.string.sign_in_success), Snackbar.LENGTH_LONG).show()
                viewModel.userSignedIn()
            } else if (isValid == false) {
                Snackbar.make(requireView(), getString(R.string.sign_in_unsucessful), Snackbar.LENGTH_LONG).show()
                _googleSignInClient.signOut()
            }

        })

        viewModel.isNonBitsAccount.observe(viewLifecycleOwner,{
            if(it == true){
                Snackbar.make(requireView(), getString(R.string.bits_account), Snackbar.LENGTH_LONG).show()
               _googleSignInClient.signOut()
                viewModel.stopSnackbar()
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