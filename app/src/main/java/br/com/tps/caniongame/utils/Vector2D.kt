package br.com.tps.caniongame.utils

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Vector2D() {

    var x: Float = 0.0f
    var y: Float = 0.0f

    constructor(x: Float, y: Float): this() {
        this.x = x
        this.y = y
    }

    constructor(angle: Float): this() {
        this.x = cos(angle)
        this.y = sin(angle)
    }

    operator fun plus(other: Vector2D): Vector2D {
        return Vector2D(this.x + other.x, this.y + other.y)
    }

    operator fun minus(other: Vector2D): Vector2D {
        return Vector2D(this.x - other.x, this.y - other.y)
    }

    operator fun times(value: Float): Vector2D {
        return Vector2D(this.x * value, this.y * value)
    }

    operator fun div(value: Float): Vector2D {
        return Vector2D(this.x / value, this.y / value)
    }

    fun size(): Float {
        return sqrt(x*x + y*y)
    }

    fun normalize() {
        this.x /= size()
        this.y /= size()
    }
}