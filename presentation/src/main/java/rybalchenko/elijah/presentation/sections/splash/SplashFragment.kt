package rybalchenko.elijah.presentation.sections.splash

import android.os.Handler
import android.os.Looper
import rybalchenko.elijah.presentation.base.BaseFragment
import rybalchenko.elijah.presentation.utils.di.FragmentInject
import rybalchenko.elijah.presentation.utils.viewmodel.ViewModelFactory
import rybalchenko.elijah.presentation.utils.viewmodel.viewModel
import rybalchenko.elijah.test.R
import rybalchenko.elijah.test.databinding.SplashFragmentBinding
import javax.inject.Inject

const val SPLASH_ANIMATION_DURATION = 1000L

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(), FragmentInject {
    override fun getLayoutId() = R.layout.splash_fragment

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override val viewModel: SplashViewModel by lazy { viewModel<SplashViewModel>(viewModelFactory) }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.onAppLaunch()
        }, SPLASH_ANIMATION_DURATION)
    }

}