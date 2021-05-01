package game

import game.audio.Sound
import game.entity.{Ball, Paddle, ScoreBoard, SideHitResult, Sides, Wall}
import game.graphics.renderers.Quad
import mafs._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._
import org.lwjgl.system.MemoryUtil._

object Breakout {

  private var wall: Wall              = _
  private var sides: Sides            = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var scoreboard: ScoreBoard  = _
  private var lastTick                = 0L
  private var lives                   = 5
  private var canHitBricks            = true
  private var paused                  = true
  private var playing                 = false

  val WIDTH: Int = 1080
  val HEIGHT: Int = 780
  val projectionMatrix: Matrix4 = Matrix4.ortho(0f, WIDTH, HEIGHT, 0f, -1f, 1f)

  var window: Long = 0

  def run() {
    try {
      Sound.init()

      initWindow()
      initGraphics()
      loop()

      // Free the window callbacks and destroy the window
      glfwFreeCallbacks(window)
      glfwDestroyWindow(window)
    }
    finally {
      Sound.destroy()
      // Terminate GLFW and free the error callback
      glfwTerminate()
      glfwSetErrorCallback(null).free()
    }
  }

  private def initWindow() {
    // Setup an error callback. The default implementation
    // will print the error message in System.err.
    GLFWErrorCallback.createPrint(System.err).set()

    // Initialize GLFW. Most GLFW functions will not work before doing this.
    if (!glfwInit())
      throw new IllegalStateException("Unable to initialize GLFW")

    glfwDefaultWindowHints() // optional, the current window hints are already the default
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE)
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

    window = glfwCreateWindow(WIDTH, HEIGHT, "BREAKOUT", NULL, NULL)

    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window!")

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    val kb = new KeyboardHandler()
    glfwSetKeyCallback(window, kb)
    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)

    val vidmode: GLFWVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
    // Center our window
    glfwSetWindowPos(
      window,
      (vidmode.width() - WIDTH) / 2,
      (vidmode.height() - HEIGHT) / 2)

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)
  }

  def initGraphics(): Unit = {
    GL.createCapabilities()

    val fbWidth = new Array[Int](1)
    val fbHeight = new Array[Int](1)
    glfwGetFramebufferSize(window, fbWidth, fbHeight)
    glViewport(0, 0, fbWidth(0), fbHeight(0))
    Quad.init()
  }

  def hitTest(): Unit = {
    if (paddle.contains(ball)) {
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

  def checkWinOrLose(): Unit = {
    if (ball.position.y >= HEIGHT) {
      lives-= 1
      scoreboard.setLives(lives)
      newBall()
    }
    if (lives == 0 || wall.isDestroyed) {
      ball.hide()
      paused = true
      playing = false
    }
  }

  def onIdle(elapsed: Float): Unit = {
    if (playing && !paused) {
      paddle.update(elapsed, getCursorXPosition)
      ball.update(elapsed)
      ball.setSpeed(wall.hits)

      hitTest()
      checkWinOrLose()
    }
  }

  def togglePause(): Unit = {
    paused = !paused
  }

  val cursorX = new Array[Double](1)
  val cursorY = new Array[Double](1)
  private def getCursorXPosition = {
    glfwGetCursorPos(window, cursorX, cursorY)
    cursorX(0).toInt
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
    lives = 5
    scoreboard = new ScoreBoard
    scoreboard.setLives(lives)
    sides = new Sides(WIDTH, HEIGHT)
    val gameField = Rect(sides.width, sides.width * 3, WIDTH - (sides.width * 2), HEIGHT - sides.width)
    wall = new Wall(gameField, scoreboard)
    paddle = Paddle(Vector2(WIDTH / 2, HEIGHT - 20), gameField)
    newBall()
  }

  def newBall(): Unit = {
    ball = new Ball(Vector2(WIDTH - (sides.width * 2), wall.lowerBound), Vector2(-0.5f, 0.5f), wall)
  }

  def loop() {
    introScreen()

    while (!glfwWindowShouldClose(window)) {
      val now = System.nanoTime()
      val elapsed = (now - lastTick).toFloat / 1000000000
      lastTick = now

      glfwPollEvents()
      glClearColor(0.0f, 0.0f, 0.01f, 1)
      glClear(GL_COLOR_BUFFER_BIT)

      onIdle(elapsed)

      allSprites foreach(sprite => Quad.render(projectionMatrix, sprite))

      glfwSwapBuffers(window)
    }
  }

  private def allSprites = {
    scoreboard.sprites ++
    sides.sprites ++
    wall.sprites ++
    paddle.sprites ++
    ball.sprites
  }
}

class KeyboardHandler() extends GLFWKeyCallback {
  def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      glfwSetWindowShouldClose(window, true)
    if (key == GLFW_KEY_P && action == GLFW_RELEASE)
      Breakout.togglePause()
    if (key == GLFW_KEY_SPACE && action == GLFW_RELEASE)
      Breakout.newGame()
  }
}
