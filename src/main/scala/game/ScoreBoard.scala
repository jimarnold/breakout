package game

import mafs.{Rect, Vector2}

import scala.collection.mutable

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

  var numbers = Map(
    '0' -> zero,
    '1' -> one,
    '2' -> two,
    '3' -> three,
    '4' -> four,
    '5' -> five,
    '6' -> six,
    '7' -> seven,
    '8' -> eight,
    '9' -> nine)

  var first: Digit = Digit(Vector2(100, 10), zero)
  var second: Digit = Digit(Vector2(200, 10), zero)
  var third: Digit = Digit(Vector2(300, 10), zero)
  var lifeDigit: Digit = Digit(Vector2(600, 10), five)
  var score = 0

  def increment(amount: Int): Unit = {
    score += amount
    val scoreString = score.toString
    if (scoreString.length == 1) {
      third.set(numbers.getOrElse(scoreString.charAt(0), zero))
    }
    if (scoreString.length == 2) {
      second.set(numbers.getOrElse(scoreString.charAt(0), zero))
      third.set(numbers.getOrElse(scoreString.charAt(1), zero))
    }
    if (scoreString.length == 3) {
      first.set(numbers.getOrElse(scoreString.charAt(0), zero))
      second.set(numbers.getOrElse(scoreString.charAt(1), zero))
      third.set(numbers.getOrElse(scoreString.charAt(2), zero))
    }
  }

  def setLives(lives: Int): Unit = {
    lifeDigit.set(numbers.getOrElse(lives.toString.charAt(0), zero))
  }

  def sprites(): Seq[Sprite] = {
    val sprites = mutable.MutableList.empty[Sprite]
    sprites ++= first.sprites
    sprites ++= second.sprites
    sprites ++= third.sprites
    sprites ++= lifeDigit.sprites
  }
}

object Segment {
  val width = 80
  val height = 40
  val halfHeight = height / 2
  val halfWidth = width / 2
  val narrow = 8
  val halfNarrow = narrow / 2
  val wide = narrow * 3

  val top = Rect(0, 0, width, narrow)
  val upperLeft = Rect(0, 0, wide, halfHeight + halfNarrow)
  val upperMiddle = Rect(halfWidth, 0, wide, halfHeight)
  val upperRight = Rect(width - wide, 0, wide, halfHeight + halfNarrow)
  val middle = Rect(wide, halfHeight - halfNarrow, width - wide, narrow)
  val lowerLeft = Rect(0, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  val lowerMiddle = Rect(halfWidth, halfHeight, wide, halfHeight)
  val lowerRight = Rect(width - wide, halfHeight - halfNarrow, wide, halfHeight + halfNarrow)
  val bottom = Rect(0, height - narrow, width, narrow)

}
