package game

import mafs.{Line, Rect, Vector}
import sdl.Canvas

class Ball(var position: Vector, var direction: Vector, wall: Wall) {

  private val width = 10
  private val halfWidth = width / 2f
  private val height = 10
  private val halfHeight = height / 2f

  private var previousPosition = position
  private var speed = 500
  var lastEntity: String = ""

  def update(elapsed: Float): Unit = {
    previousPosition = position
    position = position.plus(direction.mult(elapsed * speed))
  }

  def bounce(normal: Vector, entity: String): Unit = {
    if (entity != lastEntity) {
      direction = direction.reflect(normal).normalize()
      lastEntity = entity
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(wall.getColor(position.y))
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
