package com.example.makeupmate.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.makeupmate.R
import com.example.makeupmate.SignUpViewModel
import com.example.makeupmate.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            when {
                email.isEmpty() -> {
                    binding.emailEditTextLayout.error = getString(R.string.email_notempty)
                }

                password.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.pass__notempty)
                }

                confirmPassword.isEmpty() -> {
                    binding.passwordEditTextLayout.error = getString(R.string.conpass__notempty)
                }
            }

            if (password == confirmPassword) {
                viewModel.signUpAcc(this, email, password)
                viewModel.signUpResponse.observe(this) {
                    if (it == true) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}