package com.khoavna.loacationreminders.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class LocationWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return Result.success()
    }

}