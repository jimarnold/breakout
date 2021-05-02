package game.graphics.renderers

import game.graphics.{Camera, FragmentShader, Program, Sprite, VertexArray, VertexBuffer, VertexShader}
import org.lwjgl.opengl.GL11.{GL_FLOAT, GL_TRIANGLES, glDrawArrays}
import org.lwjgl.opengl.GL20._
import org.lwjgl.system.MemoryUtil.NULL

object Quad {
  var program: Program = _
  var vbo: VertexBuffer = _
  var vao: VertexArray = _
  var viewMatrixUniform: Int = 0
  var colorUniform: Int = 0

  def render(sprites: Seq[Sprite], camera: Camera): Unit = {
    program.use()
    vao.bind()

    sprites.foreach(sprite => {
      glUniformMatrix4fv(this.viewMatrixUniform, false, camera.view(sprite))
      glUniform4fv(this.colorUniform, sprite.color.rgba)
      glDrawArrays(GL_TRIANGLES, 0, 6)
    })
  }

  def destroy(): Unit = {
    vao.delete()
    vbo.delete()
    program.delete()
  }

  def init(): Unit = {
    program = Program(VertexShader.Simple2D, FragmentShader.SimpleColor)

    val width = 1f
    val height = 1f
    val left = 0f
    val top = 0f

    val vertices = rect(left, top, width, height)

    vbo = VertexBuffer()
    vbo.bind()

    vbo.load(vertices)

    vao = VertexArray()
    vao.bind()
    vao.enable()

    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, NULL)
    vbo.unbind()

    this.viewMatrixUniform = glGetUniformLocation(program.ptr, "viewMatrix")
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
