package ru.kekulta.giphyapp.features.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ActivityMainBinding
import ru.kekulta.giphyapp.di.MainServiceLocator

import ru.kekulta.giphyapp.features.main.domain.MainNavigator
import ru.kekulta.giphyapp.features.main.domain.presentation.MainViewModel
import ru.kekulta.giphyapp.shared.navigation.api.Command

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()
    private val viewModel: MainViewModel by viewModels() { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            }
        )

        binding.likesButton.setOnClickListener {
            viewModel.onLikesClicked()
        }
        binding.searchButton.setOnClickListener {
            viewModel.onSearchClicked()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onResume(MainNavigator(this, supportFragmentManager, R.id.container))
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


    companion object {
        const val LOG_TAG = "MainActivity"
    }
}