package ru.kekulta.giphyapp.di

import ru.kekulta.giphyapp.features.list.data.GifRepositoryImpl
import ru.kekulta.giphyapp.features.list.data.NetworkClient
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.list.data.network.RetrofitGiphyNetworkClient
import ru.kekulta.giphyapp.shared.navigation.AppRouter
import ru.kekulta.giphyapp.shared.navigation.api.Router

object MainServiceLocator {
    private var networkClient: NetworkClient? = null
    private var gifRepository: GifRepository? = null
    private var router: Router? = null


    // TODO move out of DI
    fun getRouter(): Router {
        if (router == null) {
            router = AppRouter()
        }
        return router!!
    }

    fun provideGifRepository(): GifRepository {
        if (gifRepository == null) {
            gifRepository = GifRepositoryImpl(provideNetworkClient())
        }
        return gifRepository!!
    }

    private fun provideNetworkClient(): NetworkClient {
        if (networkClient == null) {
            networkClient = RetrofitGiphyNetworkClient()
        }
        return networkClient!!
    }
}