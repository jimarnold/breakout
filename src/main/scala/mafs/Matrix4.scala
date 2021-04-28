package mafs

case class Matrix4(_0: Vector4, _1: Vector4, _2: Vector4, _3: Vector4) {
  def toa(): Array[Float] = {
    Array(
      this._0.x,
      this._1.x,
      this._2.x,
      this._3.x,
      this._0.y,
      this._1.y,
      this._2.y,
      this._3.y,
      this._0.z,
      this._1.z,
      this._2.z,
      this._3.z,
      this._0.w,
      this._1.w,
      this._2.w,
      this._3.w)
  }

  def mult (v: Vector4): Vector4 = {
    Vector4(
      this._0.x * v.x + this._0.y * v.y + this._0.z * v.z + this._0.w * v.w,
      this._1.x * v.x + this._1.y * v.y + this._1.z * v.z + this._1.w * v.w,
      this._2.x * v.x + this._2.y * v.y + this._2.z * v.z + this._2.w * v.w,
      this._3.x * v.x + this._3.y * v.y + this._3.z * v.z + this._3.w * v.w
    )
  }

  def mult (b: Matrix4): Matrix4 = {
    val a0 = this._0
    val a1 = this._1
    val a2 = this._2
    val a3 = this._3
    val b0 = b._0
    val b1 = b._1
    val b2 = b._2
    val b3 = b._3

    Matrix4(
      Vector4(
        a0.x*b0.x + a1.x*b0.y + a2.x*b0.z + a3.x*b0.w,
        a0.y*b0.x + a1.y*b0.y + a2.y*b0.z + a3.y*b0.w,
        a0.z*b0.x + a1.z*b0.y + a2.z*b0.z + a3.z*b0.w,
        a0.w*b0.x + a1.w*b0.y + a2.w*b0.z + a3.w*b0.w),
      Vector4(
        a0.x*b1.x + a1.x*b1.y + a2.x*b1.z + a3.x*b1.w,
        a0.y*b1.x + a1.y*b1.y + a2.y*b1.z + a3.y*b1.w,
        a0.z*b1.x + a1.z*b1.y + a2.z*b1.z + a3.z*b1.w,
        a0.w*b1.x + a1.w*b1.y + a2.w*b1.z + a3.w*b1.w),
      Vector4(
        a0.x*b2.x + a1.x*b2.y + a2.x*b2.z + a3.x*b2.w,
        a0.y*b2.x + a1.y*b2.y + a2.y*b2.z + a3.y*b2.w,
        a0.z*b2.x + a1.z*b2.y + a2.z*b2.z + a3.z*b2.w,
        a0.w*b2.x + a1.w*b2.y + a2.w*b2.z + a3.w*b2.w),
      Vector4(
        a0.x*b3.x + a1.x*b3.y + a2.x*b3.z + a3.x*b3.w,
        a0.y*b3.x + a1.y*b3.y + a2.y*b3.z + a3.y*b3.w,
        a0.z*b3.x + a1.z*b3.y + a2.z*b3.z + a3.z*b3.w,
        a0.w*b3.x + a1.w*b3.y + a2.w*b3.z + a3.w*b3.w)
    )
  }
}

object Matrix4 {
  def identity(): Matrix4 = new Matrix4(
    Vector4(1f, 0f, 0f, 0f),
    Vector4(0f, 1f, 0f, 0f),
    Vector4(0f, 0f, 1f, 0f),
    Vector4(0f, 0f, 0f, 1f)
  )

  def scale(scale: Float): Matrix4 = Matrix4(
    Vector4(scale, 0f, 0f, 0f),
    Vector4(0f, scale, 0f, 0f),
    Vector4(0f, 0f, scale, 0f),
    Vector4(0f, 0f, 0f, 1f)
  )

  def scale(v: Vector4): Matrix4 = Matrix4(
    Vector4(v.x, 0f, 0f, 0f),
    Vector4(0f, v.y, 0f, 0f),
    Vector4(0f, 0f, v.z, 0f),
    Vector4(0f, 0f, 0f, 1f)
  )

  def translation(v: Vector4): Matrix4 = Matrix4(
    Vector4(1f, 0f, 0f, v.x),
    Vector4(0f, 1f, 0f, v.y),
    Vector4(0f, 0f, 1f, v.z),
    Vector4(0f, 0f, 0f, 1f)
  )

  def ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4 = {
    Matrix4(
      Vector4(2f / (right - left),  0f,                   0f,                 -(right + left) / (right - left)),
      Vector4(0f,                   2f / (top - bottom),  0f,                 -(top + bottom) / (top - bottom)),
      Vector4(0f,                   0f,                   -2f / (far - near), -(far + near)   / (far - near)),
      Vector4(0f,                   0f,                   0f,                 1f)
    )
  }
}
