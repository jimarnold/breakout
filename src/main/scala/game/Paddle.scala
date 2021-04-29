package game

import game.entities.{EntityType, Hittable}
import mafs.{Rect, Vector2}

class Paddle(var position: Vector2, gameField: Rect) extends Hittable {
  private var width = gameField.width / 8f
  private var halfWidth = width / 2f
  private val height = gameField.height / 50f
  private var hasHitCeiling = false
  private val sprite = Sprite(position.x, position.y, width, height, Color.paddle)

  def update(elapsed: Float, x: Int): Unit = {
    if (x == position.x) {
      return
    }

    position = Vector2(x, position.y)

    if (position.x + width > gameField.x + gameField.width) {
      position = Vector2((gameField.x + gameField.width) - width, position.y)
    }
    if (position.x < gameField.x) {
      position = Vector2(gameField.x, position.y)
    }
    sprite.setPosition(position)
  }

  def sprites(): Seq[Sprite] = {
    List(sprite)
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()

    val normal = if (incomingLine.p2.x >= bounds.x + halfWidth && incomingLine.p1.x >= incomingLine.p2.x) {
      // coming from the right, hitting right, will bounce back
      //
      //             ^ v
      //            / /
      //           / /
      //           *
      //    ---------
      //
      Vector2(1, 1)
    } else if (incomingLine.p2.x <= bounds.x + halfWidth && incomingLine.p1.x <= incomingLine.p2.x) {
      // coming from the left, hitting left, will bounce back
      //
      // v ^
      //  \ \
      //   \ \
      //     *
      //    ---------
      //
      Vector2(1, -1)
    } else {
      // will bounce forward
      //
      //     v     ^
      //      \   /
      //       \ /
      //        *
      // ---------
      //
      Vector2(1, 0)
    }
    ball.bounce(normal, EntityType.Paddle)
  }

  def hitCeiling(): Unit = {
    if (!hasHitCeiling) {
      shrink()
      hasHitCeiling = true
    }
  }

  def shrink(): Unit = {
    sprite.setWidth(width - (width / 3f))
  }

  def bounds(): Rect = {
    sprite.bounds
  }
}
