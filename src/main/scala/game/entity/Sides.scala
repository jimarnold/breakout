package game.entity

import game.audio.Sound
import game.graphics.{Color, Sprite}
import mafs.{Rect, Vector2}

object SideHitResult extends Enumeration {
  val Side, Ceiling, None = Value
  type SideHitResult = Value
}

import game.entity.SideHitResult.SideHitResult

class Sides(val screenWidth: Int, val screenHeight: Int) {
  val width: Float = screenWidth / 20f
  private val yOffset = screenHeight / 12f
  private val leftSide = Rect(0, yOffset, width, screenHeight)
  private val rightSide = Rect(screenWidth - width, yOffset, width, screenHeight)
  private val ceiling = Rect(width, yOffset, screenWidth - (2 * width), width)
  private val leftSprite = Sprite(leftSide, Color.grey)
  private val rightSprite = Sprite(rightSide, Color.grey)
  private val ceilingSprite = Sprite(ceiling, Color.grey)

  val ceilingLowerY: Float = (screenHeight / 12f) + width
  val sprites = Seq(leftSprite, rightSprite, ceilingSprite)
  val innerArea: Rect = Rect(width, width * 3, screenWidth - (width * 2), screenHeight - width * 3)

  def hitTest(ball: Ball): SideHitResult = {
    val ballBounds = ball.bounds()
    if (leftSide.isOverlapping(ballBounds)) {
      val normal = Vector2(0, 1)
      ball.bounce(normal, EntityType.LeftSide)
      Sound.sideBeep()
      SideHitResult.Side
    } else if (rightSide.isOverlapping(ballBounds)) {
      val normal = Vector2(0, 1)
      ball.bounce(normal, EntityType.RightSide)
      Sound.sideBeep()
      SideHitResult.Side
    } else if (ceiling.isOverlapping(ballBounds)) {
      val normal = Vector2(1, 0)
      ball.bounce(normal, EntityType.Ceiling)
      Sound.ceilingBeep()
      SideHitResult.Ceiling
    } else {
      SideHitResult.None
    }
  }
}
