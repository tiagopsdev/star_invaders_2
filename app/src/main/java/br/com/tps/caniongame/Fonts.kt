package br.com.tps.caniongame

import android.content.Context
import android.graphics.Typeface
import android.util.Log

object Fonts {

    lateinit var satarjedi: Typeface

    fun initializeFonts(context: Context) {
        try {
            satarjedi = Typeface.createFromAsset(context.assets, "Starjedi.ttf")
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
    }
}