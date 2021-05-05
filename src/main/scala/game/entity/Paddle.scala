package game.entity

import game.audio.Sound
import game.graphics.{Color, Sprite}
import mafs.{Rect, Vector2}

case class Paddle(initialPosition: Vector2, gameField: Rect) extends Hittable {
  private val initialWidth = gameField.width / 9f
  private val height = gameField.height / 50f
  private val sprite = Sprite(initialPosition, initialWidth, height, Color.paddle)
  private val minWidth = initialWidth - (initialWidth / 4f)
  val sprites = Seq(sprite)

  def update(elapsed: Float, x: Float): Unit = {
    if (x == sprite.position.x) {
      return
    }

    sprite.setXPosition(constrainToGameField(x))
  }

  private def constrainToGameField(x: Float): Float = {
    x match {
      case _ if x < gameField.x =>
        gameField.x
      case _ if x + sprite.bounds.width > gameField.x + gameField.width =>
        (gameField.x + gameField.width) - sprite.bounds.width
      case _ => x
    }
  }

  def hitTest(ball: Ball): Boolean = {
    if (contains(ball)) {
      Sound.paddleBeep()
      reflect(ball)
      true
    } else {
      false
    }
  }

  def reflect(ball: Ball): Unit = {
    val bounds: Rect = this.bounds()
    val incomingLine = ball.progressLine()
    val eighthWidth = bounds.width / 8f

    val direction = if (incomingLine.p1.x >= incomingLine.p2.x) {
      // coming from the right
      if (incomingLine.p2.x >= bounds.x + (bounds.width - eighthWidth)) {
        // hitting right edge, will bounce back at more aggressive angle
        //
        //             v
        //            /
        //           /
        // _________*---->
        //
        Vector2(0f, -1f).rotate(60f)
      } else if (incomingLine.p2.x >= bounds.center.x) {
        // hitting right of center, will bounce back
        //
        //             ^ v
        //            / /
        //           / /
        //           *
        //    ---------
        //
        Vector2(0f, -1f).rotate(45f)
      } else {
        // hitting left of center, will bounce forward
        //
        //     ^     v
        //      \   /
        //       \ /
        //        *
        // ---------
        //
        Vector2(-1f, 0f).rotate(45f)
      }
    } else {
      // coming from the left
      if (incomingLine.p2.x <= bounds.x + eighthWidth) {
        // hitting left edge, will bounce back at more aggressive angle
        //
        //   v
        //    \
        //     \
        // <--- *_________
        //
        Vector2(-1f, 0f).rotate(30f)
      } else if (incomingLine.p2.x <= bounds.center.x) {
        // hitting left of center, will bounce back
        //
        // v ^
        //  \ \
        //   \ \
        //     *
        //    ---------
        //
        Vector2(-1f, 0f).rotate(45f)
      } else {
        // hitting right of center, will bounce forward
        //
        //     v     ^
        //      \   /
        //       \ /
        //        *
        // ---------
        //
        Vector2(0f, -1f).rotate(45f)
      }
    }

    ball.redirect(direction, EntityType.Paddle)
  }

  def hitCeiling(): Unit = {
    sprite.setWidth(minWidth)
  }

  def bounds(): Rect = sprite.bounds
}
