package com.khoavna.loacationreminders.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

object PermissionUtil {
    fun Fragment.checkPermission(): Boolean = getPermission().all {
        checkSelfPermission(requireActivity(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun Fragment.getPermission() =
        inject<Array<String>>(qualifier = named(Constants.PERMISSION_NAME)).value
}