package com.example.makeupmate

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.makeupmate.databinding.ActivityPreviewBinding
import com.example.makeupmate.databinding.ActivityRekomBinding
import java.io.File

class RekomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRekomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRekomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File

        myFile?.let {
            binding.previewPhoto.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@RekomActivity, MainActivity::class.java))
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}