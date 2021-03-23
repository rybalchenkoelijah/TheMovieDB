package rybalchenko.elijah.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import rybalchenko.elijah.test.R

abstract class BaseFragment<ViewModel : BaseViewModel, Binding : ViewDataBinding> : Fragment() {
    val disposable = CompositeDisposable()

    lateinit var binding: Binding

    abstract val viewModel: ViewModel

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    protected fun showSnackbar(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ResourcesCompat.getColor(resources, R.color.black, null))
            .setTextColor(ResourcesCompat.getColor(resources, R.color.white, null))
            .show()
    }
}