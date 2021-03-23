package rybalchenko.elijah.presentation.di.module

import dagger.Module
import dagger.Provides
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.DefaultAppSchedulers
import javax.inject.Singleton

@Module
class RxModule {

    @Provides
    @Singleton
    fun provideSchedulers(): AppSchedulers = DefaultAppSchedulers()
}