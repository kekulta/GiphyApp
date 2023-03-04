package ru.kekulta.giphyapp

import android.app.Application
import ru.kekulta.giphyapp.di.MainServiceLocator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MainServiceLocator.initDi(this)
    }
}