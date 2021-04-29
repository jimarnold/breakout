package game

final case class RGB(r: Int, g: Int, b: Int) {
  def toa(): Array[Float] = Array(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f)
}
