package rybalchenko.elijah.presentation.utils.rx

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

infix fun Disposable.with(composite: CompositeDisposable) {
    composite.add(this)
}
