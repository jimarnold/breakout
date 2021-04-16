package game

import mafs.{Rect, Vector}
import sdl.Canvas

class Ball(var position: Vector, var direction: Vector) {
  val width = 5
  val height = 5
  val speed = 500

  def update(elapsed: Float): Unit = {
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

  def bounce(v: Vector): Unit = {
    direction = v
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(255, 0, 255)
    canvas.drawRect(position, 5, 5)
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}
