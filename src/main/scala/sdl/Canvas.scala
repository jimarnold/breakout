package sdl

import scalanative.native._
import SDL._
import SDLImplicits._
import scala.scalanative.native.{Ptr, stackalloc}

case class Canvas(renderer: Ptr[Renderer]) {
  def setColor(rgb: RGB): Unit =
    setColor(rgb.r, rgb.g, rgb.b)
  def setColor(r: Int, g: Int, b: Int): Unit =
    SDL_SetRenderDrawColor(renderer, r.toUByte, g.toUByte, b.toUByte, 0.toUByte)

  def clear(): Unit =
    SDL_RenderClear(renderer)

  def present(): Unit =
    SDL_RenderPresent(renderer)

  def drawRect(rect: mafs.Rect): Unit = {
    val sdlRect = stackalloc[SDL.Rect].init(rect.x.toInt, rect.y.toInt, rect.width.toInt, rect.height.toInt)
    SDL_RenderFillRect(renderer, sdlRect)
  }

  def drawLine(line: mafs.Line): Unit = {
    SDL_RenderDrawLine(renderer, line.p1.x.toInt, line.p1.y.toInt, line.p2.x.toInt, line.p2.y.toInt)
  }
}
