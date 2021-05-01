package game.graphics.renderers

import game.graphics.{FragmentShader, Program, Sprite, VertexShader}
import mafs.Matrix4
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.{GL_FLOAT, GL_TRIANGLES, glDrawArrays}
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.{glBindVertexArray, glDeleteVertexArrays, glGenVertexArrays}
import org.lwjgl.system.MemoryUtil.NULL

object Quad {
  var cameraToClipMatrixUniform: Int = 0
  var program: Program = _
  var colorUniform: Int = 0
  var vbo: Int = _
  var vao: Int = 0

  def render(camera: Matrix4, sprite: Sprite): Unit = {
    glUseProgram(program.ptr)
    glBindVertexArray(vao)
    val spriteTransform = sprite.transformMatrix()
    val clipMatrix = spriteTransform mult camera
    val clipMatrixArray = clipMatrix.toa()
    glUniformMatrix4fv(this.cameraToClipMatrixUniform, false, clipMatrixArray)
    glUniform4fv(this.colorUniform, sprite.color.toa())
    glDrawArrays(GL_TRIANGLES, 0, 6)
    glUseProgram(0)
  }

  def destroy(): Unit = {
    glDeleteVertexArrays(this.vao)
    glDeleteBuffers(this.vbo)
    glDeleteProgram(this.program.ptr)
  }

  def init(): Unit = {
    program = Program(VertexShader.Simple2D, FragmentShader.SimpleColor)

    val width = 1f
    val height = 1f
    val left = 0f
    val top = 0f

    val vertices = rect(left, top, width, height)

    vbo = glGenBuffers
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip, GL_STATIC_DRAW)

    vao = glGenVertexArrays()
    glBindVertexArray(vao)
    glEnableVertexAttribArray(0)
    glBindBuffer(GL_ARRAY_BUFFER, vbo)
    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, NULL)
    glBindBuffer(GL_ARRAY_BUFFER, 0)

    this.cameraToClipMatrixUniform = glGetUniformLocation(program.ptr, "cameraToClipMatrix")
    this.colorUniform = glGetUniformLocation(program.ptr, "color")
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
}
