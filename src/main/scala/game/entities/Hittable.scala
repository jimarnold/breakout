package game.entities

import game.Ball
import mafs.{Line, Rect, Vector}

trait Hittable {
  var spin = false
  val maxSpinAngle = 20

  def bounds(): Rect

  def contains(other: Rect): Boolean = {
    this.bounds().isOverlapping(other)
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
      val n = Vector(1, 0)
      if (spin) {
        val myCenter = bounds.x + (bounds.width / 2)
        val angle = ((incomingLine.p2.x - myCenter) / bounds.width) * maxSpinAngle
        n.rotate(angle)
      } else {
        n
      }
    } else if (Line.doIntersect(incomingLine, left)) {
      Vector(0, 1)
    } else if (Line.doIntersect(incomingLine, right)) {
      Vector(0, 1)
    } else {
      Vector(1, 0)
    }
    ball.bounce(normal)
  }
}
