package game

import sdl.RGB
import sys.process._

object Notes {
  val F3 = "beep_f3.wav"
  val A3 = "beep_a3.wav"
  val C4 = "beep_c4.wav"
  val E4 = "beep_e4.wav"
  val A4 = "beep_a4.wav"
  val C5 = "beep_c5.wav"
  val D5 = "beep_d5.wav"
  val C6 = "beep_c6.wav"

  val byColor = Map(
    Color.one -> Notes.F3,
    Color.two -> Notes.A3,
    Color.three -> Notes.C4,
    Color.four -> Notes.E4,
    Color.five -> Notes.A4,
    Color.six -> Notes.C5
  )
}

object Sound {
  var lastPlayedTime: Long = 0

  def paddleBeep(): Unit = {
    play(Notes.D5)
  }
  def topBeep(): Unit = {
    play(Notes.D5)
  }
  def sideBeep(): Unit = {
    play(Notes.C6)
  }
  def brickBeep(color: RGB): Unit = {
    play(Notes.byColor.getOrElse(color, Notes.F3))
  }

  private def play(file: String): Unit = {
    val now = System.nanoTime()
    val elapsed = (now - lastPlayedTime).toFloat / 1000000000
    if (elapsed > 0.02) {
      s"afplay $file".run()
      lastPlayedTime = now
    }
  }
}
