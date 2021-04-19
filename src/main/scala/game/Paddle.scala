package game

import game.entities.Hittable
import mafs.{Rect, Vector}
import sdl.Canvas
import sdl.SDL.Keycode
import sdl.SDLKeys.{LEFT_KEY, RIGHT_KEY}

class Paddle(var position: Vector, rand: java.util.Random) extends Hittable {
  private val Left = Vector(-1, 0)
  private val Right = Vector(1, 0)
  private val Stop = Vector(0, 0)
  val speed = 500
  val width = 120
  val height = 20

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
    canvas.drawRect(bounds())
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}
