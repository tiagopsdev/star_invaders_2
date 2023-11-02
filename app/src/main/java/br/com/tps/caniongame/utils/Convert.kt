package br.com.tps.caniongame.utils

object Convert {

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