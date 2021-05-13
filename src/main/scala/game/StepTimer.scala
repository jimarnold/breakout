package game

case class StepTimer() {
  private val NANOSECONDS: Float = 0.000000001f
  private val interval = 5000000L // 5ms
  private var accumulator: Long = 0L
  private var before: Long = System.nanoTime()

  def tick(): Long = {
    val now = System.nanoTime()
    val elapsed = now - before

    before = now
    accumulator += elapsed

    elapsed
  }

  def hasTimeRemaining: Boolean = {
    accumulator > 0L
  }

  def getStepTime: Float = {
    val delta = if (accumulator >= interval) {
      interval
    } else {
      accumulator
    }
    accumulator -= delta

    delta * NANOSECONDS
  }
}
