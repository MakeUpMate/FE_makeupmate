package com.example.makeupmate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.makeupmate.R
import com.example.makeupmate.databinding.ActivityDetailBinding
import com.example.makeupmate.databinding.ActivityMainBinding

private lateinit var binding: ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUrl = intent.getStringExtra("img_url");
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            Glide.with(this@DetailActivity)
                .load(imgUrl)
                .into(imgStyle)
        }
    }
}