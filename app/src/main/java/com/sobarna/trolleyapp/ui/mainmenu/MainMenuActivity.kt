package com.sobarna.trolleyapp.ui.mainmenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.sobarna.trolleyapp.data.Resource
import com.sobarna.trolleyapp.databinding.ActivityMainMenuBinding
import com.sobarna.trolleyapp.domain.model.Store
import com.sobarna.trolleyapp.ui.login.MainActivity
import com.sobarna.trolleyapp.ui.maplist.ListStoreActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private val viewModel: MainMenuViewModel by viewModels()
    private var arrayStore: ArrayList<Store> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llVisitMart.setOnClickListener {
            Intent(this@MainMenuActivity, ListStoreActivity::class.java).let(::startActivity)
        }

        viewModel.getAllStore().observe(this) { result ->
            when (result) {
                is Resource.Error -> {
                    showProgressBar(false)
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> showProgressBar(true)
                is Resource.Success -> {
                    showProgressBar(false)
                    arrayStore.clear()
                    arrayStore.addAll(result.data ?: emptyList())
                    binding.btnLogOut.setOnClickListener {
                        viewModel.logOut(arrayStore)
                        Intent(this@MainMenuActivity, MainActivity::class.java).let(::startActivity)
                        finish()
                    }
                    initStat(result.data ?: emptyList())
                }
            }
        }
    }

    private fun showProgressBar(state: Boolean) {
        state.let {
            with(binding) {
                progressBar.isVisible = it
                llStat.isInvisible = it
            }
        }
    }

    private fun initStat(stores: List<Store>) {
        with(binding) {

            val arrayVisit: List<Store> = stores.filter { it.dateVisit.isNotBlank() }
            tvTotalStore.text = stores.size.toString()
            tvTotalVisit.text = arrayVisit.size.toString()
            tvTotalScore.text =
                if (arrayVisit.isEmpty() || stores.isEmpty())
                    "0"
                else
                    ((arrayVisit.size.toFloat() / stores.size.toFloat()) * 100).toInt().toString()

        }
    }
}