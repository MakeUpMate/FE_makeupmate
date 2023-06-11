package com.example.makeupmate

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainViewModel(private val pref: TokenPreference) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    fun saveToken(token: String){
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun tokenRefresh() {
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.currentUser?.getIdToken(true)?.addOnSuccessListener {
            viewModelScope.launch {
                _token.value = it.token.toString()
            }
        }

    }
}