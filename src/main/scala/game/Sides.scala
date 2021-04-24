package game

import mafs.{Rect, Vector}
import sdl.Canvas

class Sides(val screenWidth: Int, val screenHeight: Int) {
  val width: Int = screenWidth / 20
  private val leftSide = Rect(0, width, width, screenHeight)
  private val rightSide = Rect(screenWidth - width, width, width, screenHeight)
  private val ceiling = Rect(width, width, screenWidth - (2 * width), width)

  def hitTest(ball: Ball): Boolean = {
    if (leftSide.isOverlapping(ball.bounds()) ||
      rightSide.isOverlapping(ball.bounds())) {
      val normal = Vector(0, 1)
      ball.bounce(normal, "side")
      Sound.sideBeep()
      true
    } else if (ceiling.isOverlapping(ball.bounds())) {
      val normal = Vector(1, 0)
      ball.bounce(normal, "ceiling")
      Sound.topBeep()
      true
    } else {
      false
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(Color.grey)
    canvas.drawRect(ceiling)
    canvas.drawRect(leftSide)
    canvas.drawRect(rightSide)
  }
}
