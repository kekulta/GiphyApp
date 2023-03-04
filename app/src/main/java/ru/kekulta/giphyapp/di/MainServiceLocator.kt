package ru.kekulta.giphyapp.di


import android.annotation.SuppressLint
import android.content.Context
import ru.kekulta.giphyapp.features.list.data.NetworkClient
import ru.kekulta.giphyapp.features.list.data.database.AppDatabase
import ru.kekulta.giphyapp.features.list.data.network.RetrofitNetworkClient
import ru.kekulta.giphyapp.features.list.domain.api.GifRepository
import ru.kekulta.giphyapp.features.pager.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.pager.domain.impl.PaginationInteractorImpl
import ru.kekulta.giphyapp.shared.navigation.AppRouter
import ru.kekulta.giphyapp.shared.navigation.api.Router

@SuppressLint("StaticFieldLeak")
object MainServiceLocator {
    private var context: Context? = null
    private var database: AppDatabase? = null
    private var networkClient: NetworkClient? = null
    private var gifRepository: GifRepository? = null
    private var searchInteractor: PaginationInteractor? = null
    private var router: Router? = null


    fun initDi(context: Context) {
        this.context = context
    }

    fun provideRouter(): Router {
        if (router == null) {
            router = AppRouter()
        }
        return router!!
    }

    fun provideDatabase(): AppDatabase {
        context.let { context ->
            requireNotNull(context) { "DI should be initialized before accessing AppDatabase" }
            if (database == null) {
                AppDatabase.initDatabase(context)
                database = AppDatabase.getDatabase()
            }

            return database!!
        }
    }

    fun provideGifRepository(): GifRepository {
        if (gifRepository == null) {
            gifRepository = ru.kekulta.giphyapp.features.list.data.GifRepositoryImpl(
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

    fun provideSearchInteractor(): PaginationInteractor {
        if (searchInteractor == null) {
            searchInteractor = PaginationInteractorImpl()
        }
        return searchInteractor!!
    }
}