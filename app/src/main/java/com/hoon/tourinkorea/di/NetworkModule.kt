package com.hoon.tourinkorea.di

import com.hoon.tourinkorea.BuildConfig
import com.hoon.tourinkorea.data.source.MapDataSource
import com.hoon.tourinkorea.data.source.MapRemoteDataSource
import com.hoon.tourinkorea.data.source.PostDataSource
import com.hoon.tourinkorea.data.source.PostRemoteDataSource
import com.hoon.tourinkorea.network.ApiCallAdapterFactory
import com.hoon.tourinkorea.network.ApiClient
import com.hoon.tourinkorea.network.KakaoApiClient
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FireBaseRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoRetrofit


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val header = Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("X-Api-Key", BuildConfig.API_KEY)
                .build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .addInterceptor(header)
            .build()
    }

    @FireBaseRetrofit
    @Singleton
    @Provides
    fun provideFireBaseRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.FIRE_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @KakaoRetrofit
    @Singleton
    @Provides
    fun provideKakaoRetrofit(client: OkHttpClient): Retrofit {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ApiCallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideClient(@FireBaseRetrofit retrofit: Retrofit): ApiClient {
        return retrofit.create(ApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideKakaoClient(@KakaoRetrofit retrofit: Retrofit): KakaoApiClient {
        return retrofit.create(KakaoApiClient::class.java)
    }

    @Singleton
    @Provides
    fun providePostDataSource(apiClient: ApiClient): PostDataSource {
        return PostRemoteDataSource(apiClient)
    }

    @Singleton
    @Provides
    fun provideMapDataSource(apiClient: KakaoApiClient): MapDataSource {
        return MapRemoteDataSource(apiClient)
    }
}