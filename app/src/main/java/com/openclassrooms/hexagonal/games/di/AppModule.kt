package com.openclassrooms.hexagonal.games.di

import android.content.Context
import com.openclassrooms.hexagonal.games.data.service.CommentApi
import com.openclassrooms.hexagonal.games.data.service.FirebaseCommentService
import com.openclassrooms.hexagonal.games.data.service.FirebasePostService
import com.openclassrooms.hexagonal.games.data.service.FirebaseUserService
import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.data.service.UserApi
import com.openclassrooms.hexagonal.games.utils.InternetUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule is a Dagger Hilt module responsible for providing dependencies to the application's components.
 * It is installed in the SingletonComponent, ensuring that the provided dependencies are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Provides an instance of [InternetUtils].
     * This utility is used to check internet connectivity and perform related operations.
     *
     * @param context The application context, passed as a parameter.
     * @return A singleton instance of [InternetUtils].
     */
    @Provides
    @Singleton
    fun provideInternetUtils(@ApplicationContext context: Context): InternetUtils {
        return InternetUtils(context)
    }

    /**
     * Provides an instance of [PostApi].
     * This API is responsible for managing posts via Firebase interactions.
     *
     * @return A singleton instance of [PostApi], provided by [FirebasePostService].
     */
    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return FirebasePostService()
    }

    /**
     * Provides an instance of [UserApi].
     * This API is responsible for managing user data via Firebase interactions.
     *
     * @return A singleton instance of [UserApi], provided by [FirebaseUserService].
     */
    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return FirebaseUserService()
    }

    /**
     * Provides an instance of [CommentApi].
     * This API is responsible for managing comments via Firebase interactions.
     *
     * @return A singleton instance of [CommentApi], provided by [FirebaseCommentService].
     */
    @Provides
    @Singleton
    fun provideCommentApi(): CommentApi {
        return FirebaseCommentService()
    }

}