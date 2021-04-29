package mafs

final case class Line(p1: Vector2, p2: Vector2) {
  override def toString() : String = {
    s"p1:$p1, p2:$p2"
  }
}
