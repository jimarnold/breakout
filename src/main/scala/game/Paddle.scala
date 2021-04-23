package game

import game.entities.Hittable
import mafs.{Rect, Vector}
import sdl.Canvas

class Paddle(var position: Vector, gameField: Rect) extends Hittable {
  private val width = 120
  private val halfWidth = width / 2
  private val height = 20

  def update(elapsed: Float, x: Int): Unit = {
    position = Vector(x, position.y)

    if (position.x + width > gameField.x + gameField.width) {
      position = Vector((gameField.x + gameField.width) - width, position.y)
    }
    if (position.x < gameField.x) {
      position = Vector(gameField.x, position.y)
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(Color.paddle)
    canvas.drawRect(bounds())
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()

    val normal = if (incomingLine.p2.x >= bounds.x + halfWidth && incomingLine.p1.x >= incomingLine.p2.x) {
      // coming from the right, hitting right
      Vector(1, 1)
    } else if (incomingLine.p2.x <= bounds.x + halfWidth && incomingLine.p1.x <= incomingLine.p2.x) {
      // coming from the left, hitting left
      Vector(1, -1)
    } else {
      Vector(1, 0)
    }
    ball.bounce(normal)
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}
