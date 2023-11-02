package br.com.tps.caniongame

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import br.com.tps.caniongame.utils.DimensionsGameScreen
import kotlin.math.abs

class Game(val context: Context) {

    var buffer: Bitmap
    var render: Render
    var actualScreen: Screen? = null

    private var nWid = 0f
    private var nHei = 0f
    private var hDist = 0f
    private var vDist = 0f
    private var sx = 0f
    private var sy = 0f

    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)

    init {
        val isLandscape = context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        val bufferWidth = if (isLandscape) 1800f else 1080f
        val bufferHeight = if (isLandscape) 1080f else 1800f
        buffer = Bitmap.createBitmap(bufferWidth.toInt(), bufferHeight.toInt(), Bitmap.Config.ARGB_8888)
        val screenWidth = context.resources.displayMetrics.widthPixels.toFloat()
        val screenHeight = context.resources.displayMetrics.heightPixels.toFloat()
        val aspectBuffer = bufferWidth / bufferHeight
        val aspectScreen = screenWidth / screenHeight
        if (aspectBuffer > aspectScreen || abs(aspectScreen - aspectBuffer) < 0.1) {
            this.nWid = screenWidth
            val fAsp = aspectScreen / aspectBuffer
            this.nHei = screenHeight * fAsp
            this.vDist = (screenHeight - nHei) / 2f
        }
        else {
            this.nHei = screenHeight
            this.nWid = nHei * aspectBuffer
            this.hDist = (screenWidth - nWid) / 2f
        }

        sx = nWid / bufferWidth
        sy = nHei / bufferHeight

        render = Render(context, buffer)
        render.setOnTouchListener(render)

        //Salvar as configurações de tela
        DimensionsGameScreen.xDist = hDist
        DimensionsGameScreen.yDist = vDist
    }

    inner class Render(context: Context, private val buffer: Bitmap) : View(context), OnTouchListener {

        private var startTime = System.nanoTime()
        private var paint = Paint()

        init {
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
        }

        override fun draw(canvas: Canvas?) {
            super.draw(canvas)

            val src = Rect(0, 0, buffer.width, buffer.height)
            val dst = Rect(hDist.toInt(), vDist.toInt(), (nWid + hDist).toInt(), (nHei + vDist).toInt())
            DimensionsGameScreen.screen = dst
            val deltaTime = (System.nanoTime() - startTime) / 1000000f
            startTime = System.nanoTime()

            actualScreen?.update(deltaTime)
            actualScreen?.draw()

            canvas?.let {
                it.drawRGB(0, 0, 0)
                it.drawBitmap(buffer, src, dst, paint)
            }

            invalidate()
        }

        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            event?.let {
                val x = (it.x - hDist) / sx
                val y = (it.y - vDist) / sy
                actualScreen?.handleEvent(it.action, x, y)
            }

            return true
        }
    }

    fun onResume() {
        actualScreen?.onResume()
    }

    fun onPause() {
        actualScreen?.onPause()
    }

    fun backPressed() {
        actualScreen?.backPressed()
    }
}