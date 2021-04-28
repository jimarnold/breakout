package mafs

import org.scalatest.funsuite.AnyFunSuite

class MatrixSpec extends AnyFunSuite {

  test("Translation matrix should move vector") {
    val matrix = Matrix4.translation(Vector4(1f,2f,3f,1f))
    val v = Vector4(2f, 3f, 4f, 1f)

    val result = matrix.mult(v)
    assert(result.x == 3f)
    assert(result.y == 5f)
    assert(result.z == 7f)
    assert(result.w == 1f)
  }

  test("Orthographic projection matrix puts 0,0 at top left") {
    val matrix = Matrix4.ortho(0, 500, 400, 0, -1, 1)
    val v = Vector4(0.0f, 0.0f, 0f, 1f)

    val result = matrix.mult(v)

    assert(result.x == -1f)
    assert(result.y == 1f)
  }

  test("Orthographic projection matrix puts middle, middle at 0,0") {
    val matrix = Matrix4.ortho(0, 500, 400, 0, -1, 1)
    val v = Vector4(250.0f, 200.0f, 0f, 1f)

    val result = matrix.mult(v)

    assert(result.x == 0.0f)
    assert(result.y == 0.0f)
  }

  test("Orthographic projection matrix puts bottom right at 1,-1") {
    val matrix = Matrix4.ortho(0, 500, 400, 0, -1, 1)
    val v = Vector4(500.0f, 400.0f, 0f, 1f)

    val result = matrix.mult(v)

    assert(result.x == 1.0f)
    assert(result.y == -1.0f)
  }
}
