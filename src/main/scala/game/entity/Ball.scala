package game.entity

import game.graphics.Sprite
import mafs.{Line, Rect, Vector2}

class Ball(var position: Vector2, var direction: Vector2, wall: Wall) extends Hittable {
  private val width = 10f
  private val halfWidth = width / 2f
  private val height = 10f
  private val halfHeight = height / 2f
  private val baseSpeed = 500
  private var previousPosition = position
  private var speed = baseSpeed
  private var hasHitCeiling = false
  private val sprite: Sprite = Sprite(position.x, position.y, width, height, wall.getColor(position.y))
  private var hidden = false

  var lastEntity: EntityType = _

  def update(elapsed: Float): Unit = {
    previousPosition = position
    position = position.plus(direction.mult(elapsed * speed))
    sprite.setPosition(position)
    sprite.setColor(wall.getColor(position.y))
  }

  def sprites(): Seq[Sprite] = {
    if (hidden) Seq.empty[Sprite] else List(sprite)
  }

  def bounce(normal: Vector2, entityType: EntityType): Unit = {
    if (entityType != lastEntity) {
      direction = direction.reflect(normal).normalize()
      lastEntity = entityType
    }
  }

  def bounds(): Rect = {
    Rect(position.x - halfWidth, position.y - halfHeight, width, height)
  }

  def progressLine(): Line = {
    Line(previousPosition, position)
  }

  def hitCeiling(): Unit = {
    hasHitCeiling = true
  }

  def setSpeed(hits: Int): Unit = {
    var modifier = 0
    if (hits >= 4) {
      modifier += 150
    }
    if (hits >= 12) {
      modifier += 150
    }
    if (hasHitCeiling) {
      modifier += 150
    }
    speed = baseSpeed + modifier
  }

  def hide(): Unit = {
    hidden = true
  }

  def show(): Unit = {
    hidden = false
  }
}
