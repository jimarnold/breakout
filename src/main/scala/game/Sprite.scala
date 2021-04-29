package game

import mafs.{Matrix4, Rect, Vector2, Vector4}
import sdl.RGB

case class Sprite(var x: Float, var y: Float, var width: Float, var height: Float, var color: RGB) {
  def setPosition(v: Vector2): Unit = {
    x = v.x
    y = v.y
  }

  def setWidth(w: Float): Unit = {
    width = w
  }

  def setHeight(h: Float): Unit = {
    height = h
  }

  def setColor(c: RGB): Unit = {
    color = c
  }

  def transformMatrix(): Matrix4 = {
    val scaleMatrix = Matrix4.scale(Vector4(width, height, 1f, 1f))
    scaleMatrix mult Matrix4.translation(Vector4(x, y, 0f, 1f))
  }
}

object Sprite {
  def apply(rect: Rect, color: RGB) = new Sprite(rect.x, rect.y, rect.width, rect.height, color)
}
