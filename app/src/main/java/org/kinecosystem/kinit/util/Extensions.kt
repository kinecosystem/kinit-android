package org.kinecosystem.kinit.util

import android.util.Patterns

fun CharSequence.isValidEmail(): Boolean
        = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()