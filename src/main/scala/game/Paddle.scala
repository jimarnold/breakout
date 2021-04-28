package game

import game.entities.Hittable
import mafs.{Rect, Vector2}

class Paddle(var position: Vector2, gameField: Rect) extends Hittable {
  private var width = 120f
  private var halfWidth = width / 2
  private val height = 20f
  private var hasHitCeiling = false
  private val sprite = Sprite(width, height, Color.paddle)
  sprite.setPosition(position)

  def update(elapsed: Float, x: Int): Unit = {
    position = Vector2(x, position.y)

    if (position.x + width > gameField.x + gameField.width) {
      position = Vector2((gameField.x + gameField.width) - width, position.y)
    }
    if (position.x < gameField.x) {
      position = Vector2(gameField.x, position.y)
    }
    sprite.setPosition(position)
  }

  def draw(): Seq[Sprite] = {
    List(sprite)
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()

    val normal = if (incomingLine.p2.x >= bounds.x + halfWidth && incomingLine.p1.x >= incomingLine.p2.x) {
      // coming from the right, hitting right
      Vector2(1, 1)
    } else if (incomingLine.p2.x <= bounds.x + halfWidth && incomingLine.p1.x <= incomingLine.p2.x) {
      // coming from the left, hitting left
      Vector2(1, -1)
    } else {
      Vector2(1, 0)
    }
    ball.bounce(normal, "paddle")
  }

  def hitCeiling(): Unit = {
    if (!hasHitCeiling) {
      shrink()
      hasHitCeiling = true
    }
  }

  def shrink(): Unit = {
    width = halfWidth
    halfWidth = width / 2f
    sprite.setWidth(width)
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}
