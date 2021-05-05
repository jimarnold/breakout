package game
import mafs.Vector2
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.{GLFWErrorCallback, GLFWKeyCallback, GLFWVidMode}
import org.lwjgl.opengl.GL11.GL_TRUE
import org.lwjgl.system.MemoryUtil.NULL

object GLFW {
  var window: Long = 0L

  def swapBuffers(): Unit = {
    glfwSwapBuffers(window)
  }

  def pollEvents(): Unit = {
    glfwPollEvents()
  }

  val cursorX = new Array[Double](1)
  val cursorY = new Array[Double](1)

  def getCursorPos: Vector2 = {
    glfwGetCursorPos(window, cursorX, cursorY)
    Vector2(cursorX(0).toFloat, cursorY(0).toFloat)
  }

  def frameBufferSize(): Vector2 = {
    val fbWidth = new Array[Int](1)
    val fbHeight = new Array[Int](1)
    glfwGetFramebufferSize(window, fbWidth, fbHeight)
    Vector2(fbWidth(0), fbHeight(0))
  }

  def windowShouldClose(): Boolean = {
    glfwWindowShouldClose(window)
  }

  def init(width: Int, height: Int): Long = {
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

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    val kb = new KeyboardHandler()
    glfwSetKeyCallback(window, kb)
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

    window
  }

  def destroy(): Unit = {
    glfwFreeCallbacks(window)
    glfwDestroyWindow(window)
    glfwTerminate()
    glfwSetErrorCallback(null).free()
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
