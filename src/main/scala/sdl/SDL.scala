package sdl

import scala.scalanative.native._

@extern
@link("SDL2")
object SDL {
  type Window   = CStruct0
  type Renderer = CStruct0
  type _56   = Nat.Digit[Nat._5, Nat._6]
  type Event = CStruct2[UInt, CArray[Byte, _56]]
  type Rect = CStruct4[CInt, CInt, CInt, CInt]
  type KeyboardEvent = CStruct8[UInt, UInt, UInt, UByte, UByte, UByte, UByte, Keysym]
  type Keysym   = CStruct4[Scancode, Keycode, UShort, UInt]
  type Scancode = Int
  type Keycode  = Int

  def SDL_Init(flags: UInt): Unit = extern
  def SDL_CreateWindow(title: CString,
                       x: CInt,
                       y: CInt,
                       w: Int,
                       h: Int,
                       flags: UInt): Ptr[Window] = extern
  def SDL_Delay(ms: UInt): Unit = extern
  def SDL_CreateRenderer(window: Ptr[Window],
                         index: CInt,
                         flags: UInt): Ptr[Renderer] = extern
  def SDL_PollEvent(event: Ptr[Event]): CInt = extern
  def SDL_RenderClear(renderer: Ptr[Renderer]): Unit = extern
  def SDL_SetRenderDrawColor(renderer: Ptr[Renderer],
                             r: UByte,
                             g: UByte,
                             b: UByte,
                             a: UByte): Unit = extern
  def SDL_RenderFillRect(renderer: Ptr[Renderer], rect: Ptr[Rect]): Unit = extern
  def SDL_RenderDrawLine(renderer: Ptr[Renderer], x1: CInt, y1: CInt, x2: CInt, y2: CInt): Unit = extern
  def SDL_RenderPresent(renderer: Ptr[Renderer]): Unit = extern
}

object SDLEvents {
  val QUIT = 0x100.toUInt
  val KEY_DOWN  = 0x300.toUInt
  val KEY_UP    = (0x300 + 1).toUInt
}

object SDLKeys {
  val RIGHT_KEY = 1073741903
  val LEFT_KEY  = 1073741904
  val DOWN_KEY  = 1073741905
  val UP_KEY    = 1073741906
  val SPACE = 32
  val ESCAPE = 27
}

object SDLConst {
  val INIT_VIDEO   = 0x00000020.toUInt
}

object SDLWindowFlags {
  val SDL_WINDOW_FULLSCREEN: CUnsignedInt = 0x00000001.toUInt
  val SDL_WINDOW_FULLSCREEN_DESKTOP: CUnsignedInt = SDL_WINDOW_FULLSCREEN | 0x00001000.toUInt
  val SDL_WINDOW_SHOWN: CUnsignedInt = 0x00000004.toUInt
}

object SDLRenderFlags {
  val SDL_RENDERER_SOFTWARE: CUnsignedInt = 0x00000001.toUInt
  // The renderer is a software fallback
  val SDL_RENDERER_ACCELERATED: CUnsignedInt = 0x00000002.toUInt
  // The renderer uses hardware acceleration
  val SDL_RENDERER_PRESENTVSYNC: CUnsignedInt = 0x00000004.toUInt
  // Present is synchronized with the refresh rate
  val SDL_RENDERER_TARGETTEXTURE: CUnsignedInt = 0x00000008.toUInt
  // The renderer supports rendering to texture
}

object SDLImplicits {
  implicit class EventOps(val self: Ptr[SDL.Event]) extends AnyVal {
    def type_ = !(self._1)
  }

  implicit class RectOps(val self: Ptr[SDL.Rect]) extends AnyVal {
    def init(x: Int, y: Int, w: Int, h: Int): Ptr[SDL.Rect] = {
      !(self._1) = x
      !(self._2) = y
      !(self._3) = w
      !(self._4) = h
      self
    }
  }

  implicit class KeyboardEventOps(val self: Ptr[SDL.KeyboardEvent])
    extends AnyVal {
    def keycode: SDL.Keycode = !(self._8._2)
  }
}
