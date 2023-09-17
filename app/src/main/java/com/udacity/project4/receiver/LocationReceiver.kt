package com.udacity.project4.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.utils.Constants
import com.udacity.project4.worker.LocationWorker

class LocationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.run {
            if (action == Constants.ACTION_LOCATION_REMINDER) {
                GeofencingEvent.fromIntent(this)?.let { geofencingEvent ->
                    if (geofencingEvent.hasError()) {
                        Log.e("LocationReceiver", "${geofencingEvent.errorCode}")
                        return
                    }

                    val geofenceTransition = geofencingEvent.geofenceTransition
                    if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                        geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT
                    ) {
                        geofencingEvent.triggeringGeofences?.forEach { geofence ->
                            context?.let {
                                LocationWorker.enqueue(
                                    context = context,
                                    id = geofence.requestId.toInt()
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}