package com.tornpda

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.PlainTextComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.data.LongTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

class EnergyComplicationProviderService : SuspendingComplicationDataSourceService() {
    
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return try {
            val energyValue = getEnergyValue()
            val energyMax = 100  // Adjust based on your game mechanics
            
            when (request.complicationType) {
                ComplicationType.SHORT_TEXT -> {
                    ShortTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText(energyValue.toString()),
                        contentDescription = PlainTextComplicationData.PlainText("Energy: $energyValue")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.LONG_TEXT -> {
                    LongTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText("Energy: $energyValue/$energyMax"),
                        contentDescription = PlainTextComplicationData.PlainText("Your energy status")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.RANGED_VALUE -> {
                    // Support ranged value for a progress-style complication
                    androidx.wear.watchface.complications.data.RangedValueComplicationData.Builder(
                        value = energyValue.toFloat(),
                        min = 0f,
                        max = energyMax.toFloat(),
                        contentDescription = PlainTextComplicationData.PlainText("Energy")
                    )
                        .setText(PlainTextComplicationData.PlainText("$energyValue"))
                        .setTapAction(createTapAction())
                        .build()
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getEnergyValue(): Int {
        val prefs = getSharedPreferences("torn_pda_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("energy", 0)
    }
    
    private fun createTapAction(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
