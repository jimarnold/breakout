package game.graphics.renderers

import game.graphics.{Camera, FragmentShader, Program, Sprite, Uniform, VertexArray, VertexBuffer, VertexShader}
import org.lwjgl.opengl.GL11.{GL_FLOAT, GL_TRIANGLES, glDrawArrays}
import org.lwjgl.opengl.GL20._
import org.lwjgl.system.MemoryUtil.NULL

object QuadRenderer {
  var program: Program = _
  var vbo: VertexBuffer = _
  var vao: VertexArray = _
  var viewMatrixUniform: Uniform = _
  var colorUniform: Uniform = _

  def render(sprites: Seq[Sprite], camera: Camera): Unit = {
    program.use()
    vao.bind()

    sprites.foreach(sprite => {
      this.viewMatrixUniform.loadMatrix(camera.view(sprite))
      this.colorUniform.loadArray(sprite.color.rgba)
      glDrawArrays(GL_TRIANGLES, 0, 6)
    })
  }

  def init(): Unit = {
    program = Program(VertexShader.Simple2D, FragmentShader.SimpleColor)

    vbo = VertexBuffer()
    vbo.bind()

    vbo.load(vertices())

    vao = VertexArray()
    vao.bind()
    vao.enable()

    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, NULL)
    vbo.unbind()

    this.viewMatrixUniform = program.getUniform("viewMatrix")
    this.colorUniform = program.getUniform( "color")
  }

  def destroy(): Unit = {
    vao.delete()
    vbo.delete()
    program.delete()
  }

  private def vertices(): Array[Float] = {
    val left = 0f
    val top = 0f
    val width = 1f
    val height = 1f

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
