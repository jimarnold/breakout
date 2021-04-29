package game

import java.nio.ShortBuffer

import org.lwjgl.openal.AL10.{alBufferData, alGenBuffers}
import org.lwjgl.openal.AL10._
import org.lwjgl.openal.ALC10._
import org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.{stackGet, stackPush}
import org.lwjgl.system.libc.LibCStdlib.free
import org.lwjgl.openal.AL
import org.lwjgl.openal.ALC

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

object Sound {
  var lastPlayedTime: Long = 0
  var device: Long = 0
  var buffers: Map[String, Int] = Map.empty[String, Int]
  var sources: Map[String, Int] = Map.empty[String, Int]
  var context: Long = 0

  def paddleBeep(): Unit = {
    play(Notes.D5)
  }
  def topBeep(): Unit = {
    play(Notes.D5)
  }
  def sideBeep(): Unit = {
    play(Notes.C6)
  }
  def beep(color: RGB): Unit = {
    play(Notes.byColor.getOrElse(color, Notes.F3))
  }

  def loadBuffer(fileName: String): Int = {
    var rawAudioBuffer: ShortBuffer = null
    var channels = 0
    var sampleRate = 0

    val stack = stackPush
    try {
      val channelsBuffer = stack.mallocInt(1)
      val sampleRateBuffer = stack.mallocInt(1)
      rawAudioBuffer = stb_vorbis_decode_filename(fileName, channelsBuffer, sampleRateBuffer)
      channels = channelsBuffer.get(0)
      sampleRate = sampleRateBuffer.get(0)
    } finally if (stack != null) stack.close()

    var format = -1
    if (channels == 1) format = AL_FORMAT_MONO16
    else if (channels == 2) format = AL_FORMAT_STEREO16

    val bufferName = alGenBuffers()

    alBufferData(bufferName, format, rawAudioBuffer, sampleRate)
    free(rawAudioBuffer)
    bufferName
  }

  def init(): Unit = {
    import game.Notes.{A3, A4, C4, C5, C6, D5, E4, F3}

    val defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER)
    device = alcOpenDevice(defaultDeviceName)

    val attributes = Array(0)
    context = alcCreateContext(device, attributes)
    alcMakeContextCurrent(context)

    val alcCapabilities = ALC.createCapabilities(device)
    AL.createCapabilities(alcCapabilities)

    val stack: MemoryStack = stackGet
    val sourcePointers = stack.callocInt(8)
    alGenSources(sourcePointers)

    buffers = Map(
      F3 -> loadBuffer(F3),
      A3 -> loadBuffer(A3),
      C4 -> loadBuffer(C4),
      E4 -> loadBuffer(E4),
      A4 -> loadBuffer(A4),
      C5 -> loadBuffer(C5),
      D5 -> loadBuffer(D5),
      C6 -> loadBuffer(C6)
    )
    sources = Map(
      F3 -> sourcePointers.get(0),
      A3 -> sourcePointers.get(1),
      C4 -> sourcePointers.get(2),
      E4 -> sourcePointers.get(3),
      A4 -> sourcePointers.get(4),
      C5 -> sourcePointers.get(5),
      D5 -> sourcePointers.get(6),
      C6 -> sourcePointers.get(7)
    )
  }

  def destroy(): Unit = {
    alDeleteSources(sources.values.toArray)
    alDeleteBuffers(buffers.values.toArray)
    alcDestroyContext(context)
    alcCloseDevice(device)
  }

  private def play(file: String): Unit = {
    val now = System.nanoTime()
    val elapsed = (now - lastPlayedTime).toFloat / 1000000000
    if (elapsed > 0.05) {
      val source = sources(file)
      alSourcei(source, AL_BUFFER, buffers(file))
      alSourcePlay(source)
      lastPlayedTime = now
    }
  }
}
