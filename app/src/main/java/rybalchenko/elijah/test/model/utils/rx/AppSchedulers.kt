package rybalchenko.elijah.test.model.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AppSchedulers {

    fun io(): Scheduler = Schedulers.io()

    fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

    fun computation(): Scheduler = Schedulers.computation()

    fun newThread(): Scheduler = Schedulers.newThread()

    fun trampoline(): Scheduler = Schedulers.trampoline()
}