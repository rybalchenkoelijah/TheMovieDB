package rybalchenko.elijah.presentation.sections.movies

import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import rybalchenko.elijah.presentation.base.BaseFragment
import rybalchenko.elijah.presentation.utils.di.FragmentInject
import rybalchenko.elijah.presentation.utils.viewmodel.ViewModelFactory
import rybalchenko.elijah.presentation.utils.viewmodel.viewModel
import rybalchenko.elijah.test.R
import rybalchenko.elijah.test.databinding.MainFragmentBinding
import javax.inject.Inject

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(), FragmentInject {

    override fun getLayoutId() = R.layout.main_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: MoviePageAdapter


    override val viewModel: MainViewModel by lazy { viewModel<MainViewModel>(viewModelFactory) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titles = listOf(
            resources.getString(R.string.main_fragment_films),
            resources.getString(R.string.main_fragment_favorites)
        )
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = titles[position].capitalize()
        }.attach()
    }
}