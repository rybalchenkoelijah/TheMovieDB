package rybalchenko.elijah.test.model.injection.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import rybalchenko.elijah.presentation.TestApp
import rybalchenko.elijah.test.model.injection.module.AppModule
import rybalchenko.elijah.test.model.injection.module.NetworkModule
import rybalchenko.elijah.test.model.injection.module.RxModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RxModule::class, AndroidInjectionModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(application: rybalchenko.elijah.presentation.TestApp)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}