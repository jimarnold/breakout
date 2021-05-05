package game

import game.audio.Sound
import game.entity.{Ball, Paddle, ScoreBoard, SideHitResult, Sides, Wall}
import game.graphics.Camera
import game.graphics.renderers.Quad
import mafs._
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._

object Breakout {
  private val WIDTH: Int = 1080
  private val HEIGHT: Int = 780
  private val camera: Camera = Camera(WIDTH, HEIGHT)
  private val stepTime = 0.001f

  private var wall: Wall              = _
  private var sides: Sides            = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var scoreboard: ScoreBoard  = _
  private var canHitBricks            = true
  private var paused                  = true
  private var playing                 = false

  def run() {
    try {
      Sound.init()

      GLFW.init(WIDTH, HEIGHT)
      initOpenGL()
      loop()

    }
    finally {
      Sound.destroy()
      GLFW.destroy()
    }
  }

  def initOpenGL(): Unit = {
    GL.createCapabilities()

    val size = GLFW.frameBufferSize()
    glViewport(0, 0, size.x.toInt, size.y.toInt)
    Quad.init()
  }

  def hitTest(): Unit = {
    if (paddle.hitTest(ball)) {
      canHitBricks = true
    }
    if (canHitBricks && wall.hitTest(ball)) {
      canHitBricks = false
    }
    sides.hitTest(ball) match {
      case SideHitResult.Side =>
        canHitBricks = true
      case SideHitResult.Ceiling =>
        canHitBricks = true
        paddle.hitCeiling()
      case _ =>
        ()
    }
  }

  def checkWinOrLose(): Unit = {
    if (ball.position.y >= HEIGHT) {
      scoreboard.lifeLost()
      newBall()
    }
    if (scoreboard.livesRemaining == 0 || wall.isDestroyed) {
      ball.hide()
      paused = true
      playing = false
    }
  }

  def onIdle(elapsed: Float): Unit = {
    if (playing && !paused) {
      paddle.update(elapsed, GLFW.getCursorPos.x)
      ball.update(elapsed)

      hitTest()

      ball.setSpeed(wall.hits)

      checkWinOrLose()
    }
  }

  def togglePause(): Unit = {
    paused = !paused
  }

  def newGame(): Unit = {
    initEntities()
    paused = false
    playing = true
  }

  def introScreen(): Unit = {
    initEntities()
    ball.hide()
    paused = true
    playing = false
  }

  private def initEntities(): Unit = {
    scoreboard = new ScoreBoard
    sides = new Sides(WIDTH, HEIGHT)
    val gameField = Rect(sides.width, sides.width * 3, WIDTH - (sides.width * 2), HEIGHT - sides.width)
    wall = new Wall(gameField, scoreboard)
    paddle = Paddle(Vector2(WIDTH / 2, HEIGHT - 20), gameField)
    newBall()
  }

  def newBall(): Unit = {
    val gameField = Rect(sides.width, sides.ceilingLowerY, WIDTH - (sides.width * 2), HEIGHT - sides.ceilingLowerY)
    ball = new Ball(Vector2(WIDTH - (sides.width * 2), wall.lowerBound), Vector2(-0.5f, 0.5f).normalize(), gameField, wall)
  }

  def loop() {
    introScreen()

    val timer = StepTimer(stepTime)

    while (!GLFW.windowShouldClose()) {
      timer.tick()

      while (timer.hasTimeRemaining) {
        GLFW.pollEvents()
        onIdle(timer.getStepTime)
      }

      render()
    }
  }

  private def render(): Unit = {
    clearScreen()

    Quad.render(allSprites, camera)

    GLFW.swapBuffers()
  }

  private def clearScreen(): Unit = {
    glClearColor(0.0f, 0.0f, 0.01f, 1)
    glClear(GL_COLOR_BUFFER_BIT)
  }

  private def allSprites = {
    scoreboard.sprites ++
    sides.sprites ++
    wall.sprites ++
    paddle.sprites ++
    ball.sprites
  }
}
