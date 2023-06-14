package com.example.makeupmate.ui

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.makeupmate.RekomViewModel
import com.example.makeupmate.TokenPreference
import com.example.makeupmate.ViewModelFactory
import com.example.makeupmate.data.Image64
import com.example.makeupmate.data.PredictResponse
import com.example.makeupmate.databinding.ActivityRekomBinding
import com.example.makeupmate.utils.imageFileToBase64
import com.example.makeupmate.utils.reduceFileImage
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
            viewModel.getToken().observe(this) {
                viewModel.postImage(
                    this,
                    it.toString(),
                    Image64(imageFileToBase64(reduceFileImage(myFile)))
                )
            }
        }

        viewModel.rekomResponse.observe(this) {
            setRekom(it)
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@RekomActivity, MainActivity::class.java))
        }

    }

    private fun setRekom(response: PredictResponse?) {
        binding.apply {
            Glide.with(this@RekomActivity)
                .load(response?.skintone)
                .into(ivSkintone)

            ivCircle1.setColorFilter(Color.parseColor(response?.hex1), PorterDuff.Mode.SRC_IN)
            ivCircle2.setColorFilter(Color.parseColor(response?.hex2), PorterDuff.Mode.SRC_IN)
            ivCircle3.setColorFilter(Color.parseColor(response?.hex3), PorterDuff.Mode.SRC_IN)

            Glide.with(this@RekomActivity)
                .load(response?.bestMatch)
                .into(ivProduct1)
            Glide.with(this@RekomActivity)
                .load(response?.recom2)
                .into(ivProduct2)
            Glide.with(this@RekomActivity)
                .load(response?.recom3)
                .into(ivProduct3)

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}