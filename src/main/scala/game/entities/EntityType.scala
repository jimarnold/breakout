package game.entities

case class EntityType(t: String)

object EntityType {
  val Brick: EntityType = EntityType("brick")
  val Sides: EntityType = EntityType("sides")
  val Ceiling: EntityType = EntityType("ceiling")
  val Paddle: EntityType = EntityType("paddle")
}
