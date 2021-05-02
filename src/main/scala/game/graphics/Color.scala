package game.graphics


final case class Color(rgba: Array[Float]) {
}

object Color {
  val one: Color = Color(200, 104, 85)
  val two: Color = Color(200, 130, 75)
  val three: Color = Color(185, 135, 65)
  val four: Color = Color(170, 165, 65)
  val five: Color = Color(95, 155, 85)
  val six: Color = Color(75, 85, 200)
  val grey: Color = Color(140, 140, 140)
  val paddle: Color = Color(185, 80, 75)
  val ball: Color = paddle

  def apply(r: Float, g: Float, b: Float): Color = Color(r, g, b, 1f)
  def apply(r: Float, g: Float, b: Float, a: Float): Color = Color(Array(r, g, b, a))
  def apply(r: Int, g: Int, b: Int): Color = Color(r / 255f, g / 255f, b / 255f, 1f)
  def apply(r: Int, g: Int, b: Int, a: Int): Color = Color(r / 255f, g / 255f, b / 255f, a / 255f)
}
