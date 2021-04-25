package game

import scalanative.native._
import sdl._
import SDL._
import SDLEvents._
import SDLKeys._
import SDLConst._
import SDLImplicits._
import mafs._
import sdl.SDLRenderFlags.{SDL_RENDERER_ACCELERATED, SDL_RENDERER_PRESENTVSYNC}
import sdl.SDLWindowFlags.SDL_WINDOW_SHOWN

object Breakout {
  private val title  = c"Game"
  private val width  = 1000
  private val height = 700

  private var running                 = true
  private var playing                 = false
  private var window: Ptr[Window]     = _
  private var renderer: Ptr[Renderer] = _
  private var wall: Wall              = _
  private var sides: Sides            = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var scoreboard: ScoreBoard  = _
  private var pressed                 = collection.mutable.Set.empty[Keycode]
  private var canvas: Canvas          = _
  private var lastTick: Long          = 0
  private var lives                   = 5
  private var canHitBricks            = true
  private var paused                  = true

  def onDraw(): Unit = {
    canvas.setColor(0, 0, 0)
    canvas.clear()
    sides.draw(canvas)
    wall.draw(canvas)
    paddle.draw(canvas)
    if (ball != null) {
      ball.draw(canvas)
    }
    scoreboard.draw(canvas)

    canvas.present()
  }

  def hitTest(): Unit = {
    if (paddle.contains(ball.bounds())) {
      paddle.reflect(ball)
      Sound.paddleBeep()
      canHitBricks = true
    }
    if (canHitBricks) {
      val hitBrick = wall.hitTest(ball)
      if (hitBrick) {
        canHitBricks = false
      }
    }
    sides.hitTest(ball) match {
      case SideHitResult.Side =>
        canHitBricks = true
      case SideHitResult.Ceiling =>
        canHitBricks = true
        ball.hitCeiling()
        paddle.hitCeiling()
      case _ =>
        ()
    }
  }

  def keyPressed(key: Keycode): Boolean =
    pressed.contains(key)

  def checkWinOrLose(): Unit = {
    if (ball.position.y >= height) {
      lives-= 1
      scoreboard.setLives(lives)
      newBall()
    }
    if (lives == 0 || wall.isDestroyed) {
      ball = null
      paused = true
      playing = false
    }
  }

  def onIdle(elapsed: Float): Unit = {
    if (playing && !paused) {
      val x = stackalloc[CInt]
      val y = stackalloc[CInt]
      SDL_GetRelativeMouseState(x, y)
      hitTest()
      paddle.update(elapsed, !x)
      ball.update(elapsed)
      ball.setSpeed(wall.hits)
      checkWinOrLose()
    }
  }

  def newGame(): Unit = {
    initStaticEntities()
    newBall()
    paused = false
    playing = true
  }

  def introScreen(): Unit = {
    initStaticEntities()
    ball = null
    paused = true
    playing = false
  }

  private def initStaticEntities(): Unit = {
    lives = 5
    scoreboard = new ScoreBoard
    scoreboard.setLives(lives)
    sides = new Sides(width, height)
    val gameField = Rect(sides.width, sides.width * 3, width - (sides.width * 2), height - sides.width)
    wall = new Wall(gameField, scoreboard)
    paddle = new Paddle(Vector(width / 2, height - 20), gameField)
  }

  def newBall(): Unit = {
    ball = new Ball(Vector(width - (sides.width * 2), wall.lowerBound), Vector(-0.5f, 0.5f), wall)
  }

  def initSDL(): Unit = {
    SDL_Init(SDL_INIT_VIDEO)
    SDL_ShowCursor(SDL_DISABLE)
    SDL_SetRelativeMouseMode(SDL_TRUE)
    window = SDL_CreateWindow(title, 0, 0, width, height, SDL_WINDOW_SHOWN)
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED | SDL_RENDERER_PRESENTVSYNC)
    canvas = sdl.Canvas(renderer)
  }

  def run(): Unit = {
    initSDL()
    introScreen()

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
            if (keyPressed(P)) {
              paused = !paused
            }
            if (keyPressed(ESCAPE)) {
              running = false
            }
            if (keyPressed(SPACE)) {
              newGame()
            }
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
}

