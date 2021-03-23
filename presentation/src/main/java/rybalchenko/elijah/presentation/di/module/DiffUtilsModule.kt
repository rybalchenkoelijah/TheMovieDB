package rybalchenko.elijah.presentation.di.module

import androidx.recyclerview.widget.DiffUtil
import dagger.Module
import dagger.Provides
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.presentation.utils.data.MovieDiffCallback

@Module
class DiffUtilsModule {

    @Provides
    fun provideMovieDiffUtilCallback(): DiffUtil.ItemCallback<Movie> = MovieDiffCallback()
}