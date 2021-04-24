package game

import sdl.{RGB, libc}

import scala.scalanative.native.{CQuote, Zone, toCString}

object BeepFile {
  val F3 = "beep_f3.wav"
  val A3 = "beep_a3.wav"
  val C4 = "beep_c4.wav"
  val E4 = "beep_e4.wav"
  val A4 = "beep_a4.wav"
  val C5 = "beep_c5.wav"
  val D5 = "beep_d5.wav"
  val C6 = "beep_c6.wav"
}

object Sound {
  def paddleBeep(): Unit = {
    play(BeepFile.D5)
  }
  def topBeep(): Unit = {
    play(BeepFile.D5)
  }
  def sideBeep(): Unit = {
    play(BeepFile.C6)
  }
  def brickBeep(color: RGB): Unit = {
    color match {
      case Color.one =>
        play(BeepFile.F3)
      case Color.two =>
        play(BeepFile.A3)
      case Color.three =>
        play(BeepFile.C4)
      case Color.four =>
        play(BeepFile.E4)
      case Color.five =>
        play(BeepFile.A4)
      case Color.six =>
        play(BeepFile.C5)
    }
  }

  private def play(file: String): Unit = Zone { implicit z =>
      val command = s"afplay $file &"
      libc.system(toCString(command))
  }
}
