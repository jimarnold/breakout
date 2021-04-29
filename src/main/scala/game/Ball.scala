package game

import mafs.{Line, Rect, Vector2}

class Ball(var position: Vector2, var direction: Vector2, wall: Wall) {
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

  var lastEntity: String = ""

  def update(elapsed: Float): Unit = {
    previousPosition = position
    position = position.plus(direction.mult(elapsed * speed))
    sprite.setPosition(position)
    sprite.setColor(wall.getColor(position.y))
  }

  def sprites(): Seq[Sprite] = {
    if (hidden) Seq.empty[Sprite] else List(sprite)
  }

  def bounce(normal: Vector2, entity: String): Unit = {
    if (entity != lastEntity) {
      direction = direction.reflect(normal).normalize()
      lastEntity = entity
    }
  }

  def bounds(): Rect = {
    Rect(position.x - halfWidth, position.y - halfHeight, width, height)
  }

  def progressLine(): Line = {
    val lenAB = Math.sqrt(Math.pow(previousPosition.x - position.x, 2.0) + Math.pow(previousPosition.y - position.y, 2.0)).toFloat
    val end = Vector2(position.x + (position.x - previousPosition.x) / lenAB * 5, position.y + (position.y - previousPosition.y) / lenAB * 5)
    new Line(previousPosition, end)
  }

  def hitCeiling(): Unit = {
    hasHitCeiling = true
  }

  def setSpeed(hits: Int): Unit = {
    var modifier = 0
    if (hits >= 4) {
      modifier += 100
    }
    if (hits >= 12) {
      modifier += 100
    }
    if (hasHitCeiling) {
      modifier += 100
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
