package game.graphics

import mafs.{Matrix4, Rect, Vector2}

case class Sprite(var bounds: Rect, var color: RGB) {
  def translate(v: Vector2): Unit = {
    bounds = bounds.translate(v)
  }

  def setPosition(v: Vector2): Unit = {
    bounds = bounds.moveTo(v)
  }

  def setWidth(w: Float): Unit = {
    bounds = Rect(bounds.xy, w, bounds.height)
  }

  def setHeight(h: Float): Unit = {
    bounds = Rect(bounds.xy, bounds.width, h)
  }

  def setColor(c: RGB): Unit = {
    color = c
  }

  def transformMatrix(): Matrix4 = {
    Matrix4.scale(bounds.width, bounds.height) mult Matrix4.translate(bounds.xy)
  }
}

object Sprite {
  def apply(x: Float, y: Float, width: Float, height: Float, color: RGB): Sprite = Sprite(Rect(x, y, width, height), color)
  def apply(position: Vector2, width: Float, height: Float, color: RGB): Sprite = Sprite(Rect(position, width, height), color)
}
