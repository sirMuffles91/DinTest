package com.sethvanstaden.dintest.di

import com.sethvanstaden.dintest.core.audio.AudioMapper
import com.sethvanstaden.dintest.core.audio.DinAudioPlayer
import com.sethvanstaden.dintest.core.audio.DinAudioPlayerImpl
import com.sethvanstaden.dintest.data.local.AppDatabase
import com.sethvanstaden.dintest.data.local.mapper.TestResultMapper
import com.sethvanstaden.dintest.data.local.repository.TestResultRepository
import com.sethvanstaden.dintest.data.local.repository.TestResultRepositoryImpl
import com.sethvanstaden.dintest.domain.session.DinTestSession
import com.sethvanstaden.dintest.domain.triplet.TripletGenerator
import com.sethvanstaden.dintest.domain.usecases.results.TestResultUseCase
import com.sethvanstaden.dintest.ui.features.results.ResultsViewModel
import com.sethvanstaden.dintest.ui.features.results.TestResultUiMapper
import com.sethvanstaden.dintest.ui.features.test.DinTestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { AudioMapper() }
    single { TestResultMapper(get()) }
    single { TestResultUiMapper() }
    factory<DinAudioPlayer> { DinAudioPlayerImpl(
        appContext = get()
    ) }
    factory { TripletGenerator() }
    factory { DinTestSession(
        tripletGenerator = get()
    ) }

    single {
        androidx.room.Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "din_test.db"
        ).fallbackToDestructiveMigration().build()
    }
    single { get<AppDatabase>().testResultDao() }
    single<TestResultRepository> { TestResultRepositoryImpl(get()) }
    single { TestResultUseCase(get(), get()) }

    viewModel {
        DinTestViewModel(
            session = get(),
            audioMapper = get(),
            dinAudioPlayer = get(),
            finalizeDinTestUseCase = get(),
        )
    }

    viewModel{
        ResultsViewModel(get())
    }

}