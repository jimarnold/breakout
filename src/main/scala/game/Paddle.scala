package game

import game.entities.Hittable
import mafs.{Rect, Vector}
import sdl.Canvas

class Paddle(var position: Vector) extends Hittable {
  val width = 120
  val height = 20

  def update(elapsed: Float, x: Int): Unit = {
    position = Vector(x, position.y)
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(Color.two)
    canvas.drawRect(bounds())
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}
