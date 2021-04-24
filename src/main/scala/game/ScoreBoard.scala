package game

import mafs.{Rect, Vector}
import sdl.Canvas

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

  var digits = Map(
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

  var first: Seq[Rect] = zero
  var second: Seq[Rect] = zero
  var third: Seq[Rect] = zero
  var lifeDigit: Seq[Rect] = five
  var score = 0

  def increment(): Unit = {
    score += 1
    val scoreString = score.toString
    if (scoreString.length == 1) {
      third = digits.getOrElse(scoreString.charAt(0), zero)
    }
    if (scoreString.length == 2) {
      second = digits.getOrElse(scoreString.charAt(0), zero)
      third = digits.getOrElse(scoreString.charAt(1), zero)
    }
    if (scoreString.length == 3) {
      first = digits.getOrElse(scoreString.charAt(0), zero)
      second = digits.getOrElse(scoreString.charAt(1), zero)
      third = digits.getOrElse(scoreString.charAt(2), zero)
    }
  }

  def setLives(lives: Int): Unit = {
    lifeDigit = digits.getOrElse(lives.toString.charAt(0), zero)
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(Color.grey)
    first.foreach(s => canvas.drawRect(s.transform(Vector(100, 10))))
    second.foreach(s => canvas.drawRect(s.transform(Vector(200, 10))))
    third.foreach(s => canvas.drawRect(s.transform(Vector(300, 10))))
    lifeDigit.foreach(s => canvas.drawRect(s.transform(Vector(600, 10))))
  }
}

class Digit(segments: Seq[Rect], position: Vector) {
  def draw(canvas: Canvas): Unit = {
    segments.foreach(s => canvas.drawRect(s.transform(position)))
  }
}

object Segment {
  val width = 80
  val height = 30
  val halfHeight = height / 2
  val halfWidth = width / 2
  val narrow = 8
  val halfNarrow = narrow / 2
  val wide = narrow * 4

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
