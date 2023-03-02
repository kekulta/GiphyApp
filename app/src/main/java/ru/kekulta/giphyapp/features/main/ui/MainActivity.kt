package ru.kekulta.giphyapp.features.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kekulta.giphyapp.R
import ru.kekulta.giphyapp.databinding.ActivityMainBinding
import ru.kekulta.giphyapp.di.MainServiceLocator

import ru.kekulta.giphyapp.features.main.MainNavigator
import ru.kekulta.giphyapp.shared.navigation.api.Command

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    MainServiceLocator.getRouter().navigate(Command.CommandBack)
                }
            }
        )

    }

    override fun onResume() {
        MainServiceLocator.getRouter()
            .attachNavigator(MainNavigator(this, supportFragmentManager, R.id.container))
        MainServiceLocator.getRouter().navigate(Command.CommandForwardTo("Initial", "list"))
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        MainServiceLocator.getRouter().detachNavigator()
    }


    companion object {
        const val LOG_TAG = "MainActivity"
    }
}