package game.entity

import game.audio.Sound
import game.graphics.{Color, Sprite}
import game.graphics
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
  val sprites = Seq(leftSprite, rightSprite, ceilingSprite)

  def hitTest(ball: Ball): SideHitResult = {
    if (leftSide.intersectsWith(ball.progressLine()) ||
      rightSide.intersectsWith(ball.progressLine())) {
      val normal = Vector2(0, 1)
      ball.bounce(normal, EntityType.Sides)
      Sound.sideBeep()
      SideHitResult.Side
    } else if (ceiling.intersectsWith(ball.progressLine())) {
      val normal = Vector2(1, 0)
      ball.bounce(normal, EntityType.Ceiling)
      Sound.topBeep()
      SideHitResult.Ceiling
    } else {
      SideHitResult.None
    }
  }

}
