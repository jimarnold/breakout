package game

import mafs.Rect
import sdl.Canvas
import scala.collection.mutable.ArrayBuffer

class Wall(val gameField: Rect) {
  private var bricks: List[Brick] = _
  private val columns = 17 // 0-based
  private val removed = ArrayBuffer.empty[Brick]
  private val yOffset = 20
  private val brickWidth = gameField.width / 18f
  private val brickHeight = 20

  private var brickSeq = IndexedSeq.empty[Brick]

  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 5 * yOffset, brickWidth, brickHeight), Color.one) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 6 * yOffset, brickWidth, brickHeight), Color.two) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 7 * yOffset, brickWidth, brickHeight), Color.three) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 8 * yOffset, brickWidth, brickHeight), Color.four) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 9 * yOffset, brickWidth, brickHeight), Color.five) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(gameField.x + (e * brickWidth), 10 * yOffset, brickWidth, brickHeight), Color.six) }
  bricks = brickSeq.toList

  def draw(canvas: Canvas): Unit = {
    bricks.foreach(brick => brick.draw(canvas))
  }

  def hitTest(ball: Ball): Unit = {
    if (ball.position.y < 220) {
      bricks.foreach(brick => {
        if (brick.contains(ball.bounds())) {
          brick.reflect(ball)
          removed += brick
        }
      })
      bricks = bricks diff removed.distinct
    }
  }

  def hits: Int = removed.size

  def isDestroyed: Boolean = bricks.isEmpty
}
