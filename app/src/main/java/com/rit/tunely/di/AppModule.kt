package com.rit.tunely.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rit.tunely.data.remote.SpotifyAuthService
import com.rit.tunely.data.remote.SpotifyService
import com.rit.tunely.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpotifyService(): SpotifyService {
        return SpotifyService.create()
    }


    @Provides
    @Singleton
    fun provideAuthApi(): SpotifyAuthService =
        Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthService::class.java)
}