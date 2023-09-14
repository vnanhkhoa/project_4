package com.khoavna.loacationreminders.worker

import android.Manifest
import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.khoavna.loacationreminders.R
import com.khoavna.loacationreminders.data.database.entites.Location
import com.khoavna.loacationreminders.domain.location.LocationUseCase
import com.khoavna.loacationreminders.ui.locationdetail.LocationDetailFragmentArgs
import com.khoavna.loacationreminders.utils.Constants.CHANNEL_ID
import com.khoavna.loacationreminders.utils.PermissionUtil.checkPermission

class LocationWorker(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val locationUseCase: LocationUseCase,
) : CoroutineWorker(context, workerParameters) {

    private var notification: Notification? = null
    override suspend fun doWork(): Result {

        val id = requireNotNull(inputData.getInt(ID_KEY, -1))
        val location = locationUseCase.getLocation(id)
        notification = sendNotification(location).build()
        return Result.success()
    }

    private fun sendNotification(location: Location) =
        NotificationCompat.Builder(context, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.round_location_on_24)
            setContentTitle(location.title)
            setContentText(location.locationName)
            setAutoCancel(true)
            setContentIntent(
                NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.locationDetailFragment)
                    .setArguments(LocationDetailFragmentArgs(location = location).toBundle())
                    .createPendingIntent()
            )
            priority = NotificationCompat.PRIORITY_DEFAULT
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        }

    override suspend fun getForegroundInfo(): ForegroundInfo =
        NotificationManagerCompat.from(context).run {
            if (!context.checkPermission(Manifest.permission.POST_NOTIFICATIONS)) {
                return super.getForegroundInfo()
            }
            return notification?.let { notification ->
                notify(NOTIFICATION_ID, notification)
                ForegroundInfo(NOTIFICATION_ID, notification)
            } ?: super.getForegroundInfo()
        }

    companion object {
        const val NOTIFICATION_ID = 2023
        const val ID_KEY = "ID_KEY"

        fun enqueue(context: Context, id: Int) {
            val data = Data.Builder().putInt(ID_KEY, id).build()
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(true)
                .build()

            val request = OneTimeWorkRequest.Builder(LocationWorker::class.java)
                .setConstraints(constraints)
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setInputData(data)
                .build()

            WorkManager.getInstance(context).enqueue(request)
        }
    }
}