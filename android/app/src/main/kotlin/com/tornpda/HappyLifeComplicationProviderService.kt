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

class HappyLifeComplicationProviderService : SuspendingComplicationDataSourceService() {
    
    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        return try {
            val happylifeValue = getHappyLifeValue()
            val happylifeMax = 100  // Adjust based on your game mechanics
            
            when (request.complicationType) {
                ComplicationType.SHORT_TEXT -> {
                    ShortTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText(happylifeValue.toString()),
                        contentDescription = PlainTextComplicationData.PlainText("Happy Life: $happylifeValue")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.LONG_TEXT -> {
                    LongTextComplicationData.Builder(
                        text = PlainTextComplicationData.PlainText("Happy Life: $happylifeValue/$happylifeMax"),
                        contentDescription = PlainTextComplicationData.PlainText("Your happiness status")
                    )
                        .setTapAction(createTapAction())
                        .build()
                }
                ComplicationType.RANGED_VALUE -> {
                    // Support ranged value for a progress-style complication
                    androidx.wear.watchface.complications.data.RangedValueComplicationData.Builder(
                        value = happylifeValue.toFloat(),
                        min = 0f,
                        max = happylifeMax.toFloat(),
                        contentDescription = PlainTextComplicationData.PlainText("Happy Life")
                    )
                        .setText(PlainTextComplicationData.PlainText("$happylifeValue"))
                        .setTapAction(createTapAction())
                        .build()
                }
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }
    
    private fun getHappyLifeValue(): Int {
        val prefs = getSharedPreferences("torn_pda_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("happylife", 0)
    }
    
    private fun createTapAction(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
