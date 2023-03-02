package ru.kekulta.giphyapp.features.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ActivityMainBinding
import ru.kekulta.giphyapp.di.MainServiceLocator

import ru.kekulta.giphyapp.features.list.ui.GifListFragment
import ru.kekulta.giphyapp.shared.navigation.api.Command
import ru.kekulta.giphyapp.shared.navigation.api.Router

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.Tv.text = "new text act"

    }

    override fun onResume() {
        MainServiceLocator.getRouter()
            .attachNavigator(MainNavigator(supportFragmentManager, R.id.container))
        MainServiceLocator.getRouter().navigate(Command.CommandForwardTo("Initial", "list"))
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        MainServiceLocator.getRouter().detachNavigator()
    }

    override fun onBackPressed() {
        MainServiceLocator.getRouter().navigate(Command.CommandBack)
    }

    companion object {
        const val LOG_TAG = "MainActivity"
    }
}