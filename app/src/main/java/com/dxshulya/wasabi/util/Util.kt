package com.dxshulya.wasabi.util

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class Util {
    companion object {
        fun toObservable(editText: TextInputEditText): Observable<String> {
            val observable = Observable.create<String> { emitter ->
                val textWatcher = object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        s?.toString()?.let { emitter.onNext(it) }
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                }
                editText.addTextChangedListener(textWatcher)
                emitter.setCancellable {
                    editText.removeTextChangedListener(textWatcher)
                }
            }

            return observable.debounce(50, TimeUnit.MILLISECONDS)
        }
    }
}