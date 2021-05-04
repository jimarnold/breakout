package mafs

final case class Line(p1: Vector2, p2: Vector2) {
  override def toString : String = {
    s"p1:$p1, p2:$p2"
  }

  def intersectsWith(b: Line): Boolean = {
    val x1 = p1.x
    val y1 = p1.y
    val x2 = p2.x
    val y2 = p2.y
    val x3 = b.p1.x
    val y3 = b.p1.y
    val x4 = b.p2.x
    val y4 = b.p2.y

    val uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1))
    val uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1))

    uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1
  }
}
