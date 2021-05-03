package mafs

import org.scalatest.funsuite.AnyFunSuite

class Vector2Spec extends AnyFunSuite {
  test("magnitude") {
    val v = Vector2(5f,5f)

    assert(v.magnitude() == 7.071068f)
  }

  test("normalize") {
    val v = Vector2(1f,1f)

    val unit = v.normalize()
    assert(unit.magnitude() > 0.9999f && unit.magnitude() < 1.0001f)
    assert(unit.x == 0.70710677f)
    assert(unit.y == 0.70710677f)
  }

  test("reflect") {
    val v = Vector2(1f,1f)
    val normal = Vector2(0f, 1f)
    val v1 = v.reflect(normal)
    assert(v1.x == -1f)
    assert(v1.y == 1f)
  }

  test("rotate") {
    val v = Vector2(1f,1f)
    val v1 = v.rotate(90f)
    assert(v1.x == -1f)
    assert(v1.y == 1f)
  }

  test("rotateClockwise") {
    val v = Vector2(1f,1f)
    val v1 = v.rotateCounterClockwise(90f)
    assert(v1.x == 1f)
    assert(v1.y == -1f)
  }
}
