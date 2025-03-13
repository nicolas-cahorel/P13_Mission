package com.openclassrooms.hexagonal.games.di

import android.content.Context
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
 * This class acts as a Dagger Hilt module, responsible for providing dependencies to other parts of the application.
 * It's installed in the SingletonComponent, ensuring that dependencies provided by this module are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Provides the InternetUtils instance.
     * This utility is responsible for handling internet connectivity checks and related functions.
     *
     * @param context The application context, which is passed as a parameter.
     * @return An instance of InternetUtils.
     */
    @Provides
    @Singleton
    fun provideInternetUtils(@ApplicationContext context: Context): InternetUtils {
        return InternetUtils(context)
    }

    /**
     * Provides the PostApi instance.
     * This API handles the interaction with Firebase for managing posts.
     *
     * @return An instance of PostApi, which in this case is provided by FirebasePostService.
     */
    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return FirebasePostService()
    }

    /**
     * Provides the UserApi instance.
     * This API handles the interaction with Firebase for managing user data.
     *
     * @return An instance of UserApi, which in this case is provided by FirebaseUserService.
     */
    @Provides
    @Singleton
    fun provideUserApi(): UserApi {
        return FirebaseUserService()
    }

}