package game

import mafs.{Matrix4, Vector2, Vector4}
import sdl.RGB

case class Sprite(var width: Float, var height: Float, var color: RGB) {
  var position: Vector2 = Vector2(0f, 0f)

  def setPosition(v: Vector2): Unit = {
    position = v
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
    scaleMatrix mult Matrix4.translation(position.to4)
  }
}
