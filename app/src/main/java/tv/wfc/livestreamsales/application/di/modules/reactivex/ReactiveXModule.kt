package tv.wfc.livestreamsales.application.di.modules.reactivex

import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.ComputationScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.IoScheduler
import tv.wfc.livestreamsales.application.di.modules.reactivex.qualifiers.MainThreadScheduler
import tv.wfc.livestreamsales.application.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

@Module
class ReactiveXModule {
    @ApplicationScope
    @Provides
    @MainThreadScheduler
    internal fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @ApplicationScope
    @Provides
    @IoScheduler
    internal fun provideIoScheduler(): Scheduler{
        return Schedulers.io()
    }

    @ApplicationScope
    @Provides
    @ComputationScheduler
    internal fun provideComputationScheduler(): Scheduler{
        return Schedulers.computation()
    }
}