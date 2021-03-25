package rybalchenko.elijah.presentation.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.presentation.sections.movies.MovieCardAdapter
import rybalchenko.elijah.presentation.utils.di.FragmentInject
import rybalchenko.elijah.presentation.utils.rx.AppSchedulers
import rybalchenko.elijah.presentation.utils.rx.with
import rybalchenko.elijah.presentation.utils.viewmodel.ViewModelFactory
import rybalchenko.elijah.test.R
import rybalchenko.elijah.test.databinding.MoviesFragmentBinding
import javax.inject.Inject

abstract class BaseMoviesFragment<ViewModel : BaseMoviesViewModel> :
    BaseFragment<ViewModel, MoviesFragmentBinding>(), FragmentInject {
    override fun getLayoutId() = R.layout.movies_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MovieCardAdapter

    @Inject
    lateinit var schedulers: AppSchedulers


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeOnData()
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    protected open fun setupAdapter() {
        binding.moviesRV.adapter = adapter
        adapter.shareListener = { movie ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.movies_fragment_share, movie.title, movie.description, movie.id))
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    protected open fun observeOnData() {
        viewModel.movieDataSource.observeOn(schedulers.mainThread())
            .doOnNext { dataSource -> adapter.submitList(dataSource.movies) }
            .switchMap { movieDataSource -> movieDataSource.dataState }
            .subscribe { state ->
                when (state) {
                    DataState.EMPTY -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.emptyTV.visibility = View.VISIBLE
                    }
                    DataState.PROGRESS -> {
                        binding.swipeRefresh.isRefreshing = true
                        binding.emptyTV.visibility = View.INVISIBLE
                    }
                    DataState.SUCCESS -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.emptyTV.visibility = View.INVISIBLE
                    }
                    DataState.FAILED -> {
                        showSnackbar(R.string.movies_fragment_something_went_wrong)
                        binding.swipeRefresh.isRefreshing = false
                        binding.emptyTV.visibility = View.INVISIBLE
                    }
                }
            } with disposable
    }
}