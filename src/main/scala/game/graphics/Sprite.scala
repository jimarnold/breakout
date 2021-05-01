package game.graphics

import mafs.{Matrix4, Rect, Vector2}

case class Sprite(var bounds: Rect, var color: RGB) {
  def position: Vector2 = bounds.topLeft

  def translate(v: Vector2): Unit = {
    bounds = bounds.translate(v)
  }

  def setPosition(v: Vector2): Unit = {
    bounds = bounds.moveTo(v)
  }

  def setXPosition(x: Float): Unit = {
    bounds = bounds.moveTo(Vector2(x, position.y))
  }

  def setWidth(width: Float): Unit = {
    bounds = Rect(bounds.xy, width, bounds.height)
  }

  def setHeight(height: Float): Unit = {
    bounds = Rect(bounds.xy, bounds.width, height)
  }

  def setColor(c: RGB): Unit = {
    color = c
  }

  def locate(): Matrix4 = {
    Matrix4.scale(bounds.width, bounds.height) mult Matrix4.translate(bounds.xy)
  }
}

object Sprite {
  def apply(x: Float, y: Float, width: Float, height: Float, color: RGB): Sprite = Sprite(Rect(x, y, width, height), color)
  def apply(position: Vector2, width: Float, height: Float, color: RGB): Sprite = Sprite(Rect(position, width, height), color)
}
