package game

import mafs.{Rect, Vector2}

object SideHitResult extends Enumeration {
  val Side, Ceiling, None = Value
  type SideHitResult = Value
}

import game.SideHitResult.SideHitResult

class Sides(val screenWidth: Int, val screenHeight: Int) {
  val width: Int = screenWidth / 20
  private val leftSide = Rect(0, 60, width, screenHeight)
  private val rightSide = Rect(screenWidth - width, 60, width, screenHeight)
  private val ceiling = Rect(width, 60, screenWidth - (2 * width), width)
  private val leftSprite = Sprite(leftSide.width, leftSide.height, Color.grey)
  private val rightSprite = Sprite(rightSide.width, rightSide.height, Color.grey)
  private val ceilingSprite = Sprite(ceiling.width, ceiling.height, Color.grey)
  leftSprite.setPosition(Vector2(leftSide.x, leftSide.y))
  rightSprite.setPosition(Vector2(rightSide.x, rightSide.y))
  ceilingSprite.setPosition(Vector2(ceiling.x, ceiling.y))

  def hitTest(ball: Ball): SideHitResult = {
    if (leftSide.isOverlapping(ball.bounds()) ||
      rightSide.isOverlapping(ball.bounds())) {
      val normal = Vector2(0, 1)
      ball.bounce(normal, "side")
      Sound.sideBeep()
      SideHitResult.Side
    } else if (ceiling.isOverlapping(ball.bounds())) {
      val normal = Vector2(1, 0)
      ball.bounce(normal, "ceiling")
      Sound.topBeep()
      SideHitResult.Ceiling
    } else {
      SideHitResult.None
    }
  }

  def draw(): Seq[Sprite] = {
    List(leftSprite, rightSprite, ceilingSprite)
  }
}
