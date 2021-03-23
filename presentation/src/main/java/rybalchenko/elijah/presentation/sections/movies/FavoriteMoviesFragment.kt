package rybalchenko.elijah.presentation.sections.movies


import rybalchenko.elijah.presentation.base.BaseMoviesFragment
import rybalchenko.elijah.presentation.utils.di.FragmentInject
import rybalchenko.elijah.presentation.utils.viewmodel.viewModel

class FavoriteMoviesFragment : BaseMoviesFragment<FavoriteMoviesViewModel>(), FragmentInject {

    override val viewModel: FavoriteMoviesViewModel by lazy {
        viewModel<FavoriteMoviesViewModel>(
            viewModelFactory
        )
    }

    override fun setupAdapter() {
        super.setupAdapter()
        adapter.isFavorite = true
        adapter.clickListener = { movie ->
            viewModel.removeFromFavorite(movie)
        }
    }
}