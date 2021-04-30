package game.audio

import game.graphics.Color

object Notes {
  val F3 = "beep_f3.ogg"
  val A3 = "beep_a3.ogg"
  val C4 = "beep_c4.ogg"
  val E4 = "beep_e4.ogg"
  val A4 = "beep_a4.ogg"
  val C5 = "beep_c5.ogg"
  val D5 = "beep_d5.ogg"
  val C6 = "beep_c6.ogg"

  val byColor = Map(
    Color.one -> Notes.C5,
    Color.two -> Notes.A4,
    Color.three -> Notes.E4,
    Color.four -> Notes.C4,
    Color.five -> Notes.A3,
    Color.six -> Notes.F3
  )
}
