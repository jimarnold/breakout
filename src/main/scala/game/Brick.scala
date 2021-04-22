package game

import game.entities.Hittable
import mafs.{Line, Rect, Vector}
import sdl.{Canvas, RGB}

class Brick(bounds: Rect, color: RGB) extends Hittable {
  def draw(canvas: Canvas): Unit = {
    canvas.setColor(color)
    canvas.drawRect(bounds())
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()

    val top = Line(Vector(bounds.x, bounds.y), Vector(bounds.x + bounds.width, bounds.y))
    val bottom = Line(Vector(bounds.x, bounds.y + bounds.height), Vector(bounds.x + bounds.width, bounds.y + bounds.height))
    val left = Line(Vector(bounds.x, bounds.y), Vector(bounds.x, bounds.y + bounds.height))
    val right = Line(Vector(bounds.x + bounds.width, bounds.y), Vector(bounds.x + bounds.width, bounds.y + bounds.height))
    val normal = if (Line.doIntersect(incomingLine, bottom)) {
      Vector(1, 0)
    } else if (Line.doIntersect(incomingLine, top)) {
      Vector(1, 0)
    } else if (Line.doIntersect(incomingLine, left)) {
      Vector(0, 1)
    } else if (Line.doIntersect(incomingLine, right)) {
      Vector(0, 1)
    } else {
      Vector(1, 0)
    }
    ball.bounce(normal)
  }

  def bounds(): Rect = {
    this.bounds
  }
}
