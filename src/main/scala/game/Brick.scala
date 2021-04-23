package game

import game.entities.Hittable
import mafs.{Rect, Vector}
import sdl.{Canvas}

class Brick(bounds: Rect, val wall: Wall) extends Hittable {

  val color = wall.getColor(bounds.y)
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
