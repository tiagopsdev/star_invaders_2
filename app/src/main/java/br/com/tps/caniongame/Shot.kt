package br.com.tps.caniongame

import android.content.Context
import android.graphics.*
import android.util.Log
import br.com.tps.caniongame.utils.Vector2D

class Shot(context: Context, x: Float, y: Float, angle: Float, rangle: Float) {

    companion object {
        const val SIZE = 40f
        const val WIDTH = 172f
        const val HEIGHT = 9f
        const val VEL = 300f
    }

    private lateinit var image: Bitmap
    private val matrix: Matrix = Matrix()
    var pos: Vector2D = Vector2D(x, y)
    private var dir: Vector2D = Vector2D(angle)
    private var shotAngle = rangle

    init {
        try {
            val inputStream = context.assets.open("lasergreen.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
    }

    fun update(elapsedTime: Float) {
        pos += dir * VEL * elapsedTime / 1000f
    }

    fun draw(canvas: Canvas) {
        matrix.reset()
        matrix.postRotate(shotAngle, WIDTH /2f, HEIGHT /2f)
        matrix.postTranslate(pos.x - WIDTH /2, pos.y - HEIGHT /2)
        canvas.drawBitmap(image, matrix, null)
    }

    fun getSize(): Float {
        return WIDTH
    }
}