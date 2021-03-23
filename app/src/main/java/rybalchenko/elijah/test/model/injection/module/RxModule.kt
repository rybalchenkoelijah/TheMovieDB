package rybalchenko.elijah.test.model.injection.module

import dagger.Module
import dagger.Provides
import rybalchenko.elijah.test.model.utils.rx.AppSchedulers
import javax.inject.Singleton

@Module
class RxModule {

    @Provides
    @Singleton
    fun provideSchedulers() = AppSchedulers()
}