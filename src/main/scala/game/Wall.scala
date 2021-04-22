package game

import mafs.Rect
import sdl.{Canvas, RGB}
import scala.collection.mutable.ArrayBuffer

class Wall {
  private var bricks: List[Brick]     = _
  private val brickColor1: RGB        = RGB(200, 104, 85)
  private val brickColor2: RGB        = RGB(200, 130, 75)
  private val brickColor3: RGB        = RGB(185, 135, 65)
  private val brickColor4: RGB        = RGB(170, 165, 65)
  private val brickColor5: RGB        = RGB(95, 155, 85)
  private val brickColor6: RGB        = RGB(75, 85, 200)
  private val columns = 17 // 0-based
  private val removed = ArrayBuffer.empty[Brick]

  val xOffset = 50
  val yOffset = 20
  val brickWidth = 50
  val brickHeight = 20

  private var brickSeq = IndexedSeq.empty[Brick]
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 2 * yOffset, brickWidth, brickHeight), brickColor1) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 3 * yOffset, brickWidth, brickHeight), brickColor2) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 4 * yOffset, brickWidth, brickHeight), brickColor3) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 5 * yOffset, brickWidth, brickHeight), brickColor4) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 6 * yOffset, brickWidth, brickHeight), brickColor5) }
  brickSeq ++= (0 to columns).map { e => new Brick(Rect(e * xOffset, 7 * yOffset, brickWidth, brickHeight), brickColor6) }
  bricks = brickSeq.toList

  def draw(canvas: Canvas): Unit = {
    bricks.foreach(brick => brick.draw(canvas))
  }

  def hitTest(ball: Ball): Unit = {
    if (ball.position.y < 160) {
      bricks.foreach(brick => {
        if (brick.contains(ball.bounds())) {
          brick.reflect(ball)
          removed += brick
        }
      })
      bricks = bricks diff removed.distinct
    }
  }

  def hits(): Int = {
    removed.size
  }
}
