package rybalchenko.elijah.presentation.di.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import rybalchenko.elijah.presentation.TestApp
import rybalchenko.elijah.presentation.di.module.*
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class,
        RxModule::class,
        AndroidInjectionModule::class,
        NetworkModule::class,
        MovieDataModule::class,
        HomeActivityModule::class,
        MoviesUseCaseModule::class]
)
interface AppComponent {
    fun inject(application: TestApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}