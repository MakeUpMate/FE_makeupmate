package com.example.makeupmate

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val TAG = "Rekom"

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _rekomResponse = MutableLiveData<PredictResponse>()
    val rekomResponse: LiveData<PredictResponse> = _rekomResponse

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun postImage(context: Context, token: String, image: Image64) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().postImage(token, image)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    _rekomResponse.value = response.body()
                    Log.d(TAG, "Success: ${response.body()}")
                } else {
                    _isLoading.value = false
                    Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_LONG)
                        .show()
                    Log.e(TAG, "Erorr: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                _isLoading.value = false
                Toast.makeText(context, t.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}