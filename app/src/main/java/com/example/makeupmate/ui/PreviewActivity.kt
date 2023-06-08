package com.example.makeupmate.ui

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.makeupmate.databinding.ActivityPreviewBinding
import com.example.makeupmate.utils.rotateFile
import java.io.File

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("picture", File::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("picture")
        } as? File

        val isBackCamera = intent.getBooleanExtra("isBackCamera", true) as Boolean

        myFile?.let {
            rotateFile(it, isBackCamera)
            getFile = it
            binding.previewPhoto.setImageBitmap(BitmapFactory.decodeFile(it.path))
        }

        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@PreviewActivity, CameraActivity::class.java))
        }

        binding.delete.setOnClickListener {
            val intent =  Intent(this@PreviewActivity, CameraActivity::class.java)
            startActivity(intent)
        }

        binding.confirm.setOnClickListener {
            val intent = Intent(this@PreviewActivity, RekomActivity::class.java)
            intent.putExtra("picture", myFile)
            intent.putExtra(
                "isBackCamera",
                isBackCamera
            )
            setResult(RekomActivity.CAMERA_X_RESULT, intent)
            startActivity(intent)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
    }
}
