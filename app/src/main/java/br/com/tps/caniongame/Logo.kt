package br.com.tps.caniongame

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import br.com.tps.caniongame.utils.Vector2D
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class Logo(private val context: Context, x: Float, y: Float) {

    companion object {
        const val WIDTH = 400f
        const val HEIGHT = 400f
        const val RANGE = 200f
    }

    private lateinit var image: Bitmap
    private var angle: Float = -90f
    private val matrix: Matrix = Matrix()
    private val paint = Paint()

    var pos = Vector2D(x, y)

    init {
        try {
            val inputStream = context.assets.open("Logo.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
        paint.color = Color.argb(100, 255, 0, 0)
    }

    fun draw(canvas: Canvas) {
        matrix.reset()
        matrix.postTranslate(pos.x - WIDTH /2, pos.y - HEIGHT /2)
        canvas.drawCircle(pos.x, pos.y, RANGE, paint)
        canvas.drawBitmap(image, matrix, null)
    }

    fun handleEvent(event: Int, x: Float, y: Float): Boolean {
        //Não estava desativando o active quando mudava a posição do toque
        if (event == MotionEvent.ACTION_DOWN) {
            if (calcDist(x, y) < RANGE) {
                return true
            }
        }


        return false
    }

    private fun calcAngle(x: Float, y: Float) {
        val dx = x - this.pos.x
        val dy = y - this.pos.y
        angle = radiansToDegrees(atan2(dy, dx))
    }

    private fun calcDist(x: Float, y: Float): Float {
        return sqrt((x - this.pos.x) * (x - this.pos.x) + (y - this.pos.y) * (y - this.pos.y))
    }

    private fun degreesToRadians(angle: Float): Float {
        return (angle * PI / 180).toFloat()
    }

    private fun radiansToDegrees(angle: Float): Float {
        return (angle * 180 / PI).toFloat()
    }

}