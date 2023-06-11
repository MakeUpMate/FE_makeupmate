package com.example.makeupmate.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.makeupmate.LoginViewModel
import com.example.makeupmate.RekomViewModel
import com.example.makeupmate.TokenPreference
import com.example.makeupmate.ViewModelFactory
import com.example.makeupmate.data.Image64
import com.example.makeupmate.databinding.ActivityRekomBinding
import com.example.makeupmate.utils.imageFileToBase64
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class RekomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRekomBinding
    private lateinit var viewModel: RekomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRekomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(TokenPreference.getInstance(dataStore))
        )[RekomViewModel::class.java]

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File

        myFile?.let {
            binding.previewPhoto.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }

        if (myFile != null) {
            viewModel.getToken().observe(this){
                viewModel.postImage(this, it.toString(), Image64(imageFileToBase64(myFile)))
            }
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@RekomActivity, MainActivity::class.java))
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}