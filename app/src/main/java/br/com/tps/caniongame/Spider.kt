package br.com.tps.caniongame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.util.Log
import br.com.tps.caniongame.utils.Vector2D
import kotlin.math.PI

class Spider(context: Context, x: Float, y: Float) {

    companion object {
        const val WIDTH = 96
        const val HEIGHT = 96
    }

    private lateinit var image: Bitmap
    private val src = Rect()
    private val dst = Rect()
    private val vel = 220.0f
    private var frame = 0
    private var dirF = 0
    private var animTime = 0f
    private var changeTime = 200.0f;
    private var pos = Vector2D(x, y)
    private var dir = Vector2D(0f)

    var caught = false

    init {
        try {
            val inputStream = context.assets.open("spider.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
    }

    fun update(elapsedTime: Float) {
        animTime += elapsedTime
        if (animTime > changeTime) {
            animTime = 0f
            frame = if (frame == 3) 0 else frame + 1
            src.set(frame * WIDTH, dirF * HEIGHT, frame * WIDTH + WIDTH, dirF * HEIGHT + HEIGHT)
        }
        if (!caught) {
            when (dirF) {
                0 -> dir = Vector2D((PI / 2).toFloat())
                1 -> dir = Vector2D(PI.toFloat())
                2 -> dir = Vector2D(0f)
                3 -> dir = Vector2D((3 * PI / 2).toFloat())
            }
        }
        pos += dir * vel * elapsedTime / 1000f
        dst.set(
            (pos.x - WIDTH /2 * 4).toInt(), (pos.y - HEIGHT /2 * 4).toInt(),
            (pos.x + WIDTH /2 * 4).toInt(), (pos.y + HEIGHT /2 * 4).toInt())
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, src, dst, null)
    }

    fun moveTo(x: Float, y: Float) {
        pos = Vector2D(x, y)
    }

    fun verifyWasCaught(x: Float, y: Float): Boolean {
        if (dst.contains(x.toInt(), y.toInt())) {
            caught = true
            changeTime = 40f
            return true
        }
        return false
    }

    fun wasReleased() {
        caught = false
        changeTime = 200f
    }

    fun changeDir() {
        dirF++
        if (dirF > 3) {
            dirF = 0
        }
    }
}