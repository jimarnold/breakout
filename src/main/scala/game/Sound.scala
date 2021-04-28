package game

import java.io.File

import sdl.RGB

import javax.sound.sampled.{AudioSystem, Clip}

object Notes {
  val F3 = getClip("beep_f3.wav")
  val A3 = getClip("beep_a3.wav")
  val C4 = getClip("beep_c4.wav")
  val E4 = getClip("beep_e4.wav")
  val A4 = getClip("beep_a4.wav")
  val C5 = getClip("beep_c5.wav")
  val D5 = getClip("beep_d5.wav")
  val C6 = getClip("beep_c6.wav")

  val byColor = Map(
    Color.one -> Notes.F3,
    Color.two -> Notes.A3,
    Color.three -> Notes.C4,
    Color.four -> Notes.E4,
    Color.five -> Notes.A4,
    Color.six -> Notes.C5
  )

  private def getClip(file: String): Clip = {
    val clip = AudioSystem.getClip
    clip.open(AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile))
    clip
  }
}

object Sound {
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

  private def play(clip: Clip): Unit = {
//    clip.setMicrosecondPosition(0)
//    clip.start()
  }
}
