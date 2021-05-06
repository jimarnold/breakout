package mafs

import org.scalatest.funsuite.AnyFunSuite

class LineSpec extends AnyFunSuite {
  test("two lines intersect") {
    val l1 = Line(Point(0f, 0f), Point(2f, 0f))
    val l2 = Line(Point(1f, 1f), Point(1f, -1f))

    assert(l1.intersectsWith(l2))
  }
  test("two lines do not intersect") {
    val l1 = Line(Point(0f, 0f), Point(0.99f, 0f))
    val l2 = Line(Point(1f, 1f), Point(1f, -1f))

    assert(!l1.intersectsWith(l2))

    val l3 = Line(Point(54.0f,65.0f), Point(1026.0f,65.0f))
    val l4 = Line(Point(972.0f,288.0f), Point(623.2096f,636.7906f))

    assert(!l3.intersectsWith(l4))
  }
}
