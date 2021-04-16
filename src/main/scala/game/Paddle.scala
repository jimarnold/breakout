package game

import mafs.{Rect, Vector}
import sdl.Canvas
import sdl.SDL.Keycode
import sdl.SDLKeys.{LEFT_KEY, RIGHT_KEY}

class Paddle(var position: Vector) {
  private val Left = Vector(-1, 0)
  private val Right = Vector(1, 0)
  private val Stop = Vector(0, 0)
  val speed = 500
  val width = 120
  val height = 20

  def contains(rect: Rect): Boolean = {
    rect.x < position.x + width &&
    rect.x + rect.width > position.x &&
    rect.y < position.y + height &&
    rect.y + rect.height > position.y
  }

  def reflect(p: Vector, direction: Vector): Vector = {
    val normal = Vector(0, 1)
    direction.reflect(normal)
  }

  def update(elapsed: Float, pressed: scala.collection.Set[Keycode]): Unit = {
    val direction =
      if (pressed.contains(LEFT_KEY)) Left
      else if (pressed.contains(RIGHT_KEY)) Right
      else Stop
    val oldPos = position
    position = position.plus(direction.mult(elapsed * speed))
    if (position.x < 0 || position.y < 0 || position.x > 799 || position.y > 799) {
      position = oldPos
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(200, 200, 200)
    canvas.drawRect(position, width, height)
  }
}
