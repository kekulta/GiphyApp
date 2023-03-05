package ru.kekulta.giphyapp.di


import android.annotation.SuppressLint
import android.content.Context
import ru.kekulta.giphyapp.features.likes.data.LikesRepositoryImpl
import ru.kekulta.giphyapp.features.search.data.NetworkClient
import ru.kekulta.giphyapp.features.likes.data.database.AppDatabase
import ru.kekulta.giphyapp.features.likes.domain.api.LikesRepository
import ru.kekulta.giphyapp.features.likes.domain.impl.GifInteractorLikedImpl
import ru.kekulta.giphyapp.features.search.data.network.RetrofitNetworkClient
import ru.kekulta.giphyapp.features.search.domain.api.GifInteractor
import ru.kekulta.giphyapp.features.search.domain.api.GifRepository
import ru.kekulta.giphyapp.features.search.domain.impl.GifInteractorSearchImpl
import ru.kekulta.giphyapp.features.pager.domain.api.PaginationInteractor
import ru.kekulta.giphyapp.features.pager.domain.impl.PaginationInteractorImpl
import ru.kekulta.giphyapp.shared.navigation.AppRouter
import ru.kekulta.giphyapp.shared.navigation.api.Router

@SuppressLint("StaticFieldLeak")
object MainServiceLocator {
    private var gifSearchInteractor: GifInteractor? = null
    private var gifLikedInteractor: GifInteractor? = null
    private var likesRepository: LikesRepository? = null
    private var context: Context? = null
    private var database: AppDatabase? = null
    private var networkClient: NetworkClient? = null
    private var gifRepository: GifRepository? = null
    private var searchInteractor: PaginationInteractor? = null
    private var likesInteractor: PaginationInteractor? = null
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

    private fun provideDatabase(): AppDatabase {
        context.let { context ->
            requireNotNull(context) { "DI should be initialized before accessing AppDatabase" }
            if (database == null) {
                AppDatabase.initDatabase(context)
                database = AppDatabase.getDatabase()
            }

            return database!!
        }
    }

    private fun provideGifRepository(): GifRepository {
        if (gifRepository == null) {
            gifRepository = ru.kekulta.giphyapp.features.search.data.GifRepositoryImpl(
                provideNetworkClient()
            )
        }
        return gifRepository!!
    }

    fun provideGifSearchInteractor(): GifInteractor {
        if (gifSearchInteractor == null) {
            gifSearchInteractor = GifInteractorSearchImpl(provideLikesRepository(), provideGifRepository())
        }
        return gifSearchInteractor!!
    }
    fun provideGifLikedInteractor(): GifInteractor {
        if (gifLikedInteractor == null) {
            gifLikedInteractor = GifInteractorLikedImpl(provideLikesRepository(), provideGifRepository())
        }
        return gifLikedInteractor!!
    }

    fun provideLikesRepository(): LikesRepository {
        if (likesRepository == null) {
            likesRepository = LikesRepositoryImpl(provideDatabase().getGifLikedDao())
        }
        return likesRepository!!
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
    fun provideLikesInteractor(): PaginationInteractor {
        if (likesInteractor == null) {
            likesInteractor = PaginationInteractorImpl()
        }
        return likesInteractor!!
    }
}