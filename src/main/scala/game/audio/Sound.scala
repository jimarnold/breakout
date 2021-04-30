package game.audio

import java.nio.ShortBuffer
import java.nio.file.Paths

import game.graphics.RGB
import org.lwjgl.openal.AL10._
import org.lwjgl.openal.ALC10._
import org.lwjgl.openal.{AL, ALC}
import org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryStack.{stackGet, stackPush}
import org.lwjgl.system.libc.LibCStdlib.free

object Sound {
  var device: Long = 0
  var context: Long = 0
  var samples = Map.empty[String, Sample]

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

  def play(file: String): Unit = {
    samples(file).play()
  }

  def init(): Unit = {

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

    import game.audio.Notes._

    samples = Map(
      F3 -> Sample(sourcePointers.get(0), loadBuffer(F3)),
      A3 -> Sample(sourcePointers.get(1), loadBuffer(A3)),
      C4 -> Sample(sourcePointers.get(2), loadBuffer(C4)),
      E4 -> Sample(sourcePointers.get(3), loadBuffer(E4)),
      A4 -> Sample(sourcePointers.get(4), loadBuffer(A4)),
      C5 -> Sample(sourcePointers.get(5), loadBuffer(C5)),
      D5 -> Sample(sourcePointers.get(6), loadBuffer(D5)),
      C6 -> Sample(sourcePointers.get(7), loadBuffer(C6))
    )
  }

  def loadBuffer(fileName: String): Int = {
    var rawAudioBuffer: ShortBuffer = null
    var channels = 0
    var sampleRate = 0

    val stack = stackPush
    try {
      val channelsBuffer = stack.mallocInt(1)
      val sampleRateBuffer = stack.mallocInt(1)

      //TODO: this sucks. find a way to load resource files, or from a stream
      rawAudioBuffer = stb_vorbis_decode_filename(Paths.get("src/main/resources", fileName).toString, channelsBuffer, sampleRateBuffer)

      channels = channelsBuffer.get(0)
      sampleRate = sampleRateBuffer.get(0)
    } finally if (stack != null) stack.close()

    val format =  if (channels == 1) AL_FORMAT_MONO16
                  else if (channels == 2) AL_FORMAT_STEREO16
                  else -1

    val bufferName = alGenBuffers()

    alBufferData(bufferName, format, rawAudioBuffer, sampleRate)
    free(rawAudioBuffer)
    bufferName
  }

  def destroy(): Unit = {
    alDeleteSources(samples.values.map(s => s.source).toArray)
    alDeleteBuffers(samples.values.map(s => s.buffer).toArray)
    alcDestroyContext(context)
    alcCloseDevice(device)
  }
}
