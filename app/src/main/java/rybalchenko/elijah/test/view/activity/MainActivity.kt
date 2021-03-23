package rybalchenko.elijah.test.view.activity

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import rybalchenko.elijah.test.R
import javax.inject.Inject

class MainActivity : AppCompatActivity()  {
//    @Inject
//    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

//    @Inject
//    lateinit var res: Resources

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
//        dispatchingAndroidInjector
    }

//    override fun androidInjector() = dispatchingAndroidInjector
}