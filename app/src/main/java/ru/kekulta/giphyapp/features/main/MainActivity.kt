package ru.kekulta.giphyapp.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ActivityMainBinding

import ru.kekulta.giphyapp.features.list.ui.GifListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.commit {
            replace(R.id.container, GifListFragment::class.java, null)
        }

        binding.Tv.text = "new text act"

    }

    companion object {
        const val LOG_TAG = "MainActivity"

        // TODO implement navigation
        var currentPosition = 0
    }
}