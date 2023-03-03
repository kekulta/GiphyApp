package ru.kekulta.giphyapp.di



import ru.kekulta.giphyapp.features.search.list.data.NetworkClient
import ru.kekulta.giphyapp.features.search.list.data.network.RetrofitNetworkClient
import ru.kekulta.giphyapp.features.search.list.domain.api.GifRepository
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
            gifRepository = ru.kekulta.giphyapp.features.search.list.data.GifRepositoryImpl(
                provideNetworkClient()
            )
        }
        return gifRepository!!
    }

    private fun provideNetworkClient(): NetworkClient {
        if (networkClient == null) {
            networkClient = RetrofitNetworkClient()
        }
        return networkClient!!
    }
}