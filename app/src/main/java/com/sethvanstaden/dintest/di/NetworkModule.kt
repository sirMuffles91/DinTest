package com.sethvanstaden.dintest.di

import com.sethvanstaden.dintest.data.remote.DinUploaderImpl
import com.sethvanstaden.dintest.data.remote.api.DinApi
import com.sethvanstaden.dintest.domain.uploader.DinUploader
import com.sethvanstaden.dintest.domain.usecases.upload.FinalizeDinTestUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val networkModule = module {
    single {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("https://enoqczf2j2pbadx.m.pipedream.net/")
            .client(get())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .build()
    }

    single<DinApi> { get<Retrofit>().create(DinApi::class.java) }

    single<DinUploader> { DinUploaderImpl(api = get()) }
    single { FinalizeDinTestUseCase(get(), get(), get()) }
}