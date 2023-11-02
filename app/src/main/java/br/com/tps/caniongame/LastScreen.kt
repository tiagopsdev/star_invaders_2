package br.com.tps.caniongame

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import br.com.tps.caniongame.utils.Convert

class LastScreen(val  game: Game) : Screen(game) {

    private val btnRestartPaint = Paint()
    private val drawPaint = Paint()
    private val textPaint = Paint()
    private val btnRestartTextPaint = Paint()
    private val textPaintFixed = Paint()
    private val btnRestart: Rect
    private var scoreList = mutableListOf<Int>()
    var result = ""

    private var drawMainColor = Color.BLACK
    private var texColor = Color.RED
    private var changeTime = 0f
    private var x = canvas.width / 2f
    private var y = canvas.height / 2f
    private var w = 600f
    private var h = 600



    init {

        ScoreManegment(game.sharedPref)
        textPaintFixed.typeface = Fonts.satarjedi
        textPaintFixed.color = Color.YELLOW
        textPaintFixed.textAlign = Paint.Align.CENTER

        drawPaint.color = drawMainColor

        textPaint.typeface = Fonts.satarjedi
        textPaint.color = texColor
        textPaint.textAlign = Paint.Align.CENTER

        btnRestartTextPaint.typeface = Fonts.satarjedi
        btnRestartTextPaint.color = Color.YELLOW
        btnRestartTextPaint.textAlign = Paint.Align.CENTER
        btnRestartTextPaint.textSize = 100f

        btnRestart = Rect(0, (canvas.height - 10), canvas.width, (canvas.height - 210))
        btnRestartPaint.color = Color.GRAY
    }

    override fun update(elapsedTime: Float) {
        changeTime += elapsedTime
        if (changeTime > 1000) {
            changeTime = 0f

            drawPaint.color = drawMainColor
            textPaint.color = texColor

        }

    }

    override fun draw() {
        canvas.drawRGB(0, 0, 0)

        textPaintFixed.textSize = 100f
        canvas.drawText("Fim de Jogo", canvas.width/2f, textPaintFixed.textSize + 10, textPaintFixed)
        canvas.drawText("Tempo de Jogo", canvas.width/2f, textPaintFixed.textSize * 2f + 10, textPaintFixed)

        textPaint.textSize = 100f
        result = ""
        val firstScoreLine = textPaintFixed.textSize * 3f + 10
        var line = 1f
        for (score in scoreList){

            result =  Convert.convertMillisecondsToTime(score.toFloat())

            canvas.drawText(result, canvas.width/2f, firstScoreLine + textPaint.textSize * line + 10, textPaint)

            line++
        }



        canvas.drawRect(btnRestart, btnRestartPaint)
        canvas.drawText("voltar", (canvas.width / 2).toFloat(), (canvas.height - 30).toFloat(), btnRestartTextPaint)
    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        if (event == MotionEvent.ACTION_DOWN) {
            Log.i("Toques", "X${x.toInt()}, y${y.toInt()}")
            Log.i("Toques", "B${btnRestart.bottom}, T${btnRestart.top}, L${btnRestart.left}, R${btnRestart.right}")
            Log.i("Toques", "${btnRestart.contains(x.toInt(), y.toInt())}")
            if (btnRestart.contains(x.toInt(), y.toInt()) || (btnRestart.left <= x.toInt() && x.toInt() > btnRestart.right || btnRestart.bottom < y.toInt() && y.toInt() <= btnRestart.top) ) {
                game.actualScreen = FirstScreen(game)
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
        var score: Int
        for (key in listOf("1", "2", "3")){

            score = sharedPref.getInt(key, -1)
            if(score >=0)
                scoreList.add(score)
        }
    }
}