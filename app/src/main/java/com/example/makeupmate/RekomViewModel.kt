package com.example.makeupmate

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.makeupmate.data.ApiConfig
import com.example.makeupmate.data.Image64
import com.example.makeupmate.data.PredictResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RekomViewModel(private val pref: TokenPreference) : ViewModel() {

    private val TAG = "Login"

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun postImage(context: Context, token: String, image: Image64) {
        val client = ApiConfig.getApiService().postImage(token, image)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    Log.e(TAG, "Success: ${response.body()}")
                } else {
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e(TAG, "Erorr: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}