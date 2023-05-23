package com.sobarna.trolleyapp.ui.login

import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.sobarna.trolleyapp.util.Utils.setViewAnimation
import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.databinding.ActivityMainBinding
import com.sobarna.trolleyapp.ui.mainmenu.MainMenuActivity
import com.sobarna.trolleyapp.util.Utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getAllData().observe(this) { result ->
            when (result) {
                is Resource.Error -> {
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                    showProgressBar(false)
                }
                is Resource.Loading -> showProgressBar(true)
                is Resource.Success -> {
                    showProgressBar(false)
                    if (result.data.isNullOrEmpty().not()) {
                        Intent(
                            this@MainActivity, MainMenuActivity::class.java
                        ).let(::startActivity)
                        finish()
                    } else playAnimation()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            hideKeyboard(this@MainActivity)
            if (checkAvailabilityLogin()) viewModel.postLogin(
                binding.etUsername.text.toString(), binding.etPassword.text.toString()
            ).observe(this) { result ->
                when (result) {
                    is Resource.Error -> {
                        Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                        showProgressBar(false)
                    }
                    is Resource.Loading -> showProgressBar(true)
                    is Resource.Success -> {
                        showProgressBar(true)
                        Intent(
                            this@MainActivity, MainMenuActivity::class.java
                        ).let(::startActivity)
                        finish()
                    }
                }
            }
        }
    }

    private fun checkAvailabilityLogin(): Boolean {
        with(binding) {
            return when {
                etUsername.text.isNullOrBlank() -> {
                    etUsername.error = "username is empty"
                    false
                }
                etPassword.text.isNullOrBlank() -> {
                    etPassword.error = "password is empty"
                    false
                }
                else -> true
            }
        }
    }

    private fun showProgressBar(state: Boolean) {
        with(binding) {
            state.let {
                progressBar.isVisible = it
                imageView.isVisible = !it
                textView.isVisible = !it
                textView1.isVisible = !it
                etUsername.isVisible = !it
                etPassword.isVisible = !it
                btnLogin.isVisible = !it
            }
        }
    }

    private fun playAnimation() {
        with(binding) {

            val imageView = setViewAnimation(imageView, 1f, 500)
            val textView = setViewAnimation(textView, 1f, 500)
            val textView1 = setViewAnimation(textView1, 1f, 500)
            val etUsername = setViewAnimation(etUsername, 1f, 500)
            val etPassword = setViewAnimation(etPassword, 1f, 500)
            val btnLogin = setViewAnimation(btnLogin, 1f, 500)

            AnimatorSet().apply {
                playSequentially(
                    imageView, textView, textView1, etUsername, etPassword, btnLogin
                )
                startDelay = 500
            }.start()
        }
    }
}