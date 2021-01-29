package com.example.shoutout.login

import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.shoutout.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

class LoginViewmodel : ViewModel() {

    private var _isValidUser: MutableLiveData<Boolean?> = MutableLiveData()
    val isValidUser: LiveData<Boolean?>
        get() = _isValidUser

    private var _isNonBitsAccount: MutableLiveData<Boolean?> = MutableLiveData()
    val isNonBitsAccount: LiveData<Boolean?>
        get() = _isNonBitsAccount

    private val _firebaseAuth = Firebase.auth

    fun getAccount(data: Intent) {
        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                if (account.email?.toLowerCase(Locale.ROOT)?.endsWith("@hyderabad.bits-pilani.ac.in") == true){
                    firebaseAuthWithGoogle(account)
                    _isNonBitsAccount.value = false
                }
                else{
                    _isNonBitsAccount.value = true
                }
            }
        } catch (e: ApiException) {
            throw Exception (e.toString())
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        _firebaseAuth.signInWithCredential(credential).addOnCompleteListener {

            _isValidUser.value = it.isSuccessful
        }
    }

    fun userSignedIn() {
        _isValidUser.value = null
    }

    fun stopSnackbar() {
        _isNonBitsAccount.value = false
    }
}