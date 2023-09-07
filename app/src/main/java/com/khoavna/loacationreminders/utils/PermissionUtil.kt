package com.khoavna.loacationreminders.utils

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

object PermissionUtil {
    fun Fragment.checkPermission(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(
            requireActivity(),
            permission
        ) == PackageManager.PERMISSION_DENIED
}