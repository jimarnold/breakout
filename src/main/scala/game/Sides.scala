package game

import mafs.{Rect, Vector}
import sdl.Canvas

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

  def hitTest(ball: Ball): SideHitResult = {
    if (leftSide.isOverlapping(ball.bounds()) ||
      rightSide.isOverlapping(ball.bounds())) {
      val normal = Vector(0, 1)
      ball.bounce(normal, "side")
      Sound.sideBeep()
      SideHitResult.Side
    } else if (ceiling.isOverlapping(ball.bounds())) {
      val normal = Vector(1, 0)
      ball.bounce(normal, "ceiling")
      Sound.topBeep()
      SideHitResult.Ceiling
    } else {
      SideHitResult.None
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(Color.grey)
    canvas.drawRect(ceiling)
    canvas.drawRect(leftSide)
    canvas.drawRect(rightSide)
  }
}
