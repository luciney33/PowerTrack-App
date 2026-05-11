package com.example.powertrack_app.data.remote.di

import com.example.powertrack_app.BuildConfig
import com.example.powertrack_app.common.Constantes
import com.example.powertrack_app.data.remote.api.DragonBallApiService
import com.example.powertrack_app.data.remote.api.ExerciseDbApiService
import com.example.powertrack_app.data.remote.api.GymApiService
import com.example.powertrack_app.data.remote.api.OpenFoodFactsApiService
import com.example.powertrack_app.data.remote.api.SecretosApiService
import com.example.powertrack_app.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named(Constantes.RETROFIT_GYMAPI)
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGymApiService(@Named(Constantes.RETROFIT_GYMAPI) retrofit: Retrofit): GymApiService {
        return retrofit.create(GymApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(Constantes.RETROFIT_SECRETOSAPI)
    fun provideSecretosRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSecretosApiService(@Named(Constantes.RETROFIT_SECRETOSAPI) retrofit: Retrofit): SecretosApiService {
        return retrofit.create(SecretosApiService::class.java)
    }


    @Provides
    @Singleton
    @Named(Constantes.RETROFIT_DBAPI)
    fun provideDragonBallRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_DRAGONBALL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDragonBallApiService(@Named(Constantes.RETROFIT_DBAPI) retrofit: Retrofit): DragonBallApiService {
        return retrofit.create(DragonBallApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(Constantes.PLAIN_OKHTTPCLIENT)
    fun providePlainOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named(Constantes.RETROFIT_EXERCISEDB)
    fun provideExerciseDbRetrofit(
        @Named(Constantes.PLAIN_OKHTTPCLIENT) okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constantes.URL_EXERCISEDB)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseDbApiService(
        @Named(Constantes.RETROFIT_EXERCISEDB) retrofit: Retrofit
    ): ExerciseDbApiService {
        return retrofit.create(ExerciseDbApiService::class.java)
    }

    @Provides
    @Singleton
    @Named(Constantes.RETROFIT_OPENFOODFACTS)
    fun provideOpenFoodFactsRetrofit(
        @Named(Constantes.PLAIN_OKHTTPCLIENT) okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constantes.URL_OPENFOODFACTS)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenFoodFactsApiService(
        @Named(Constantes.RETROFIT_OPENFOODFACTS) retrofit: Retrofit
    ): OpenFoodFactsApiService {
        return retrofit.create(OpenFoodFactsApiService::class.java)
    }

}