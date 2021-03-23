package rybalchenko.elijah.presentation.sections.movies

import android.os.Bundle
import android.view.View
import rybalchenko.elijah.presentation.base.BaseMoviesFragment
import rybalchenko.elijah.presentation.utils.di.FragmentInject
import rybalchenko.elijah.presentation.utils.viewmodel.viewModel


class MoviesFragment : BaseMoviesFragment<MoviesViewModel>(), FragmentInject {

    override val viewModel: MoviesViewModel by lazy { viewModel<MoviesViewModel>(viewModelFactory) }

    override fun setupAdapter() {
        super.setupAdapter()
        adapter.clickListener = { movie ->  viewModel.addToFavorite(movie) }
    }
}