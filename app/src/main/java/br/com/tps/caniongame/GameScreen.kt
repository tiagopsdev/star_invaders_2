package br.com.tps.caniongame

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import br.com.tps.caniongame.utils.DimensionsGameScreen
import br.com.tps.caniongame.utils.Vector2D
import kotlin.random.Random

class GameScreen(private val game: Game) : Screen(game) {

    private var music: Music? = null
    private var cannon: Cannon? = Cannon(game.context, canvas.width/2f, canvas.height/1f-300)
    private var shots = arrayListOf<Shot>()
    private var enemies = arrayListOf<Enemy>()
    private var explosions = arrayListOf<Explosion>()
    private val textPaint = Paint()
    private var timeCounter: Float = 0f
    private var totalElapsedTimeString = ""


    private val rnd = Random
    private var nextEnemyTime = rnd.nextInt(3, 6)
    private var enemyTime = 0f
    private var addEnemyTime = 0f
    private var timeToAddEnemyTime = 7
    private var nEnemies = 1


    init {
        try {
            val descriptor = game.context.assets.openFd("music.mp3")
            music = Music(descriptor)
        }
        catch (e: Exception) {
            Log.d("Jogo2", e.localizedMessage)
        }
        music?.setLooping(true)
        music?.setVolume(1f)
        music?.play()

        textPaint.typeface = Fonts.satarjedi
        textPaint.color = Color.YELLOW
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.textSize = 50f
    }

    override fun update(elapsedTime: Float) {



        cannon?.let { cannon ->
            addEnemyTime += elapsedTime / 1000f
            if (addEnemyTime > timeToAddEnemyTime) {
                addEnemyTime = 0f
                nEnemies++
            }

            enemyTime += elapsedTime / 1000f
            if (enemyTime > nextEnemyTime) {
                enemyTime = 0f
                nextEnemyTime = rnd.nextInt(2, 4)
                for (i in 1..nEnemies) {

                    val xPos= rnd.nextInt(80, 1000).toFloat()
                    val yPos = 80f
                    val enemy = Enemy(game.context, xPos, yPos, cannon.pos)
                    enemies.add(enemy)
                }
            }
        }
        cannon?.update(elapsedTime)
        val shotsToRemove = arrayListOf<Shot>()
        for (shot in shots) {
            shot.update(elapsedTime)
            if (outScreen(shot.pos)) {
                shotsToRemove.add(shot)
            }
        }
        for (shot in shotsToRemove) {
            shots.remove(shot)
        }
        val enemiesToRemove = arrayListOf<Enemy>()
        for (enemy in enemies) {
            enemy.update(elapsedTime)
            if (enemy.verifyCollision(shots)) {
                explosions.add(Explosion(game.context, enemy.pos.x, enemy.pos.y))
                enemiesToRemove.add(enemy)
            }
        }
        for (enemy in enemiesToRemove) {
            enemies.remove(enemy)
        }
        cannon?.let {
            if (it.verifyCollision(enemies)) {
                explosions.add(Explosion(game.context, it.pos.x, it.pos.y))
                explosions.add(Explosion(game.context, it.pos.x - 150, it.pos.y - 150))
                explosions.add(Explosion(game.context, it.pos.x - 150, it.pos.y + 150))
                explosions.add(Explosion(game.context, it.pos.x + 150, it.pos.y - 150))
                explosions.add(Explosion(game.context, it.pos.x + 150, it.pos.y + 150))
                cannon = null
                enemies.clear()
                ScoreManegment(game.sharedPref)
                game.actualScreen = LastScreen(game)
            }
        }
        val explosionsToRemove = arrayListOf<Explosion>()
        for (explosion in explosions) {
            if (explosion.update(elapsedTime)) {
                explosionsToRemove.add(explosion)
            }
        }
        for (explosion in explosionsToRemove) {
            explosions.remove(explosion)
        }


            timeCounter += elapsedTime
            totalElapsedTimeString = convertMillisecondsToTime(timeCounter)

    }

    override fun draw() {
        canvas.drawRGB(0, 0, 0)
        cannon?.draw(canvas)
        for (shot in shots) {
            shot.draw(canvas)
        }
        for (enemy in enemies) {
            enemy.draw(canvas)
        }
        for (explosion in explosions) {
            explosion.draw(canvas)
        }




        cannon?.let {

            canvas.drawText(totalElapsedTimeString, 0f, (DimensionsGameScreen.screen.bottom.toFloat() -400f), textPaint)

        }





    }

    override fun handleEvent(event: Int, x: Float, y: Float) {
        cannon?.handleEvent(event, x, y)?.let { shot ->
            shots.add(shot)
        }
    }

    override fun onResume() {

    }

    override fun onPause() {
        music?.pause()
        music?.dispose()
    }

    override fun backPressed() {
        music?.pause()
        music?.dispose()
        game.actualScreen = FirstScreen(game)
    }

    override fun ScoreManegment(sharedPref: SharedPreferences) {
        var scores = mutableListOf(timeCounter.toInt())
        var score: Int


        for (key in listOf("1", "2", "3")){

            score = sharedPref.getInt(key, -1)
            if(score >=0)
                scores.add(score)
        }
        scores.sortDescending()

        with (sharedPref.edit()) {
            var i: Int
            for (key in listOf("1", "2", "3")){
                 i = (key.toInt()-1)
                if(i < scores.size)
                    putInt(key, scores[i])

            }
            apply()
        }
    }

    private fun outScreen(pos: Vector2D): Boolean {
        return pos.x < 0 || pos.x > canvas.width || pos.y < 0 || pos.y > canvas.height
    }

    fun convertMillisecondsToTime(t: Float): String {

        val milliseconds = (t % 1000).toInt()
        val totalSeconds = t / 1000
        val seconds = (totalSeconds % 60).toInt()
        val totalMinutes = (totalSeconds / 60).toInt()
        val minutes = (totalMinutes % 60)
        val hours = (totalMinutes / 60)

        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, milliseconds)
    }
}