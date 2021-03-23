package rybalchenko.elijah.presentation.utils.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

interface AppSchedulers {

    fun io(): Scheduler

    fun mainThread(): Scheduler

    fun computation(): Scheduler

    fun newThread(): Scheduler

    fun trampoline(): Scheduler
}

class DefaultAppSchedulers :
    AppSchedulers {
    override fun io(): Scheduler = Schedulers.io()

    override fun mainThread(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun trampoline(): Scheduler = Schedulers.trampoline()
}