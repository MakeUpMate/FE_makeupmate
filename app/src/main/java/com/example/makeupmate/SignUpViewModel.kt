package com.example.makeupmate

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.makeupmate.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel() : ViewModel() {

    private val _signUpResponse = MutableLiveData<Boolean?>()
    val signUpResponse: LiveData<Boolean?> = _signUpResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun signUpAcc(context: Context, email: String, password: String){
        var firebaseAuth = FirebaseAuth.getInstance()
        _isLoading.value = true
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _signUpResponse.value = true
                _isLoading.value = false
            } else {
                _isLoading.value = false
                Toast.makeText(context, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

}