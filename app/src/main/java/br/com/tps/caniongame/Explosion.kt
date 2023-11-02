package br.com.tps.caniongame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.Log
import br.com.tps.caniongame.utils.Vector2D


class Explosion(context: Context, x: Float, y: Float) {

    companion object {
        const val WIDTH = 150
        const val HEIGHT = 150
    }

    var bitmaps = arrayListOf<Bitmap>()
    private val matrix = Matrix()
    var pos: Vector2D = Vector2D(x, y)
    var frame = 0
    private var nextFrameTime = 75
    private var frameTime = 0f
    var status = false

    init {
        try {
            val inputStream = context.assets.open("explosion.png")
            val spriteSheet = BitmapFactory.decodeStream(inputStream)
            for (i in 0..7) {
                bitmaps.add(Bitmap.createBitmap(spriteSheet, i * WIDTH, 0, WIDTH, HEIGHT))
            }
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
    }

    fun update(elapsedTime: Float) : Boolean {
        if (!status) {
            frameTime += elapsedTime
            if (frameTime > nextFrameTime) {
                frameTime = 0f
                frame++
                if (frame == 8) {
                    status = true
                    return status
                }
            }
        }
        matrix.reset()
        matrix.postTranslate(pos.x - WIDTH/2, pos.y - HEIGHT/2)

        return status
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bitmaps[frame], matrix, null)
    }
}