package rybalchenko.elijah.presentation.sections.movies

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import javax.inject.Inject

class MoviePageAdapter @Inject constructor(moviesFragment: MainFragment) : FragmentStateAdapter(moviesFragment) {
    private val fragments = listOf(MoviesFragment(), FavoriteMoviesFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}