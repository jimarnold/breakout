package game

import mafs.{Rect, Vector}
import sdl.Canvas

class Brick(var position: Vector) {
  val width = 30
  val height = 10
  def contains(v: Vector): Boolean = {
    (v.x >= position.x && v.x <= (position.x + width)) && (v.y >= position.y && v.y <= (position.y + height))
  }
  def contains(rect: Rect): Boolean = {
    rect.x < position.x + width &&
    rect.x + rect.width > position.x &&
    rect.y < position.y + height &&
    rect.y + rect.height > position.y
  }

  def reflect(v: Vector): Vector = {
    val lowNormal = Vector(0, 1)
    v.reflect(lowNormal)
  }

  def update(): Unit = {

  }
  def draw(canvas: Canvas): Unit = {
    canvas.setColor(0, 150, 0)
    canvas.drawRect(position, 30, 10)
  }
}
