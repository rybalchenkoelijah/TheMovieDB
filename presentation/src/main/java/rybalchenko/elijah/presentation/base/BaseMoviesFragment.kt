package rybalchenko.elijah.presentation.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import rybalchenko.elijah.domain.entity.DataState
import rybalchenko.elijah.domain.entity.Movie
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
        observeOnDataState()
        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }

    protected open fun setupAdapter() {
        binding.moviesRV.adapter = adapter
    }

    protected open fun observeOnDataState() {
        viewModel.dataState
            .observeOn(schedulers.mainThread())
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

    protected open fun observeOnData() {
        viewModel.movieList.observe(
            viewLifecycleOwner,
            Observer<PagedList<Movie>> { list -> adapter.submitList(list) })
    }
}