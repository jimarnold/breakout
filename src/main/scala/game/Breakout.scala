package game

import org.lwjgl.glfw.GLFW._

import game.audio.Sound
import game.entity.{Ball, Paddle, ScoreBoard, SideHitResult, Sides, Wall}
import game.graphics.Camera
import game.graphics.renderers.QuadRenderer
import mafs._

object Breakout {
  private val WIDTH: Int = 1080
  private val HEIGHT: Int = 780
  private val camera: Camera = Camera(WIDTH, HEIGHT)

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
      Screen.init(WIDTH, HEIGHT, onKeyUp)
      QuadRenderer.init()

      loop()
    }
    finally {
      Sound.destroy()
      Screen.destroy()
    }
  }

  private def loop() {
    introScreen()

    val timer = StepTimer()

    while (!Screen.shouldClose()) {
      timer.tick()

      while (timer.hasTimeRemaining) {
        Screen.pollEvents()
        onIdle(timer.getStepTime)
      }

      render()
    }
  }

  private def onIdle(elapsed: Float): Unit = {
    if (playing && !paused) {
      paddle.update(elapsed, Screen.getCursorPos.x)
      ball.update(elapsed)

      hitTest()

      ball.setSpeed(wall.hits)

      checkWinOrLose()
    }
  }

  private def render(): Unit = {
    Screen.clear()
    QuadRenderer.render(allSprites, camera)
    Screen.swapBuffers()
  }

  private def onKeyUp(key: Int): Unit = {
    if (key == GLFW_KEY_P)
      togglePause()
    if (key == GLFW_KEY_SPACE)
      newGame()
  }

  private def hitTest(): Unit = {
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

  private def checkWinOrLose(): Unit = {
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

  private def togglePause(): Unit = {
    paused = !paused
  }

  private def newGame(): Unit = {
    initEntities()
    paused = false
    playing = true
  }

  private def introScreen(): Unit = {
    initEntities()
    ball.hide()
    paused = true
    playing = false
  }

  private def initEntities(): Unit = {
    scoreboard = new ScoreBoard
    sides = new Sides(WIDTH, HEIGHT)
    wall = new Wall(sides.innerArea, scoreboard)
    paddle = Paddle(sides.innerArea)
    newBall()
  }

  private def newBall(): Unit = {
    ball = new Ball(Point(WIDTH - (sides.width * 2), wall.lowerBound), Vector2(-0.5f, 0.5f).normalize(), sides.innerArea, wall)
  }

  private def allSprites = {
    scoreboard.sprites ++
    sides.sprites ++
    wall.sprites ++
    paddle.sprites ++
    ball.sprites
  }
}
