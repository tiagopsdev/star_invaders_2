package br.com.tps.caniongame

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.MotionEvent
import br.com.tps.caniongame.utils.Vector2D
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class Cannon(private val context: Context, x: Float, y: Float) {

    companion object {
        const val WIDTH = 344f
        const val HEIGHT = 244f
        const val RANGE = 300f
    }

    private lateinit var image: Bitmap
    private var angle: Float = -90f
    private val matrix: Matrix = Matrix()
    private var active = false
    private val paint = Paint()

    var pos = Vector2D(x, y)

    init {
        try {
            val inputStream = context.assets.open("starDestroyer.png")
            image = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
        }
        catch (e: Exception) {
            Log.d("Jogo!", e.localizedMessage)
        }
        paint.color = Color.argb(100, 255, 0, 0)
    }

    fun update(elapsedTime: Float) {

    }

    fun draw(canvas: Canvas) {
        matrix.reset()
        matrix.postRotate(angle, WIDTH /2f, HEIGHT /2f)
        matrix.postTranslate(pos.x - WIDTH /2, pos.y - HEIGHT /2)
        canvas.drawCircle(pos.x, pos.y, RANGE, paint)
        canvas.drawBitmap(image, matrix, null)
    }

    fun handleEvent(event: Int, x: Float, y: Float): Shot? {
        //Não estava desativando o active quando mudava a posição do toque
        if (event == MotionEvent.ACTION_DOWN) {
            if (calcDist(x, y) < RANGE) {
                //active = true
                calcAngle(x, y)
            }
        }
        else if (event == MotionEvent.ACTION_MOVE) {
            if (calcDist(x, y) < RANGE) {
                calcAngle(x, y)
            }
        }
        else if (event == MotionEvent.ACTION_UP) {
            if (calcDist(x, y) < RANGE) {
                return shoot()
            }
        }

        return null
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

    private fun shoot(): Shot {
        val posShot = pos + Vector2D(degreesToRadians(angle)) * WIDTH /2f
        return Shot(context, posShot.x, posShot.y, degreesToRadians(angle), angle)
    }
    fun verifyCollision(enemies: MutableList<Enemy>) : Boolean {
        var result = false
        var enemiesToRemove = arrayListOf<Enemy>()
        for (enemy in enemies) {
            if (calcDist(enemy.pos.x, enemy.pos.y) < 75f + enemy.getSize()) {
                result = true
                enemiesToRemove.add(enemy)
                break
            }
        }
        for (enemy in enemiesToRemove) {
            enemies.remove(enemy)
        }

        return result
    }
}