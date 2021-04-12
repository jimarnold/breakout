package breakout

import scalanative.native._
import sdl._
import SDL._
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._

import scala.collection.mutable.ArrayBuffer

final case class Point(x: Int, y: Int) {
  def -(other: Point): Point = Point(this.x - other.x, this.y - other.y)
  def +(other: Point): Point = Point(this.x + other.x, this.y + other.y)
}

final case class Rect(x: Int, y: Int, width: Int, height: Int) {
  def contains(point: Point): Boolean = {
    (point.x >= this.x && point.x <= (this.x + this.width)) && (point.y >= this.y && point.y <= (this.y + this.height))
  }
  def contains(rect: Rect): Boolean = {
    rect.x < this.x + this.width &&
    rect.x + rect.width > this.x &&
    rect.y < this.y + this.height &&
    rect.y + rect.height > this.y
  }
  def +(point: Point): Rect = Rect(this.x + point.x, this.y + point.y, this.width, this.height)
  def -(point: Point): Rect = Rect(this.x - point.x, this.y - point.y, this.width, this.height)
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
  private var bricks: List[Rect] = _
  private var paddle: Rect           = Rect(30, 30, 60, 20)
  private var ball: Rect             = Rect(40, 20, 5, 5)
  private var ballVelocity: Point     = Point(0, 0)
  private var pressed                 = collection.mutable.Set.empty[Keycode]

  def setColor(r: Int, g: Int, b: Int): Unit =
    SDL_SetRenderDrawColor(renderer, r.toUByte, g.toUByte, b.toUByte, 0.toUByte)
  def clear(): Unit =
    SDL_RenderClear(renderer)
  def present(): Unit =
    SDL_RenderPresent(renderer)
  def drawPoint(xy: Point, w: Int, h: Int): Unit = {
    val rect = stackalloc[SDL.Rect].init(xy.x, xy.y, w, h)
    SDL_RenderFillRect(renderer, rect)
  }

  def drawRect(rect: Rect): Unit = {
    val sdlRect = stackalloc[SDL.Rect].init(rect.x, rect.y, rect.width, rect.height)
    SDL_RenderFillRect(renderer, sdlRect)
  }

  def drawBricks(): Unit = {
    setColor(0, 150, 0)
    bricks.foreach(brick => drawRect(brick))
  }
  def drawPaddle(): Unit = {
    setColor(200, 200, 200)
    drawRect(paddle)
  }
  def drawBall(): Unit = {
    setColor(255, 0, 255)
    drawRect(ball)
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
      ball = Rect(0, ball.y, ball.width, ball.height)
      ballVelocity = Point(-ballVelocity.x, ballVelocity.y)
    }
    if (ball.y < 0) {
      ball = Rect(ball.x, 0, ball.width, ball.height)
      ballVelocity = Point(ballVelocity.x, -ballVelocity.y)
    }
    if (ball.x > 799) {
      ball = Rect(799, ball.y, ball.width, ball.height)
      ballVelocity = Point(-ballVelocity.x, ballVelocity.y)
    }
    if (ball.y > 799) {
      ball = Rect(ball.x, 799, ball.width, ball.height)
      ballVelocity = Point(ballVelocity.x, -ballVelocity.y)
    }
  }

  def hitTest(): Unit = {
    val toRemove = ArrayBuffer.empty[Rect]
    bricks.foreach(brick => if (brick.contains(ball)) toRemove += brick)
    bricks = bricks diff toRemove.distinct
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
      hitTest()
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
    bricks = (0 to 19).map { e => Rect(e * 40, 10, 30, 10) }.toList
    paddle = Rect(400, 700, 60, 20)
    ball = Rect(410, 695, 5, 5)
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

