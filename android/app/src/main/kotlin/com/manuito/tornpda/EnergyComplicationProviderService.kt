package com.manuito.tornpda

import android.app.Service
import android.content.Intent
import android.os.IBinder

class EnergyComplicationProviderService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
