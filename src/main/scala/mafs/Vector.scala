package mafs

import scala.math.sqrt

case class Vector(x: Float, y: Float) {
  def magnitude(): Float = {
    sqrt((this.x * this.x) + (this.y * this.y)).toFloat
  }

  def mult(b: Float): Vector = {
    Vector(this.x * b, this.y * b)
  }

  def divide(m: Float): Vector = {
    Vector(this.x / m, this.y / m)
  }

  def plus(v: Vector): Vector = {
    Vector(this.x + v.x, this.y + v.y)
  }

  def minus(v: Vector): Vector = {
    Vector(this.x - v.x, this.y - v.y)
  }

  def normalize(): Vector = {
    val m = this.magnitude()
    if (m > 0) this.divide(m) else Vector(0, 0)
  }

  def dot(v: Vector): Float = {
    this.x * v.x + this.y * v.y
  }

  def reflect(n: Vector): Vector = {
    this.minus(n.mult(2 * this.dot(n))).reverse()
  }

  def project(onto: Vector): Vector = {
    val numerator = this.dot(onto)
    val denominator = onto.dot(onto)
    onto.mult(numerator / denominator)
  }

  def reverse(): Vector = {
    Vector(-this.x, -this.y)
  }

  def rotate(degrees: Float): Vector = {
    val theta = Math.toRadians(degrees)
    val cs = Math.cos(theta).toFloat
    val sn = Math.sin(theta).toFloat
    Vector(x * cs - y * sn, x * sn + y * cs)
  }

  def angleBetween(v: Vector): Double = {
    val dot = this.dot(v)
    val mag1 = this.magnitude()
    val mag2 = v.magnitude()

    Math.toDegrees(Math.acos(dot / (mag1 * mag2)))
  }

  override def toString(): String = {
    s"${x.toInt},${y.toInt}"
  }
}
