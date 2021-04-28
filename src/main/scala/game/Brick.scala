package game

import game.entities.Hittable
import mafs.{Rect, Vector2}

class Brick(bounds: Rect, val wall: Wall) extends Hittable {
  val color = wall.getColor(bounds.y)
  val sprite = Sprite(bounds.width, bounds.height, color)
  sprite.setPosition(Vector2(bounds.x, bounds.y))

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
