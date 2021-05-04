package mafs

import org.scalatest.funsuite.AnyFunSuite

class LineSpec extends AnyFunSuite {
  test("two lines intersect") {
    val l1 = Line(Vector2(0f, 0f), Vector2(2f, 0f))
    val l2 = Line(Vector2(1f, 1f), Vector2(1f, -1f))

    assert(l1.intersectsWith(l2))
  }
  test("two lines do not intersect") {
    val l1 = Line(Vector2(0f, 0f), Vector2(0.99f, 0f))
    val l2 = Line(Vector2(1f, 1f), Vector2(1f, -1f))

    assert(!l1.intersectsWith(l2))

    val l3 = Line(Vector2(54.0f,65.0f), Vector2(1026.0f,65.0f))
    val l4 = Line(Vector2(972.0f,288.0f), Vector2(623.2096f,636.7906f))

    assert(!l3.intersectsWith(l4))
  }
}
