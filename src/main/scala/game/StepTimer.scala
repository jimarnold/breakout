package game

case class StepTimer(interval: Float) {
  val NANOSECONDS: Float = 0.000000001f
  var accumulator = 0f
  var before: Float = System.nanoTime() * NANOSECONDS

  def tick(): Unit = {
    val now = System.nanoTime() * NANOSECONDS
    val elapsed = now - before
    before = now
    accumulator += elapsed
  }

  def hasTimeRemaining: Boolean = {
    accumulator > 0f
  }

  def getStepTime: Float = {
    val delta = if (accumulator >= interval) {
      interval
    } else {
      accumulator
    }
    accumulator -= delta
    delta
  }
}
