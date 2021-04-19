package game.entities

import mafs.{Line, Rect, Vector}

trait Hittable {
  def bounds(): Rect
  def contains(v: Vector): Boolean = {
    val bounds: Rect = this.bounds()
    (v.x >= bounds.x && v.x <= (bounds.x + bounds.width)) && (v.y >= bounds.y && v.y <= (bounds.y + bounds.height))
  }

  def reflect(p: Vector, d: Vector): Vector = {
    val bounds: Rect = this.bounds()
    val reverseDirection = d.reverse()
    val incomingLine = new Line(p, p.plus(reverseDirection.mult(10)))
    val normal = if (Line.doIntersect(
      incomingLine.p1,
      incomingLine.p2,
      Vector(bounds.x, bounds.y + bounds.height),
      Vector(bounds.x + bounds.width, bounds.y + bounds.height))) {
        Vector(0, 1)
    } else if (Line.doIntersect(
      incomingLine.p1,
      incomingLine.p2,
      Vector(bounds.x, bounds.y),
      Vector(bounds.x + bounds.width, bounds.y))) {
        Vector(0, 1)
    } else {
      Vector(1, 0)
    }
    d.reflect(normal)
  }
}
