package mafs

import scala.math.sqrt

case class Vector2(x: Float, y: Float) {
  def magnitude(): Float = {
    sqrt((this.x * this.x) + (this.y * this.y)).toFloat
  }

  def mult(b: Float): Vector2 = {
    Vector2(this.x * b, this.y * b)
  }

  def divide(m: Float): Vector2 = {
    Vector2(this.x / m, this.y / m)
  }

  def plus(v: Vector2): Vector2 = {
    Vector2(this.x + v.x, this.y + v.y)
  }

  def minus(v: Vector2): Vector2 = {
    Vector2(this.x - v.x, this.y - v.y)
  }

  def normalize(): Vector2 = {
    val m = this.magnitude()
    if (m > 0) this.divide(m) else Vector2(0, 0)
  }

  def dot(v: Vector2): Float = {
    this.x * v.x + this.y * v.y
  }

  def reflect(n: Vector2): Vector2 = {
    this.minus(n.mult(2 * this.dot(n))).reverse()
  }

  def project(onto: Vector2): Vector2 = {
    val numerator = this.dot(onto)
    val denominator = onto.dot(onto)
    onto.mult(numerator / denominator)
  }

  def reverse(): Vector2 = {
    Vector2(-this.x, -this.y)
  }

  def rotate(degrees: Float): Vector2 = {
    val theta = Math.toRadians(degrees)
    val cs = Math.cos(theta).toFloat
    val sn = Math.sin(theta).toFloat
    Vector2(x * cs - y * sn, x * sn + y * cs)
  }

  def angleBetween(v: Vector2): Double = {
    val dot = this.dot(v)
    val mag1 = this.magnitude()
    val mag2 = v.magnitude()

    Math.toDegrees(Math.acos(dot / (mag1 * mag2)))
  }

  def to4: Vector4 = Vector4(x, y, 0f, 1f)

  override def toString: String = {
    s"$x,$y"
  }
}
