package rybalchenko.elijah.presentation

import android.app.Application
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import rybalchenko.elijah.domain.repository.MovieRepository
import rybalchenko.elijah.presentation.di.component.DaggerAppComponent
import rybalchenko.elijah.presentation.utils.di.AppInjector
import javax.inject.Inject

class TestApp : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repository: MovieRepository


    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this){
            DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
        }
    }

    override fun androidInjector() = dispatchingAndroidInjector

}