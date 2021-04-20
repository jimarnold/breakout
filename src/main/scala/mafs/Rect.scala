package mafs

final case class Rect(x: Float, y: Float, width: Float, height: Float) {
  def isOverlapping(other: Rect) = {
    if (this.topRight.y > other.bottomLeft.y || this.bottomLeft.y < other.topRight.y) false
    else if (this.topRight.x < other.bottomLeft.x || this.bottomLeft.x > other.topRight.x) false
    else true
  }
  def topLeft: Vector = Vector(x, y)
  def topRight: Vector = Vector(x + width, y)
  def bottomLeft: Vector = Vector(x, y + height)
  def bottomRight: Vector = Vector(x + width, y + height)
}
