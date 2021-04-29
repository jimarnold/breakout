package game

import mafs._
import org.lwjgl.glfw._
import org.lwjgl.opengl._
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.system.MemoryUtil._
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL30.{glBindVertexArray, glDeleteVertexArrays, glGenVertexArrays}

import scala.collection.mutable

object Breakout {
  private var wall: Wall              = _
  private var sides: Sides            = _
  private var paddle: Paddle          = _
  private var ball: Ball              = _
  private var scoreboard: ScoreBoard  = _
  private val pressed                 = collection.mutable.Set.empty[Int]
  private var lastTick                = 0L
  private var lives                   = 5
  private var canHitBricks            = true
  private var paused                  = true
  private var playing                 = false

  val WIDTH: Int = 1000
  val HEIGHT: Int = 700
  val projectionMatrix: Matrix4 = Matrix4.ortho(0f, WIDTH, HEIGHT, 0f, -1f, 1f)

  var quadVbo: Int = _
  var vao: Int = 0
  var cameraToClipMatrixUniform: Int = 0
  var colorUniform: Int = 0
  var window: Long = 0
  var program: Int = 0

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
    val kb = new KeyboardHandler(pressed)
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

    val vs = NewShader(GL_VERTEX_SHADER, """#version 400
    in vec2 position;
    out vec4 fs_color;
    uniform vec4 color;
    uniform mat4 cameraToClipMatrix;
    void main() {
        gl_Position = cameraToClipMatrix * vec4(position.x, position.y, 0.0, 1.0);
        fs_color = color;
    }""")

    val fs = NewShader(GL_FRAGMENT_SHADER, """#version 400
    in vec4 fs_color;
    out vec4 frag_color;
    void main(void) {
      frag_color = fs_color;
    }""")

    program = NewProgram(vs, fs)

    quadVbo = glGenBuffers
    val width = 1f
    val height = 1f
    val left = 0f
    val top = 0f

    val vertices = rect(left, top, width, height)

    glBindBuffer(GL_ARRAY_BUFFER, quadVbo)
    glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip, GL_STATIC_DRAW)

    vao = glGenVertexArrays()
    glBindVertexArray(vao)
    glEnableVertexAttribArray(0)
    glBindBuffer(GL_ARRAY_BUFFER, quadVbo)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, NULL)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    this.cameraToClipMatrixUniform = glGetUniformLocation(program, "cameraToClipMatrix")
    this.colorUniform = glGetUniformLocation(program, "color")
  }

  private def rect(left: Float, top: Float, width: Float, height: Float): Array[Float] = {
     Array(
      left, top,
      left, top + height,
      left + width, top,
      left, top + height,
      left + width, top + height,
      left + width, top
    )
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

  def keyPressed(key: Int): Boolean =
    pressed.contains(key)

  def onIdle(elapsed: Float): Unit = {
    if (playing && !paused) {
      val x = new Array[Double](1)
      val y = new Array[Double](1)
      glfwGetCursorPos(window, x, y)

      paddle.update(elapsed, x(0).toInt)
      ball.update(elapsed)
      ball.setSpeed(wall.hits)

      hitTest()
      checkWinOrLose()
    }
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
    paddle = new Paddle(Vector2(WIDTH / 2, HEIGHT - 20), gameField)
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
      if (keyPressed(GLFW_KEY_P)) {
        paused = !paused
      }
      if (keyPressed(GLFW_KEY_SPACE)) {
        newGame()
      }

      glfwPollEvents()
      glClearColor(0.0f, 0.0f, 0.01f, 1)
      glClear(GL_COLOR_BUFFER_BIT)

      onIdle(elapsed)

      glUseProgram(program)

      scoreboard.sprites ++
      sides.sprites ++
      wall.sprites ++
      paddle.sprites ++
      ball.sprites foreach(s => renderSprite(s))

      glUseProgram(0)
      glfwSwapBuffers(window)
    }

    glDeleteVertexArrays(this.vao)
    glDeleteBuffers(this.quadVbo)
    glDeleteProgram(this.program)
  }

  private def renderSprite(sprite: Sprite): Unit = {
    glBindVertexArray(vao)
    val spriteTransform = sprite.transformMatrix()
    val clipMatrix = spriteTransform mult projectionMatrix
    val clipMatrixArray = clipMatrix.toa()
    glUniformMatrix4fv(this.cameraToClipMatrixUniform, false, clipMatrixArray)
    glUniform4fv(this.colorUniform, sprite.color.toa())
    glDrawArrays(GL_TRIANGLES, 0, 6)
  }

  def NewProgram(vs: Int, fs: Int): Int = {
    val program = glCreateProgram()

    glAttachShader(program, vs)
    glAttachShader(program, fs)
    glLinkProgram(program)
    val linked = glGetProgrami(program, GL_LINK_STATUS)
    if (linked == GL_FALSE) {
      println(glGetProgramInfoLog(program))
      throw new AssertionError("Could not link program")
    }
    glDeleteShader(vs)
    glDeleteShader(fs)
    program
  }

  def NewShader(shaderType: Int, source: String) : Int = {
    val s = glCreateShader(shaderType)
    glShaderSource(s, source)
    glCompileShader(s)
    val status = glGetShaderi(s, GL_COMPILE_STATUS)
    if (status == GL_FALSE) {
      println(glGetShaderInfoLog(s))
    }
    s
  }
}

class KeyboardHandler(val pressed: mutable.Set[Int]) extends GLFWKeyCallback {
  def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
      glfwSetWindowShouldClose(window, true)
    if (action == GLFW_PRESS) {
      pressed += key
    } else if(action == GLFW_RELEASE)
      pressed -= key
  }
}
