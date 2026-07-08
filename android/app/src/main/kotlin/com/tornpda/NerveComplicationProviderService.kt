package com.tornpda

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainTextComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationDataSourceUpdateRequester
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.ComplicationRequestListener
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class NerveComplicationProviderService : SuspendingComplicationDataSourceService() {
    
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return try {
            val nerveValue = getNerveValue()
            val nerveMax = 100  // Adjust based on your game mechanics
            
            when (request.complicationType) {
                ComplicationType.SHORT_TEXT -> {
                    ShortTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText(nerveValue.toString()),
                        contentDescription = PlainTextComplicationData.PlainText("Nerve: $nerveValue")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.LONG_TEXT -> {
                    LongTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText("Nerve: $nerveValue/$nerveMax"),
                        contentDescription = PlainTextComplicationData.PlainText("Your nerve status")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.RANGED_VALUE -> {
                    // Support ranged value for a progress-style complication
                    androidx.wear.watchface.complications.data.RangedValueComplicationData.Builder(
                        value = nerveValue.toFloat(),
                        min = 0f,
                        max = nerveMax.toFloat(),
                        contentDescription = PlainTextComplicationData.PlainText("Nerve")
                    )
                        .setText(PlainTextComplicationData.PlainText("$nerveValue"))
                        .setTapAction(createTapAction())
                        .build()
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getNerveValue(): Int {
        val prefs = getSharedPreferences("torn_pda_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("nerve", 0)
    }
    
    private fun createTapAction(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
