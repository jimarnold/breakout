package game.graphics

import mafs.{Matrix4, Point, Rect, Vector2}

case class Sprite(var bounds: Rect, var color: Color) {
  def position: Point = bounds.topLeft

  def translate(v: Vector2): Unit = {
    bounds = bounds.translate(v)
  }

  def setPosition(point: Point): Unit = {
    bounds = bounds.moveTo(point)
  }

  def setXPosition(x: Float): Unit = {
    bounds = bounds.moveTo(Point(x, position.y))
  }

  def setWidth(width: Float): Unit = {
    bounds = Rect(bounds.topLeft, width, bounds.height)
  }

  def setHeight(height: Float): Unit = {
    bounds = Rect(bounds.topLeft, bounds.width, height)
  }

  def setColor(c: Color): Unit = {
    color = c
  }

  def locate(): Matrix4 = {
    Matrix4.scale(bounds.width, bounds.height) mult Matrix4.translate(bounds.xy)
  }
}

object Sprite {
  def apply(x: Float, y: Float, width: Float, height: Float, color: Color): Sprite = Sprite(Rect(x, y, width, height), color)
  def apply(position: Point, width: Float, height: Float, color: Color): Sprite = Sprite(Rect(position, width, height), color)
}
