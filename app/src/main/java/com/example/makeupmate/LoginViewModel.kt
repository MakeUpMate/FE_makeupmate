package com.example.makeupmate

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.makeupmate.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class LoginViewModel(private val pref: TokenPreference) : ViewModel()  {

    private val _loginResponse = MutableLiveData<Boolean?>()
    val loginResponse: LiveData<Boolean?> = _loginResponse

    fun loginAcc(context: Context, email: String, password: String){
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _loginResponse.value = true
                firebaseAuth.currentUser?.getIdToken(false)?.addOnSuccessListener {
                    viewModelScope.launch {
                        pref.saveToken(it.token.toString())
                    }
                }
            } else {
                _loginResponse.value = false
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}