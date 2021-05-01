package game.graphics

import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL30.{glBindVertexArray, glDeleteVertexArrays, glGenVertexArrays}

case class VertexArray(ptr: Int) {
  def bind(): Unit = glBindVertexArray(ptr)
  def delete(): Unit = glDeleteVertexArrays(ptr)
  def enable(): Unit = glEnableVertexAttribArray(0)
}

object VertexArray {
  def apply(): VertexArray = {
    VertexArray(glGenVertexArrays())
  }
}
