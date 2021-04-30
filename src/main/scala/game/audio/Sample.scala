package game.audio

import org.lwjgl.openal.AL10.{AL_BUFFER, alSourcePlay, alSourcei}

case class Sample(source: Int, buffer: Int) {
  var lastPlayedTime: Long = 0
  val debounceTime = 0.05

  def play(): Unit = {
    // if the same sound is triggered again too quickly, it can sound crappy, so prevent it:
    val now = System.nanoTime()
    val elapsed = (now - lastPlayedTime).toFloat / 1000000000
    if (elapsed > debounceTime) {
      alSourcei(source, AL_BUFFER, buffer)
      alSourcePlay(source)
      lastPlayedTime = now
    }
  }
}
