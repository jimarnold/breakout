package game.entity

import game.audio.Sound
import game.graphics.{Color, Sprite}
import mafs.Rect

import scala.collection.mutable.ArrayBuffer

class Wall(val gameField: Rect, val scoreboard: ScoreBoard) {
  private val rows = 6
  private val columns = 18
  private val brickWidth = gameField.width / columns
  private val brickHeight = brickWidth / 3
  private val initialHeight = gameField.y + brickHeight
  private val bricks: ArrayBuffer[Brick] = ArrayBuffer.empty[Brick]
  val lowerBound: Float = (rows * brickHeight) + initialHeight

  (0 until rows).map { i =>
    (0 until columns).map { j =>
      val y = initialHeight + (i * brickHeight)
      bricks += Brick(
        Rect(gameField.x + (j * brickWidth), y, brickWidth, brickHeight),
        getColor(y))
    }
  }

  def sprites(): Seq[Sprite] = bricks.map(_.sprite)

  def hitTest(ball: Ball): Boolean = {
    if (ball.position.y >= lowerBound)
      false
    else
      bricks.find(_.contains(ball)) match {
        case Some(brick) =>
          Sound.beep(brick.color)
          brick.reflect(ball)
          bricks -= brick
          scoreboard.increment(getPoints(brick.bounds().y))
          true
        case None => false
      }
  }

  def hits: Int = (rows * columns) - bricks.size

  def isDestroyed: Boolean = bricks.isEmpty

  def getColor(y: Float): Color = {
    y match {
      case y if y < initialHeight =>
        Color.grey
      case y if y < (initialHeight + brickHeight) =>
        Color.one
      case y if y < (initialHeight + brickHeight * 2) =>
        Color.two
      case y if y < (initialHeight + brickHeight * 3) =>
        Color.three
      case y if y < (initialHeight + brickHeight * 4) =>
        Color.four
      case y if y < (initialHeight + brickHeight * 5) =>
        Color.five
      case y if y < (initialHeight + brickHeight * 6) =>
        Color.six
      case _ =>
        Color.ball
    }
  }

  def getPoints(y: Float): Int = {
    y match {
      case y if y < (initialHeight + brickHeight * 2) =>
        7
      case y if y < (initialHeight + brickHeight * 4) =>
        4
      case _ =>
        1
    }
  }
}
