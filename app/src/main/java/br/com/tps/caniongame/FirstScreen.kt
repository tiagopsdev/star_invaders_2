package br.com.tps.caniongame

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import kotlin.random.Random

class FirstScreen(val game: Game) : Screen(game) {

    private val drawPaint = Paint()
    private val textPaint = Paint()
    private val rnd = Random
    private var changeTime = 0f
    private var x = canvas.width / 2f
    private var y = canvas.height / 2f
    private var w = 400f
    private var h = 400f
    private val boundingBox: Rect
    private val logo = Logo(game.context, x, y)

    init {
        drawPaint.color = Color.BLACK
        textPaint.typeface = Fonts.satarjedi
        textPaint.color = Color.BLUE
        textPaint.textAlign = Paint.Align.CENTER
        boundingBox = Rect((x - w/2).toInt(), (y - h/2).toInt(), (x + w/2).toInt(), (y + h/2).toInt())
    }

    override fun update(elapsedTime: Float) {
        changeTime += elapsedTime
        if (changeTime > 1000) {
            changeTime = 0f
            drawPaint.color = Color.rgb(
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256)
            )
        }
    }

    override fun draw() {
        canvas.drawRGB(255, 255, 255)

        textPaint.textSize = 100f
        canvas.drawText("Star Wars", canvas.width/2f, 120f, textPaint)
        textPaint.textSize = 50f
        val posText = logo.pos.y * 1.3f + textPaint.textSize + 10f
        canvas.drawText("Iniciar o Jogo", canvas.width/2f, posText, textPaint)
        logo.draw(canvas)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            if (boundingBox.contains(x.toInt(), y.toInt())) {
                game.actualScreen = GameScreen(game)
            }
            if (logo.handleEvent(event, x, y)){
                game.actualScreen = GameScreen(game)
            }
        }
    }

    override fun onResume() {

    }

    override fun onPause() {

    }

    override fun backPressed() {

    }

    override fun ScoreManegment(sharedPref: SharedPreferences) {

    }
}