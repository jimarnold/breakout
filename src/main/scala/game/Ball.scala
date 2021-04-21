package game

import mafs.{Line, Rect, Vector}
import sdl.Canvas

class Ball(var position: Vector, var direction: Vector) {
  val width = 5
  val halfWidth = 2.5f
  val height = 5
  val halfHeight = 2.5f
  val speed = 600
  private var previousPosition = position

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
    if (position.y > 799) {
      position = Vector(position.x, 799)
      direction = Vector(direction.x, -direction.y)
    }
  }

  def bounce(normal: Vector): Unit = {
    direction = direction.reflect(normal)
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(255, 0, 255)
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
}
