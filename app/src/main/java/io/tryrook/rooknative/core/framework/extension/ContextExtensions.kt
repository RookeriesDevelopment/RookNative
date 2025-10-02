package io.tryrook.rooknative.core.framework.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(@StringRes messageId: Int) {
    Toast.makeText(this, messageId, Toast.LENGTH_LONG).show()
}
