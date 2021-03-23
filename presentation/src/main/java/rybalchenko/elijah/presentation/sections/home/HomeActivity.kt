package rybalchenko.elijah.presentation.sections.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import rybalchenko.elijah.domain.repository.MovieRepository
import rybalchenko.elijah.test.R
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HasAndroidInjector  {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repository: MovieRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val a = 1
    }

    override fun onResume() {
        super.onResume()

    }

    override fun androidInjector() = dispatchingAndroidInjector
}