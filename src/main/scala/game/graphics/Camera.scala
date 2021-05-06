package game.graphics

import mafs.Matrix4

case class Camera(width: Int, height: Int) {
  private val viewMatrix = Matrix4.ortho(0f, width, height, 0f, -1f, 1f)

  def view(sprite: Sprite): Matrix4 = {
    val spriteTransform = sprite.locate()
    (spriteTransform mult viewMatrix)
  }
}
