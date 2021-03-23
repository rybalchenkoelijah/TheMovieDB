package rybalchenko.elijah.presentation.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import rybalchenko.elijah.presentation.di.scopes.ActivityScope
import rybalchenko.elijah.presentation.sections.home.HomeActivity

@Module
interface HomeActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class,
            MainNavigationModule::class,
            AdaptersModule::class,
            GlideModule::class,
            DiffUtilsModule::class,
            ViewModelModule::class]
    )
    fun createHomeActivityInjector(): HomeActivity

}