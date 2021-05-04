package mafs

final case class Rect(topLeft: Vector2, width: Float, height: Float) {
  val topRight: Vector2 = Vector2(topLeft.x + width, topLeft.y)
  val bottomLeft: Vector2 = Vector2(topLeft.x, topLeft.y + height)
  val bottomRight: Vector2 = Vector2(topLeft.x + width, topLeft.y + height)

  def translate(v: Vector2): Rect = {
    moveTo(Vector2(topLeft.x + v.x, topLeft.y + v.y))
  }

  def moveTo(position: Vector2): Rect = {
    Rect(position, width, height)
  }

  def isOverlapping(other: Rect): Boolean =  {
    (topRight.y <= other.bottomLeft.y) &&
    (bottomLeft.y >= other.topRight.y) &&
    (topRight.x >= other.bottomLeft.x) &&
    (bottomLeft.x <= other.topRight.x)
  }

  def intersectsWith(line: Line): Boolean = {
    Line(topLeft, topRight).intersectsWith(line) ||
    Line(topRight, bottomRight).intersectsWith(line) ||
    Line(bottomRight, bottomLeft).intersectsWith(line) ||
    Line(bottomLeft, topLeft).intersectsWith(line)
  }

  def x: Float = topLeft.x
  def y: Float = topLeft.y
  def xy: Vector2 = topLeft
}

object Rect {
  def apply(x: Float, y: Float, width: Float, height: Float): Rect = Rect(Vector2(x,y), width, height)
}
