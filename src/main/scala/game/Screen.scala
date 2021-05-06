package game
import mafs.{Point, Vector2}
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWKeyCallback, GLFWVidMode}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil.NULL

object Screen {
  var window: Long = 0L

  def clear(): Unit = {
    glClearColor(0.0f, 0.0f, 0.01f, 1)
    glClear(GL_COLOR_BUFFER_BIT)
  }

  def swapBuffers(): Unit = {
    glfwSwapBuffers(window)
  }

  def pollEvents(): Unit = {
    glfwPollEvents()
  }

  val cursorX = new Array[Double](1)
  val cursorY = new Array[Double](1)

  def getCursorPos: Point = {
    glfwGetCursorPos(window, cursorX, cursorY)
    Point(cursorX(0).toFloat, cursorY(0).toFloat)
  }

  def frameBufferSize(): Vector2 = {
    val fbWidth = new Array[Int](1)
    val fbHeight = new Array[Int](1)
    glfwGetFramebufferSize(window, fbWidth, fbHeight)
    Vector2(fbWidth(0), fbHeight(0))
  }

  def shouldClose(): Boolean = {
    glfwWindowShouldClose(window)
  }

  def init(width: Int, height: Int, onKeyUp : Int => Unit): Unit = {
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
    glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE)

    window = glfwCreateWindow(width, height, "BREAKOUT", NULL, NULL)

    if (window == NULL)
      throw new RuntimeException("Failed to create the GLFW window!")

    glfwSetKeyCallback(window, KeyboardHandler(onKeyUp))
    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN)

    val monitor = glfwGetPrimaryMonitor()
    val videoMode: GLFWVidMode = glfwGetVideoMode(monitor)

    glfwSetWindowPos(
      window,
      (videoMode.width() - width) / 2,
      (videoMode.height() - height) / 2)

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)
    glfwShowWindow(window)

    GL.createCapabilities()

    val size = frameBufferSize()
    glViewport(0, 0, size.x.toInt, size.y.toInt)
  }

  def destroy(): Unit = {
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}

case class KeyboardHandler(onKeyUp: Int => Unit) extends GLFWKeyCallback {
  def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
    if (action == GLFW_RELEASE) {
      onKeyUp(key)

      if (key == GLFW_KEY_ESCAPE) {
        glfwSetWindowShouldClose(window, true)
      }
    }
  }
}
