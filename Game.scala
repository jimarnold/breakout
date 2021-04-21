package breakout

import scalanative.native._
import sdl._
import SDL._
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._
import mafs._
import game._
import sdl.SDLRenderFlags.{SDL_RENDERER_ACCELERATED, SDL_RENDERER_PRESENTVSYNC}
import sdl.SDLWindowFlags.SDL_WINDOW_SHOWN

object Game extends App {
  private val rand   = new java.util.Random
  private val title  = c"Game"
  private val width  = 800
  private val height = 600

  private var running                 = true
  private var window: Ptr[Window]     = _
  private var renderer: Ptr[Renderer] = _
  private var wall: Wall              = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var pressed                 = collection.mutable.Set.empty[Keycode]
  private var canvas: Canvas          = _
  private var lastTick: Long          = 0

  def drawBricks(): Unit = {
  }

  def onDraw(): Unit = {
    canvas.setColor(0, 0, 0)
    canvas.clear()
    wall.draw(canvas)
    paddle.draw(canvas)
    ball.draw(canvas)
    canvas.present()
  }

  def hitTest(): Unit = {
    if (paddle.contains(ball.bounds())) {
      paddle.reflect(ball)
    }
    wall.hitTest(ball)
  }

  def keyPressed(key: Keycode): Boolean =
    pressed.contains(key)

  def onIdle(elapsed: Float): Unit = {
    if (keyPressed(ESCAPE)) {
      running = false
    }
    if (keyPressed(SPACE)) {
      newGame()
    } else {
      val x = stackalloc[CInt]
      val y = stackalloc[CInt]
      SDL_GetMouseState(x, y)
      paddle.update(elapsed, !x)
      ball.update(elapsed)
      hitTest()
    }
  }

  def init(): Unit = {
    rand.setSeed(java.lang.System.nanoTime)
    SDL_Init(INIT_VIDEO)
    SDL_ShowCursor(SDL_DISABLE)
    SDL_SetRelativeMouseMode(SDL_TRUE)
    window = SDL_CreateWindow(title, 0, 0, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC)
    canvas = sdl.Canvas(renderer)
    newGame()
  }

  def newGame(): Unit = {
    wall = new Wall
    paddle = new Paddle(Vector(400, 500))
    ball = new Ball(Vector(800, 240), Vector(-0.5f, 0.5f))
  }

  def run(): Unit = {
    val event = stackalloc[Event]
    while (running) {
      val now = System.nanoTime()
      val elapsed = (now - lastTick).toFloat / 1000000000
      lastTick = now

      while (SDL_PollEvent(event) != 0) {
        event.type_ match {
          case QUIT =>
            return
          case KEY_DOWN =>
            pressed += event.cast[Ptr[KeyboardEvent]].keycode
          case KEY_UP =>
            pressed -= event.cast[Ptr[KeyboardEvent]].keycode
          case _ =>
            ()
        }
      }
      onDraw()
      onIdle(elapsed)
    }
  }

  init()
  run()
}

