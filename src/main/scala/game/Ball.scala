package game

import mafs.{Line, Rect, Vector}
import sdl.Canvas

class Ball(var position: Vector, var direction: Vector) {

  private val width = 10
  private val halfWidth = width / 2f
  private val height = 10
  private val halfHeight = height / 2f
  private var previousPosition = position

  private var speed = 500

  def update(elapsed: Float): Unit = {
    previousPosition = position

    position = position.plus(direction.mult(elapsed * speed))

    if (position.x < 0) {
      position = Vector(0, position.y)
      direction = Vector(-direction.x, direction.y)
    }
    if (position.y < 0) {
      position = Vector(position.x, 0)
      direction = Vector(direction.x, -direction.y)
    }
    if (position.x > 799) {
      position = Vector(799, position.y)
      direction = Vector(-direction.x, direction.y)
    }
  }

  def bounce(normal: Vector): Unit = {
    direction = direction.reflect(normal).normalize()
  }

  def draw(canvas: Canvas): Unit = {
    val color = position.y match {
      case y if y < 60 =>
        Color.one
      case y if y < 80 =>
        Color.two
      case y if y < 100 =>
        Color.three
      case y if y < 120 =>
        Color.four
      case y if y < 140 =>
        Color.five
      case y if y < 160 =>
        Color.six
      case _ =>
        Color.ball
    }
    canvas.setColor(color)
    canvas.drawRect(bounds())
  }

  def bounds(): Rect = {
    Rect(position.x - halfWidth, position.y - halfHeight, width, height)
  }

  def progressLine(): Line = {
    val lenAB = Math.sqrt(Math.pow(previousPosition.x - position.x, 2.0) + Math.pow(previousPosition.y - position.y, 2.0)).toFloat
    val end = Vector(position.x + (position.x - previousPosition.x) / lenAB * 5, position.y + (position.y - previousPosition.y) / lenAB * 5)
    new Line(previousPosition, end)
  }

  def setSpeed(hits: Int): Unit = {
    if (hits >= 4) {
      speed = 600
    }
    if (hits >= 12) {
      speed = 700
    }
  }
}
