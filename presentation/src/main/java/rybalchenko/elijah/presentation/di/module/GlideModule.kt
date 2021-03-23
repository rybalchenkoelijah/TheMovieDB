package rybalchenko.elijah.presentation.di.module

import dagger.Module
import dagger.Provides
import rybalchenko.elijah.presentation.di.scopes.ActivityScope
import rybalchenko.elijah.presentation.utils.glide.ImageLoader
import javax.inject.Named

@Module
object GlideModule {

    @Provides
    @ActivityScope
    fun provideImageLoader(@Named("photoUrl") photoUrl: String): ImageLoader = ImageLoader(photoUrl)

}
