package com.example.makeupmate.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.makeupmate.LoginViewModel
import com.example.makeupmate.R
import com.example.makeupmate.TokenPreference
import com.example.makeupmate.ViewModelFactory
import com.example.makeupmate.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(TokenPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        firebaseAuth = FirebaseAuth.getInstance()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.tvRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_notempty)
                }
                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.pass__notempty)
                }
            }
            viewModel.loginAcc(this, email, password)
            viewModel.loginResponse.observe(this) {
                if (it == true) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
