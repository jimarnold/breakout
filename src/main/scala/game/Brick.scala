package game

import game.entities.Hittable
import mafs.{Rect, Vector2}

class Brick(bounds: Rect, val wall: Wall) extends Hittable {
  val color = wall.getColor(bounds.y)
  val sprite = Sprite(bounds, color)

  def draw(): Sprite = {
    sprite
  }

  def reflect(ball: Ball): Unit = {
    ball.bounce(Vector2(1, 0), "brick")
  }

  def bounds(): Rect = {
    this.bounds
  }
}
