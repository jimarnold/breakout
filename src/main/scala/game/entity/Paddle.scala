package game.entity

import game.graphics.{Color, Sprite}
import mafs.{Rect, Vector2}

case class Paddle(initialPosition: Vector2, gameField: Rect) extends Hittable {
  private val initialWidth = gameField.width / 8f
  private val height = gameField.height / 50f
  private val sprite = Sprite(initialPosition, initialWidth, height, Color.paddle)
  private val minWidth = initialWidth - (initialWidth / 3f)

  def update(elapsed: Float, x: Int): Unit = {
    if (x == sprite.position.x) {
      return
    }

    sprite.setXPosition(constrainToGameField(x))
  }

  private def constrainToGameField(x: Int): Float = {
    x match {
      case _ if x < gameField.x =>
        gameField.x
      case _ if x + initialWidth > gameField.x + gameField.width =>
        (gameField.x + gameField.width) - initialWidth
      case _ => x
    }
  }

  def sprites(): Seq[Sprite] = {
    List(sprite)
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()
    val halfWidth = bounds.width / 2f

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
    sprite.setWidth(minWidth)
  }

  def bounds(): Rect = {
    sprite.bounds
  }
}
