package mafs

case class Point(x: Float, y: Float) {
  def translate(v: Vector2): Point = {
    Point(x + v.x, y + v.y)
  }
}
