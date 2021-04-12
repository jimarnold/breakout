package breakout

import scalanative.native._
import sdl._
import SDL._
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._

final case class Point(x: Int, y: Int) {
  def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
  def +(other: Point): Point = Point(this.x - other.x, this.y - other.y)
}

object Game extends App {
  private val rand                    = new java.util.Random
  private val title  = c"Game"
  private val width  = 800
  private val height = 800
  private val Left   = Point(10, 0)
  private val Right  = Point(-10, 0)
  private val Stop   = Point(0, 0)

  private var running                 = true
  private var window: Ptr[Window]     = _
  private var renderer: Ptr[Renderer] = _
  private var bricks: IndexedSeq[Point]     = _
  private var paddle: Point           = Point(30, 30)
  private var ball: Point             = Point(40, 20)
  private var ballVelocity: Point     = Point(0, 0)
  private var pressed                 = collection.mutable.Set.empty[Keycode]

  def setColor(r: Int, g: Int, b: Int): Unit =
    SDL_SetRenderDrawColor(renderer, r.toUByte, g.toUByte, b.toUByte, 0.toUByte)
  def clear(): Unit =
    SDL_RenderClear(renderer)
  def present(): Unit =
    SDL_RenderPresent(renderer)
  def drawRect(xy: Point, w: Int, h: Int): Unit = {
    val rect = stackalloc[Rect].init(xy.x, xy.y, w, h)
    SDL_RenderFillRect(renderer, rect)
  }
  def drawBricks(): Unit = {
    setColor(0, 150, 0)
    bricks.foreach(brick => drawRect(brick, 30, 20))
  }
  def drawPaddle(): Unit = {
    setColor(200, 200, 200)
    drawRect(paddle, 60, 20)
  }
  def drawBall(): Unit = {
    setColor(255, 0, 255)
    drawRect(ball, 4, 4)
  }
  def onDraw(): Unit = {
    setColor(0, 0, 0)
    clear()
    drawBricks()
    drawPaddle()
    drawBall()
    present()
  }

  def movePaddle(newPos: Point): Unit = {
    val oldPos = paddle
    paddle = paddle + newPos
    if (paddle.x < 0 || paddle.y < 0 || paddle.x > 799 || paddle.y > 799) {
      paddle = oldPos
    }
  }

  def moveBall(): Unit = {
    ball = ball + ballVelocity
    if (ball.x < 0) {
      ball = Point(0, ball.y)
      ballVelocity = Point(-ballVelocity.x, ballVelocity.y)
    }
    if (ball.y < 0) {
      ball = Point(ball.x, 0)
      ballVelocity = Point(ballVelocity.x, -ballVelocity.y)
    }
    if (ball.x > 799) {
      ball = Point(799, ball.y)
      ballVelocity = Point(-ballVelocity.x, ballVelocity.y)
    }
    if (ball.y > 799) {
      ball = Point(ball.x, 799)
      ballVelocity = Point(ballVelocity.x, -ballVelocity.y)
    }
  }

  def keyPressed(key: Keycode): Boolean =
    pressed.contains(key)

  def onIdle(): Unit = {
    if (keyPressed(ESCAPE)) {
      running = false
    }
    if (keyPressed(SPACE)) {
      newGame()
    } else {
      movePaddle(
        if (keyPressed(LEFT_KEY)) Left
        else if (keyPressed(RIGHT_KEY)) Right
        else Stop)
      moveBall()
    }
  }

  def init(): Unit = {
    rand.setSeed(java.lang.System.nanoTime)
    SDL_Init(INIT_VIDEO)
    window = SDL_CreateWindow(title, 0, 0, width, height, WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, VSYNC)
    newGame()
  }

  def newGame(): Unit = {
    bricks = (0 to 19).map { e => Point(e * 40, 10) }
    paddle = Point(400, 700)
    ball = Point(410, 695)
    val r1 = 1 + rand.nextInt(( 8 - 1) + 1)
    val r2 = 1 + rand.nextInt(( 8 - 1) + 1)
    ballVelocity = Point(r1, r2)
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

  def loop(): Unit = {
    val event = stackalloc[Event]
    while (running) {
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
      onIdle()
      delay(20.toUInt)
    }
  }

  init()
  loop()
}

