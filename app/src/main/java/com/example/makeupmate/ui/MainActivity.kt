package com.example.makeupmate.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.makeupmate.LoginViewModel
import com.example.makeupmate.MainViewModel
import com.example.makeupmate.TokenPreference
import com.example.makeupmate.ViewModelFactory
import com.example.makeupmate.ui.LoginActivity
import com.example.makeupmate.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(TokenPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        binding.btnAnalyze.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.tokenRefresh()
        viewModel.token.observe(this){
            viewModel.saveToken(it.toString())
        }
    }
}