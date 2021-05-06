package game.graphics

import mafs.Matrix4
import org.lwjgl.opengl.GL20.{glUniform4fv, glUniformMatrix4fv}

case class Uniform(ptr: Int) {
  def loadMatrix(matrix: Matrix4): Unit = {
    glUniformMatrix4fv(ptr, false, matrix.toa())
  }

  def loadArray(a: Array[Float]): Unit = {
    glUniform4fv(ptr, a)
  }
}
