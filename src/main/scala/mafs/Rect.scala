package mafs

final case class Rect(x: Float, y: Float, width: Float, height: Float) {
  def translate(v: Vector2): Rect = {
    Rect(this.x + v.x, this.y + v.y, this.width, this.height)
  }

  def moveTo(x: Float, y: Float): Rect = {
    Rect(x, y, width, height)
  }

  def isOverlapping(other: Rect) = {
    if (this.topRight.y > other.bottomLeft.y || this.bottomLeft.y < other.topRight.y) false
    else if (this.topRight.x < other.bottomLeft.x || this.bottomLeft.x > other.topRight.x) false
    else true
  }
  def topLeft: Vector2 = Vector2(x, y)
  def topRight: Vector2 = Vector2(x + width, y)
  def bottomLeft: Vector2 = Vector2(x, y + height)
  def bottomRight: Vector2 = Vector2(x + width, y + height)
}
