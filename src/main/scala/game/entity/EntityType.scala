package game.entity

case class EntityType(t: String)

object EntityType {
  val Brick: EntityType = EntityType("brick")
  val LeftSide: EntityType = EntityType("left")
  val RightSide: EntityType = EntityType("right")
  val Ceiling: EntityType = EntityType("ceiling")
  val Paddle: EntityType = EntityType("paddle")
}
