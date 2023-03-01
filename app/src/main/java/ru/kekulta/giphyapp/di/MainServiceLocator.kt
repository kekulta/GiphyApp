package ru.kekulta.giphyapp.di

import ru.kekulta.giphyapp.features.list.data.GifRepositoryImpl
import ru.kekulta.giphyapp.features.list.data.NetworkClient
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.list.data.network.RetrofitGiphyNetworkClient

object MainServiceLocator {
    private var networkClient: NetworkClient? = null
    private var gifRepository: GifRepository? = null


    fun provideGifRepository(): GifRepository {
        if (gifRepository == null) {
            gifRepository = GifRepositoryImpl(provideNetworkClient())
        }
        return gifRepository!!
    }

    fun provideNetworkClient(): NetworkClient {
        if (networkClient == null) {
            networkClient = RetrofitGiphyNetworkClient()
        }
        return networkClient!!
    }
}