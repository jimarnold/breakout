package mafs

final case class Rect(topLeft: Point, width: Float, height: Float) {
  val topRight: Point = Point(topLeft.x + width, topLeft.y)
  val bottomLeft: Point = Point(topLeft.x, topLeft.y + height)
  val bottomRight: Point = Point(topLeft.x + width, topLeft.y + height)
  val center: Point = Point(topLeft.x + (width / 2f), topLeft.y + (height / 2f))

  def translate(v: Vector2): Rect = {
    moveTo(topLeft.translate(v))
  }

  def moveTo(position: Point): Rect = {
    Rect(position, width, height)
  }

  def isOverlapping(other: Rect): Boolean =  {
    val result = (topRight.y <= other.bottomLeft.y) &&
      (bottomLeft.y >= other.topRight.y) &&
      (topRight.x >= other.bottomLeft.x) &&
      (bottomLeft.x <= other.topRight.x)
    result
  }

  def intersectsWith(line: Line): Boolean = {
    Line(topLeft, topRight).intersectsWith(line) ||
    Line(topRight, bottomRight).intersectsWith(line) ||
    Line(bottomRight, bottomLeft).intersectsWith(line) ||
    Line(bottomLeft, topLeft).intersectsWith(line)
  }

  def x: Float = topLeft.x
  def y: Float = topLeft.y
  def xy: Vector2 = Vector2(topLeft.x, topLeft.y)

  override def toString: String = s"$x, $y, $width, $height"
}

object Rect {
  def apply(x: Float, y: Float, width: Float, height: Float): Rect = Rect(Point(x,y), width, height)
}
