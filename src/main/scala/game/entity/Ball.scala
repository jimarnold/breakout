package game.entity

import game.graphics.Sprite
import mafs.{Line, Rect, Vector2, Point}

class Ball(var position: Point, var direction: Vector2, gameField: Rect, wall: Wall) extends Hittable {
  private val width = gameField.width / 80f
  private val height = gameField.width / 80f
  private val baseSpeed = gameField.height / 1.3f
  private val speedIncrement = gameField.height / 5f
  private val sprite: Sprite = Sprite(position, width, height, wall.getColor(position.y))

  private var previousPosition = position
  private var speed = baseSpeed
  private var hasHitCeiling = false
  private var hidden = false
  private var lastEntity: EntityType = _

  def update(elapsed: Float): Unit = {
    position = position.translate(direction.mult(elapsed * speed))
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

  def redirect(newDirection: Vector2, entityType: EntityType): Unit = {
    if (entityType != lastEntity) {
      direction = newDirection.normalize()
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
    speed = baseSpeed
    if (hits >= 4) {
      speed += speedIncrement
    }
    if (hits >= 12) {
      speed += speedIncrement
    }
    if (hits >= 30) {
      speed += speedIncrement
    }
    if (hasHitCeiling) {
      speed += speedIncrement
    }
  }

  def hide(): Unit = {
    hidden = true
  }

  def show(): Unit = {
    hidden = false
  }
}
