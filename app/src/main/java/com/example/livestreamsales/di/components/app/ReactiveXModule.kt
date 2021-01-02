package com.example.livestreamsales.di.components.app

import com.example.livestreamsales.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Named

@Module
class ReactiveXModule {

    companion object{
        internal const val DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER = "MAIN_THREAD_SCHEDULER"
        internal const val DEPENDENCY_NAME_IO_SCHEDULER = "IO_SCHEDULER"
        internal const val DEPENDENCY_NAME_COMPUTATION_SCHEDULER = "COMPUTATION_SCHEDULER"
    }

    @ApplicationScope
    @Provides
    @Named(DEPENDENCY_NAME_MAIN_THREAD_SCHEDULER)
    internal fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @ApplicationScope
    @Provides
    @Named(DEPENDENCY_NAME_IO_SCHEDULER)
    internal fun provideIoScheduler(): Scheduler{
        return Schedulers.io()
    }

    @ApplicationScope
    @Provides
    @Named(DEPENDENCY_NAME_COMPUTATION_SCHEDULER)
    internal fun provideComputationScheduler(): Scheduler{
        return Schedulers.computation()
    }

}