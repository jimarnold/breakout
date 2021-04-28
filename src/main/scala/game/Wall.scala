package game

import mafs.Rect
import sdl.RGB

import scala.collection.mutable.ArrayBuffer

class Wall(val gameField: Rect, val scoreboard: ScoreBoard) {
  private var bricks: List[Brick] = _
  private val columns = 17 // 0-based
  private val removed = ArrayBuffer.empty[Brick]
  private val brickWidth = gameField.width / 18f
  private val brickHeight = 20
  private val initialHeight = gameField.y + 30
  private var brickSeq = IndexedSeq.empty[Brick]

  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight , brickWidth, brickHeight), this) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight + brickHeight, brickWidth, brickHeight), this) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight + (2*brickHeight), brickWidth, brickHeight), this) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight + (3*brickHeight), brickWidth, brickHeight), this) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight + (4*brickHeight), brickWidth, brickHeight), this) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), initialHeight + (5*brickHeight), brickWidth, brickHeight), this) }
  bricks = brickSeq.toList
  val lowerBound = bricks.last.bounds().bottomRight.y

  def draw(): Seq[Sprite] = {
    bricks.map(_.draw())
  }

  def hitTest(ball: Ball): Boolean = {
    var hit = false

    if (ball.position.y < lowerBound) {
      bricks.foreach(brick => {
        if (!hit && brick.contains(ball.bounds())) {
          Sound.brickBeep(brick.color)
          brick.reflect(ball)
          removed += brick
          hit = true
          scoreboard.increment(getPoints(brick.bounds.y))
        }
      })
      bricks = bricks diff removed.distinct
    }
    hit
  }

  def hits: Int = removed.size

  def isDestroyed: Boolean = bricks.isEmpty

  def getColor(y: Float): RGB = {
    y match {
      case y if y < (initialHeight) =>
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
