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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun loginAcc(context: Context, email: String, password: String){
        var firebaseAuth = FirebaseAuth.getInstance()
        _isLoading.value = true
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _loginResponse.value = true
                _isLoading.value = false
                firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
                    viewModelScope.launch {
                        pref.saveToken(it.token.toString())
                    }
                }
            } else {
                _isLoading.value = false
                _loginResponse.value = false
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}