package game.graphics

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20._

case class Shader(ptr: Int) {
}

object Shader {
  def create(shaderType: Int, source: String): Shader = {
    val s = glCreateShader(shaderType)
    glShaderSource(s, source)
    glCompileShader(s)
    val status = glGetShaderi(s, GL_COMPILE_STATUS)
    if (status == GL_FALSE) {
      println(glGetShaderInfoLog(s))
    }
    Shader(s)
  }
}

object VertexShader {
  val Simple2D: Shader = Shader.create(GL_VERTEX_SHADER,
    """#version 400
    in vec2 position;
    out vec4 fs_color;
    uniform vec4 color;
    uniform mat4 viewMatrix;
    void main() {
        gl_Position = viewMatrix * vec4(position.x, position.y, 0.0, 1.0);
        fs_color = color;
    }""")
}

object FragmentShader {
  val SimpleColor: Shader = Shader.create(GL_FRAGMENT_SHADER,
    """#version 400
    in vec4 fs_color;
    out vec4 frag_color;
    void main(void) {
      frag_color = fs_color;
    }""")
}
