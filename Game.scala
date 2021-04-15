package breakout

import scalanative.native._
import sdl._
import SDL._
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._

import scala.collection.mutable.ArrayBuffer

final case class Rect(x: Float, y: Float, width: Float, height: Float) {

}

class Ball(var position: Vector, var velocity: Vector) {
  val width = 5
  val height = 5
  def update(elapsed: Float): Unit = {
    position = position.plus(velocity.mult(elapsed * 0.2f))
    if (position.x < 0) {
      position = Vector(0, position.y)
      velocity = Vector(-velocity.x, velocity.y)
    }
    if (position.y < 0) {
      position = Vector(position.x, 0)
      velocity = Vector(velocity.x, -velocity.y)
    }
    if (position.x > 799) {
      position = Vector(799, position.y)
      velocity = Vector(-velocity.x, velocity.y)
    }
    if (position.y > 799) {
      position = Vector(position.x, 799)
      velocity = Vector(velocity.x, -velocity.y)
    }
  }

  def draw(canvas: Canvas): Unit = {
    canvas.setColor(255, 0, 255)
    canvas.drawRect(position, 5, 5)
  }

  def bounds(): Rect = {
    Rect(position.x, position.y, width, height)
  }
}

class Brick(var position: Vector) {
  val width = 30
  val height = 10
  def contains(v: Vector): Boolean = {
    (v.x >= position.x && v.x <= (position.x + width)) && (v.y >= position.y && v.y <= (position.y + height))
  }
  def contains(rect: Rect): Boolean = {
    rect.x < position.x + width &&
      rect.x + rect.width > position.x &&
      rect.y < position.y + height &&
      rect.y + rect.height > position.y
  }
  def update(): Unit = {

  }
  def draw(canvas: Canvas): Unit = {
    canvas.setColor(0, 150, 0)
    canvas.drawRect(position, 30, 10)
  }
}

class Paddle(var position: Vector) {
  private val Left   = Vector(-1, 0)
  private val Right  = Vector(1, 0)
  private val Stop   = Vector(0, 0)

  def update(elapsed: Float, pressed: scala.collection.Set[Keycode]): Unit = {
    val direction =
      if (pressed.contains(LEFT_KEY)) Left
      else if (pressed.contains(RIGHT_KEY)) Right
      else Stop
    val oldPos = position
    position = position.plus(direction.mult(elapsed * 0.05f))
    if (position.x < 0 || position.y < 0 || position.x > 799 || position.y > 799) {
      position = oldPos
    }
  }
  def draw(canvas: Canvas): Unit = {
    canvas.setColor(200, 200, 200)
    canvas.drawRect(position, 120, 20)
  }
}

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
    bricks.foreach(brick => {
      if (brick.contains(ball.bounds())) {
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
    canvas = Canvas(renderer)
    newGame()
  }

  def newGame(): Unit = {
    bricks = (0 to 19).map { e => new Brick(Vector(e * 40, 10)) }.toList
    paddle = new Paddle(Vector(400, 700))
    val r1 = (0.5f - rand.nextFloat()) * 0.1f
    val r2 = -0.1f
    ball = new Ball(Vector(410, 695), Vector(r1, r2))
  }

  def delay(ms: UInt): Unit =
    SDL_Delay(ms)

  def loop(): Unit = {
    val event = stackalloc[Event]
    while (running) {
      val elapsed = (System.nanoTime() - lastTick).toFloat

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
      lastTick = System.nanoTime()
    }
  }

  init()
  loop()
}

