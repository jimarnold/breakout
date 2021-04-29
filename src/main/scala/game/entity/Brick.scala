package game.entity

import game.graphics
import game.graphics.{RGB, Sprite}
import mafs.{Rect, Vector2}

class Brick(bounds: Rect, wall: Wall) extends Hittable {
  val color: RGB = wall.getColor(bounds.y)
  val sprite: Sprite = graphics.Sprite(bounds, color)

  def reflect(ball: Ball): Unit = {
    ball.bounce(Vector2(1, 0), EntityType.Brick)
  }

  def bounds(): Rect = {
    this.bounds
  }
}
