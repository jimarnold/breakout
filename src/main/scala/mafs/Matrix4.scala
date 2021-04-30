package mafs

case class Matrix4(
    _0x: Float, _0y: Float, _0z: Float, _0w: Float,
    _1x: Float, _1y: Float, _1z: Float, _1w: Float,
    _2x: Float, _2y: Float, _2z: Float, _2w: Float,
    _3x: Float, _3y: Float, _3z: Float, _3w: Float) {

  def toa(): Array[Float] = {
    // flipping to column-wise because opengl
    Array(
      _0x, _1x, _2x, _3x,
      _0y, _1y, _2y, _3y,
      _0z, _1z, _2z, _3z,
      _0w, _1w, _2w, _3w)
  }

  def mult (v: Vector4): Vector4 = {
    Vector4(
      _0x * v.x + _0y * v.y + _0z * v.z + _0w * v.w,
      _1x * v.x + _1y * v.y + _1z * v.z + _1w * v.w,
      _2x * v.x + _2y * v.y + _2z * v.z + _2w * v.w,
      _3x * v.x + _3y * v.y + _3z * v.z + _3w * v.w
    )
  }

  def mult (b: Matrix4): Matrix4 = {
    Matrix4(
      Vector4(
        _0x * b._0x + _1x * b._0y + _2x * b._0z + _3x * b._0w,
        _0y * b._0x + _1y * b._0y + _2y * b._0z + _3y * b._0w,
        _0z * b._0x + _1z * b._0y + _2z * b._0z + _3z * b._0w,
        _0w * b._0x + _1w * b._0y + _2w * b._0z + _3w * b._0w),
        Vector4(
        _0x * b._1x + _1x * b._1y + _2x * b._1z + _3x * b._1w,
        _0y * b._1x + _1y * b._1y + _2y * b._1z + _3y * b._1w,
        _0z * b._1x + _1z * b._1y + _2z * b._1z + _3z * b._1w,
        _0w * b._1x + _1w * b._1y + _2w * b._1z + _3w * b._1w),
        Vector4(
        _0x * b._2x + _1x * b._2y + _2x * b._2z + _3x * b._2w,
        _0y * b._2x + _1y * b._2y + _2y * b._2z + _3y * b._2w,
        _0z * b._2x + _1z * b._2y + _2z * b._2z + _3z * b._2w,
        _0w * b._2x + _1w * b._2y + _2w * b._2z + _3w * b._2w),
        Vector4(
        _0x * b._3x + _1x * b._3y + _2x * b._3z + _3x * b._3w,
        _0y * b._3x + _1y * b._3y + _2y * b._3z + _3y * b._3w,
        _0z * b._3x + _1z * b._3y + _2z * b._3z + _3z * b._3w,
        _0w * b._3x + _1w * b._3y + _2w * b._3z + _3w * b._3w)
    )
  }
}

object Matrix4 {
  def apply(v0: Vector4, v1: Vector4, v2: Vector4, v3: Vector4): Matrix4 = Matrix4(
    v0.x, v0.y, v0.z, v0.w,
    v1.x, v1.y, v1.z, v1.w,
    v2.x, v2.y, v2.z, v2.w,
    v3.x, v3.y, v3.z, v3.w
  )

  def identity(): Matrix4 = Matrix4(
    1f, 0f, 0f, 0f,
    0f, 1f, 0f, 0f,
    0f, 0f, 1f, 0f,
    0f, 0f, 0f, 1f
  )

  def scale(x: Float, y: Float, z: Float = 1f, w: Float = 1f): Matrix4 = Matrix4(
    x, 0f, 0f, 0f,
    0f, y, 0f, 0f,
    0f, 0f, z, 0f,
    0f, 0f, 0f, w
  )

  def scale(f: Float): Matrix4 = scale(f, f, f)

  def scale(v: Vector2): Matrix4 = scale(v.x, v.y)

  def scale(v: Vector4): Matrix4 = scale(v.x, v.y, v.z, v.w)

  def translate(x: Float, y: Float, z: Float = 0f, w: Float = 1f): Matrix4 = Matrix4(
    1f, 0f, 0f, x,
    0f, 1f, 0f, y,
    0f, 0f, 1f, z,
    0f, 0f, 0f, w
  )

  def translate(v: Vector2): Matrix4 = translate(v.x, v.y)

  def translate(v: Vector4): Matrix4 = translate(v.x, v.y, v.z, v.w)

  def ortho(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4 =
    Matrix4(
      2f / (right - left),  0f,                   0f,                 -(right + left) / (right - left),
      0f,                   2f / (top - bottom),  0f,                 -(top + bottom) / (top - bottom),
      0f,                   0f,                   -2f / (far - near), -(far + near)   / (far - near),
      0f,                   0f,                   0f,                 1f
    )
}
