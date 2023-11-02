package br.com.tps.caniongame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import androidx.core.graphics.rotationMatrix
import br.com.tps.caniongame.utils.Vector2D

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Enemy(private val context: Context, x: Float, y: Float, target: Vector2D) {

    companion object {

        const val WIDTH = 50f
        const val HEIGHT = 71f
        const val RADIUS = 80f
        const val VEL = 200f
        const val ROT_VEL = 180
        const val SCALE_vel = 0.2f
        private var angle: Float = 0f
    }

    var pos: Vector2D = Vector2D(x, y)
    private lateinit var image: Bitmap
    private val matrix: Matrix = Matrix()
    private var dir: Vector2D
    private val paint = Paint()
    private val path = Path()
    private var rotateAngle = 180f
    private var scale = 1.0f



    init {
        try {
            val inputStream = context.assets.open("xwing.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
        paint.color = Color.argb(255, 100, 100, 100)
        dir = target - pos
        dir.normalize()
    }

    fun update(elapsedTime: Float) {
        pos += dir * VEL * elapsedTime / 1000f
        //rotateAngle += ROT_VEL * elapsedTime / 1000f
        //scale -= SCALE_vel * elapsedTime / 1000f
    }

    fun draw(canvas: Canvas) {
        matrix.reset()
        matrix.postRotate(rotateAngle, Cannon.WIDTH /2f, Cannon.HEIGHT /2f)
        matrix.postTranslate(pos.x - Cannon.WIDTH /2, pos.y - Cannon.HEIGHT /2)
        //canvas.drawCircle(pos.x, pos.y, Cannon.RANGE, paint)
        canvas.drawBitmap(image, matrix, null)
    }

    fun verifyCollision(shots: MutableList<Shot>) : Boolean {
        var result = false
        val shotsToRemove = arrayListOf<Shot>()
        for (shot in shots) {
            if (calcDist(shot.pos.x, shot.pos.y) < (WIDTH * scale * 0) - 10 + shot.getSize()) {
                result = true
                shotsToRemove.add(shot)
                break
            }
        }
        for (shot in shotsToRemove) {
            shots.remove(shot)
        }

        return result
    }

    private fun calcDist(x: Float, y: Float): Float {
        return sqrt((x - this.pos.x) * (x - this.pos.x) + (y - this.pos.y) * (y - this.pos.y))
    }

    fun getSize(): Float {
        return WIDTH * scale
    }
}