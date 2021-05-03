package game.entity

import game.graphics.{Color, Sprite}
import mafs.{Rect, Vector2}

case class Digit(position: Vector2, segments: Seq[Rect]) {
  var sprites: Seq[Sprite] = toSprites(segments)

  def set(segments: Seq[Rect]): Unit = {
    this.sprites = toSprites(segments)
  }

  def toSprites(segments: Seq[Rect]): Seq[Sprite] = {
    segments.map(s => {
      Sprite(s.translate(position), Color.grey)
    })
  }
}

class ScoreBoard {
  val zero = List(Segment.top, Segment.upperLeft, Segment.upperRight, Segment.lowerLeft, Segment.lowerRight, Segment.bottom)
  val one = List(Segment.upperMiddle, Segment.lowerMiddle)
  val two = List(Segment.top, Segment.upperRight, Segment.middle, Segment.lowerLeft, Segment.bottom)
  val three = List(Segment.top, Segment.upperRight, Segment.middle, Segment.lowerRight, Segment.bottom)
  val four = List(Segment.upperLeft, Segment.upperRight, Segment.middle, Segment.lowerRight)
  val five = List(Segment.top, Segment.upperLeft, Segment.middle, Segment.lowerRight, Segment.bottom)
  val six = List(Segment.top, Segment.upperLeft, Segment.middle, Segment.lowerLeft, Segment.lowerRight, Segment.bottom)
  val seven = List(Segment.top, Segment.upperRight, Segment.lowerRight)
  val eight = List(Segment.top, Segment.upperLeft, Segment.upperRight, Segment.middle, Segment.lowerLeft, Segment.lowerRight, Segment.bottom)
  val nine = List(Segment.top, Segment.upperLeft, Segment.upperRight, Segment.middle, Segment.lowerRight)

  private val numbers = Array(
    zero,
    one,
    two,
    three,
    four,
    five,
    six,
    seven,
    eight,
    nine)

  private val firstDigit = Digit(Vector2(100, 10), zero)
  private val secondDigit = Digit(Vector2(200, 10), zero)
  private val thirdDigit = Digit(Vector2(300, 10), zero)
  private val lifeDigit = Digit(Vector2(600, 10), five)

  var score = 0
  var livesRemaining = 5

  def lifeLost(): Unit = {
    livesRemaining -= 1
    lifeDigit.set(numbers(livesRemaining))
  }

  def increment(amount: Int): Unit = {
    score += amount
    firstDigit.set(numbers((score / 100) % 10))
    secondDigit.set(numbers((score / 10) % 10))
    thirdDigit.set(numbers(score % 10))
  }

  def sprites(): Seq[Sprite] = {
    firstDigit.sprites ++
    secondDigit.sprites ++
    thirdDigit.sprites ++
    lifeDigit.sprites
  }
}

object Segment {
  private val width = 80
  private val height = 40
  private val halfHeight = height / 2
  private val halfWidth = width / 2
  private val narrow = 8
  private val halfNarrow = narrow / 2
  private val wide = narrow * 3

  val top: Rect = Rect(0, 0, width, narrow)
  val upperLeft: Rect = Rect(0, 0, wide, halfHeight + halfNarrow)
  val upperMiddle: Rect = Rect(halfWidth, 0, wide, halfHeight)
  val upperRight: Rect = Rect(width - wide, 0, wide, halfHeight + halfNarrow)
  val middle: Rect = Rect(wide, halfHeight - halfNarrow, width - wide, narrow)
  val lowerLeft: Rect = Rect(0, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  val lowerMiddle: Rect = Rect(halfWidth, halfHeight, wide, halfHeight)
  val lowerRight: Rect = Rect(width - wide, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  val bottom: Rect = Rect(0, height - narrow, width, narrow)
}
