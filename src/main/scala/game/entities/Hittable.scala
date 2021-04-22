package game.entities

import mafs.Rect

trait Hittable {
  def bounds(): Rect

  def contains(other: Rect): Boolean = {
    this.bounds().isOverlapping(other)
  }
}
