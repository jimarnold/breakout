package mafs

final case class Rect(x: Float, y: Float, width: Float, height: Float) {
  val topLeft: Vector2 = Vector2(x, y)
  val topRight: Vector2 = Vector2(x + width, y)
  val bottomLeft: Vector2 = Vector2(x, y + height)
  val bottomRight: Vector2 = Vector2(x + width, y + height)

  def translate(v: Vector2): Rect = {
    moveTo(this.x + v.x, this.y + v.y)
  }

  def moveTo(x: Float, y: Float): Rect = {
    Rect(x, y, width, height)
  }

  def isOverlapping(other: Rect): Boolean =  {
    (topRight.y <= other.bottomLeft.y) &&
    (bottomLeft.y >= other.topRight.y) &&
    (topRight.x >= other.bottomLeft.x) &&
    (bottomLeft.x <= other.topRight.x)
  }

  def xy: Vector2 = topLeft
}
