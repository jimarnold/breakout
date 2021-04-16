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
    if (paddle.contains(ball.bounds())) {
      ball.bounce(paddle.reflect(ball.position, ball.direction))
    }
    bricks.foreach(brick => {
      if (brick.contains(ball.bounds())) {
        ball.bounce(brick.reflect(ball.direction))
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
    window = SDL_CreateWindow(title, 0, 0, width, height, WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, VSYNC)
    canvas = sdl.Canvas(renderer)
    newGame()
  }

  def newGame(): Unit = {
    bricks = (0 to 9).map { e => new Brick(Vector(e * 80, 10)) }.toList
    paddle = new Paddle(Vector(400, 700))
    val r1 = 0.5f - rand.nextFloat()
    val r2 = -rand.nextFloat()
    ball = new Ball(Vector(410, 695), Vector(r1, r2))
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

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

