package game.entity

import game.graphics.{Color, Sprite}
import mafs.{Point, Rect, Vector2}

class ScoreBoard {
  import game.entity.Number._

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

  private val firstDigit = Digit(Point(100, 10), zero)
  private val secondDigit = Digit(Point(200, 10), zero)
  private val thirdDigit = Digit(Point(300, 10), zero)
  private val lifeDigit = Digit(Point(600, 10), five)

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

case class Digit(position: Point, segments: Seq[Rect]) {
  var sprites: Seq[Sprite] = toSprites(segments)

  def set(segments: Seq[Rect]): Unit = {
    this.sprites = toSprites(segments)
  }

  def toSprites(segments: Seq[Rect]): Seq[Sprite] = {
    segments.map(s => {
      Sprite(s.translate(Vector2(position.x, position.y)), Color.grey)
    })
  }
}

object Number {
  private val width = 80
  private val height = 40
  private val halfHeight = height / 2
  private val halfWidth = width / 2
  private val narrow = 8
  private val halfNarrow = narrow / 2
  private val wide = narrow * 3

  private val top = Rect(0, 0, width, narrow)
  private val upperLeft = Rect(0, 0, wide, halfHeight + halfNarrow)
  private val upperMiddle = Rect(halfWidth, 0, wide, halfHeight)
  private val upperRight = Rect(width - wide, 0, wide, halfHeight + halfNarrow)
  private val middle = Rect(wide, halfHeight - halfNarrow, width - wide, narrow)
  private val lowerLeft = Rect(0, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  private val lowerMiddle = Rect(halfWidth, halfHeight, wide, halfHeight)
  private val lowerRight = Rect(width - wide, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  private val bottom = Rect(0, height - narrow, width, narrow)

  val zero = Seq(top, upperLeft, upperRight, lowerLeft, lowerRight, bottom)
  val one = Seq(upperMiddle, lowerMiddle)
  val two = Seq(top, upperRight, middle, lowerLeft, bottom)
  val three = Seq(top, upperRight, middle, lowerRight, bottom)
  val four = Seq(upperLeft, upperRight, middle, lowerRight)
  val five = Seq(top, upperLeft, middle, lowerRight, bottom)
  val six = Seq(top, upperLeft, middle, lowerLeft, lowerRight, bottom)
  val seven = Seq(top, upperRight, lowerRight)
  val eight = Seq(top, upperLeft, upperRight, middle, lowerLeft, lowerRight, bottom)
  val nine = Seq(top, upperLeft, upperRight, middle, lowerRight)
}
