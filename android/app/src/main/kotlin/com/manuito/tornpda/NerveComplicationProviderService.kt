package com.manuito.tornpda

import android.app.Service
import android.content.Intent
import android.os.IBinder

// Minimaler Skeleton-Service damit die Klasse beim Build vorhanden ist.
// Für echte Complication-Logik musst du die passende Wear API / Baseklasse verwenden.
class NerveComplicationProviderService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
