package breakout

import scalanative.native._
import sdl._
import SDL._
import sdl.RGB
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._
import mafs._
import game._
import sdl.SDLRenderFlags.{SDL_RENDERER_ACCELERATED, SDL_RENDERER_PRESENTVSYNC}
import sdl.SDLWindowFlags.SDL_WINDOW_SHOWN

import scala.collection.mutable.ArrayBuffer

object Game extends App {
  private val rand   = new java.util.Random
  private val title  = c"Game"
  private val width  = 800
  private val height = 800

  private var running                 = true
  private var window: Ptr[Window]     = _
  private var renderer: Ptr[Renderer] = _
  private var bricks: List[Brick]     = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var pressed                 = collection.mutable.Set.empty[Keycode]
  private var canvas: Canvas          = _
  private var lastTick: Long          = 0
  private val brickColor1: RGB        = RGB(255, 0, 0)
  private val brickColor2: RGB        = RGB(0, 0, 255)
  private val brickColor3: RGB        = RGB(0, 255, 255)

  def drawBricks(): Unit = {
  }

  def onDraw(): Unit = {
    canvas.setColor(0, 0, 0)
    canvas.clear()
    bricks.foreach(brick => brick.draw(canvas))
    paddle.draw(canvas)
    ball.draw(canvas)
    canvas.present()
  }

  def hitTest(): Unit = {
    val toRemove = ArrayBuffer.empty[Brick]
    if (paddle.contains(ball.position)) {
      ball.bounce(paddle.reflect(ball.position, ball.direction))
    }
    bricks.foreach(brick => {
      if (brick.contains(ball.position)) {
        ball.bounce(brick.reflect(ball.position, ball.direction))
        toRemove += brick
      }
    })
    bricks = bricks diff toRemove.distinct
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
      paddle.update(elapsed, pressed)
      ball.update(elapsed)
      hitTest()
    }
  }

  def init(): Unit = {
    rand.setSeed(java.lang.System.nanoTime)
    SDL_Init(INIT_VIDEO)
    window = SDL_CreateWindow(title, 0, 0, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC)
    canvas = sdl.Canvas(renderer)
    newGame()
  }

  def newGame(): Unit = {
    var brickSeq = (0 to 9).map { e => new Brick(Rect(e * 80, 10, 60, 20), brickColor1) }
    brickSeq ++= (0 to 9).map { e => new Brick(Rect(e * 80, 60, 60, 20), brickColor2) }
    brickSeq ++= (0 to 9).map { e => new Brick(Rect(e * 80, 120, 60, 20), brickColor3) }
    bricks = brickSeq.toList
    paddle = new Paddle(Vector(400, 700), rand)
    ball = new Ball(Vector(410, 695), Vector(0.5f, 0.5f))
  }

  def loop(): Unit = {
    val event = stackalloc[Event]
    while (running) {
      val now = System.nanoTime()
      val elapsed = (now - lastTick).toFloat  / 1000000000
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
  loop()
}

