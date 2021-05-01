package game.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

case class Program(ptr: Int) {
  def delete(): Unit = glDeleteProgram(ptr)
  def use(): Unit = glUseProgram(ptr)
}

object Program {
  def apply(vs: VertexShader, fs: FragmentShader): Program = {
    val program = glCreateProgram()

    glAttachShader(program, vs.ptr)
    glAttachShader(program, fs.ptr)
    glLinkProgram(program)
    val linked = glGetProgrami(program, GL_LINK_STATUS)
    if (linked == GL_FALSE) {
      println(glGetProgramInfoLog(program))
      throw new AssertionError("Could not link program")
    }
    glDeleteShader(vs.ptr)
    glDeleteShader(fs.ptr)
    Program(program)
  }
}
