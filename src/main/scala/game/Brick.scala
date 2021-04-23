package game

import game.entities.Hittable
import mafs.{Line, Rect, Vector}
import sdl.{Canvas, RGB}

class Brick(bounds: Rect, val color: RGB) extends Hittable {

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(color)
    canvas.drawRect(bounds())
  }

  def reflect(ball: Ball): Unit = {
    ball.bounce(Vector(1, 0), "brick")
  }

  def bounds(): Rect = {
    this.bounds
  }
}
