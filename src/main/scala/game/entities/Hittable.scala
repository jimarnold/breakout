package game.entities

import mafs.Rect

trait Hittable {
  def bounds(): Rect

  def contains(other: Hittable): Boolean = {
    this.bounds().isOverlapping(other.bounds())
  }
}
