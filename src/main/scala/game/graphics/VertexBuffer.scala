package game.graphics

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL15.{GL_ARRAY_BUFFER, GL_STATIC_DRAW, glBindBuffer, glBufferData, glDeleteBuffers, glGenBuffers}

case class VertexBuffer(ptr: Int) {
  def load(vertices: Array[Float]): Unit = {
    glBufferData(GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip, GL_STATIC_DRAW)
  }
  def unbind(): Unit = glBindBuffer(GL_ARRAY_BUFFER, 0)
  def bind(): Unit = glBindBuffer(GL_ARRAY_BUFFER, ptr)
  def delete(): Unit = glDeleteBuffers(ptr)
}

object VertexBuffer {
  def apply(): VertexBuffer = {
    VertexBuffer(glGenBuffers)
  }
}
