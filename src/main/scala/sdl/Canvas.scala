package sdl

import mafs.Vector
import scalanative.native._
import SDL._
import SDLImplicits._
import scala.scalanative.native.{Ptr, stackalloc}

case class Canvas(renderer: Ptr[Renderer]) {
  def setColor(r: Int, g: Int, b: Int): Unit =
    SDL_SetRenderDrawColor(renderer, r.toUByte, g.toUByte, b.toUByte, 0.toUByte)

  def clear(): Unit =
    SDL_RenderClear(renderer)

  def present(): Unit =
    SDL_RenderPresent(renderer)

  def drawRect(position: Vector, width: Int, height: Int): Unit = {
    val sdlRect = stackalloc[SDL.Rect].init(position.x.toInt, position.y.toInt, width, height)
    SDL_RenderFillRect(renderer, sdlRect)
  }
}
