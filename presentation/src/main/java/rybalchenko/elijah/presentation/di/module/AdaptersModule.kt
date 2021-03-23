package rybalchenko.elijah.presentation.di.module

import androidx.paging.PagedListAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import dagger.Binds
import dagger.Module
import rybalchenko.elijah.domain.entity.Movie
import rybalchenko.elijah.presentation.sections.movies.MovieCardAdapter
import rybalchenko.elijah.presentation.sections.movies.MoviePageAdapter

@Module
interface AdaptersModule {
    @Binds
    fun provideMoviesPageAdapter(adapter: MoviePageAdapter): FragmentStateAdapter

    @Binds
    fun provideMovieCardAdapter(adapter: MovieCardAdapter): PagedListAdapter<Movie, MovieCardAdapter.MovieCardViewHolder>
}