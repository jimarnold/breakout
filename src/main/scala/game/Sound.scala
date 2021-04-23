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

  var lastPlayedTime: Long = 0
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
        brickBeep1()
      case Color.two =>
        brickBeep2()
      case Color.three =>
        brickBeep3()
      case Color.four =>
        brickBeep4()
      case Color.five =>
        brickBeep5()
      case Color.six =>
        brickBeep6()
    }
  }
  def brickBeep1(): Unit = {
    play(BeepFile.F3)
  }
  def brickBeep2(): Unit = {
    play(BeepFile.A3)
  }
  def brickBeep3(): Unit = {
    play(BeepFile.C4)
  }
  def brickBeep4(): Unit = {
    play(BeepFile.E4)
  }
  def brickBeep5(): Unit = {
    play(BeepFile.A4)
  }
  def brickBeep6(): Unit = {
    play(BeepFile.C5)
  }

  private def play(file: String): Unit = Zone { implicit z =>
    val now = System.nanoTime()
    val elapsed = (now - lastPlayedTime).toFloat / 1000000000

    if (elapsed > 0.02) {
      val command = s"afplay $file &"
      libc.system(toCString(command))
      lastPlayedTime = now
    }
  }
}
