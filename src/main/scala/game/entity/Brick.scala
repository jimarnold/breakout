package game.entity

import game.graphics._
import mafs.{Rect, Vector2}

case class Brick(_bounds: Rect, color: Color) extends Hittable {
  val sprite: Sprite = Sprite(_bounds, color)

  def reflect(ball: Ball): Unit = {
    ball.bounce(Vector2(1, 0), EntityType.Brick)
  }

  def bounds(): Rect = {
    this._bounds
  }
}
