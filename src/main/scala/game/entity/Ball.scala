package game.entity

import game.graphics.Sprite
import mafs.{Line, Rect, Vector2}

class Ball(var position: Vector2, var direction: Vector2, gameField: Rect, wall: Wall) extends Hittable {
  private val width = gameField.width / 80f
  private val height = gameField.width / 80f
  private val baseSpeed = gameField.height / 1.4f
  private val speedIncrement = gameField.height / 3.5f
  private val sprite: Sprite = Sprite(position, width, height, wall.getColor(position.y))

  private var previousPosition = position
  private var speed = baseSpeed
  private var hasHitCeiling = false
  private var hidden = false
  private var lastEntity: EntityType = _

  def update(elapsed: Float): Unit = {
    position = position.plus(direction.mult(elapsed * speed))
    sprite.setPosition(position)
    sprite.setColor(wall.getColor(position.y))
  }

  def sprites(): Seq[Sprite] = {
    if (hidden) Seq.empty[Sprite] else List(sprite)
  }

  def bounce(normal: Vector2, entityType: EntityType): Unit = {
    if (entityType == EntityType.Ceiling) {
      hasHitCeiling = true
    }
    if (entityType != lastEntity) {
      direction = direction.reflect(normal).normalize()
      lastEntity = entityType
    }
    previousPosition = position
  }

  def redirect(v: Vector2, entityType: EntityType): Unit = {
    if (entityType != lastEntity) {
      direction = v.normalize()
      lastEntity = entityType
      previousPosition = position
    }
  }

  def bounds(): Rect = {
    Rect(position, width, height)
  }

  def progressLine(): Line = {
    Line(previousPosition, position)
  }

  def setSpeed(hits: Int): Unit = {
    var modifier = 0f
    if (hits >= 4) {
      modifier += speedIncrement
    }
    if (hits >= 12) {
      modifier += speedIncrement
    }
    if (hasHitCeiling) {
      modifier += speedIncrement
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
