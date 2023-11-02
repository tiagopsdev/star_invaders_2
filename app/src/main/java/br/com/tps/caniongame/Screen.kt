package br.com.tps.caniongame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import br.com.tps.caniongame.Game

abstract class Screen(game: Game) {

    protected val canvas = Canvas(game.buffer)

    abstract fun update(elapsedTime: Float)
    abstract fun draw()
    abstract fun handleEvent(event: Int, x: Float, y: Float)
    abstract fun onResume()
    abstract fun onPause()
    abstract fun backPressed()
    abstract fun ScoreManegment(sharedPref: SharedPreferences)

}