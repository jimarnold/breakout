package game

import game.entities.Hittable
import mafs.Rect
import sdl.{Canvas, RGB}

class Brick(bounds: Rect, color: RGB) extends Hittable {
  def draw(canvas: Canvas): Unit = {
    canvas.setColor(color)
    canvas.drawRect(bounds())
  }

  def bounds(): Rect = {
    this.bounds
  }
}
