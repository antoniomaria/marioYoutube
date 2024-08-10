package jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene{

    private String vertexShadersSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "\n" +
            "    color = fColor;\n" +
            "\n" +
            "}";


    private int vertexID, fragmentId, shaderProgram;

    // vertex array object, vertex buffer object, element buffer object
   private int vaoId, vboId, eboId;

    private float [] vertexAarray = {
        // position               //color
            0.5f, -0.5f, 0f,      1.0f, 0, 0, 1.0f, // bottom right     0
            -0.5f, 0.5f, 0f,      0.0f, 1.0f, 0, 1.0f, // top left      1
            0.5f, 0.5f, 0f,       0.0f, 0.0f, 1f, 1.0f, // top right    2
            -0.5f, -0.5f, 0f,     1.0f, 1.0f, 0f, 1.0f, // bottom left  3
    };

    // IMPORTANT: MUST BE IN COUNTER-clockwise order
    private int [] elementArray = {
            /*
                   x               x


                   x               x
            */
            2,1,0, // top right triangle
            0,1,3 // bottom left triangle
    };

    public LevelEditorScene(){
        System.out.println("LevelEditorScene");
    }

    @Override
    public void init() {
        // Compile and link the shaders

        // first load and compile the vertext shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // pass the shader to the gpu
        glShaderSource(vertexID, vertexShadersSrc);
        glCompileShader(vertexID);

        // check for errors in compilation


        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE){
            System.err.println("eRror compiling the shader vertex shader");

            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);

            System.err.println("ERROR COMPILING: default.glsl. Vertex shader");
            System.err.println(glGetShaderInfoLog(vertexID, len));
            throw new IllegalStateException("ERROR COMPILING: default.glsl. Vertex shader");
        }


        // compile the next thing

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);

        // pass the shader to the gpu
        glShaderSource(fragmentId, fragmentShaderSrc);
        glCompileShader(fragmentId);

        // check for errors in compilation


         success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);

        if (success == GL_FALSE){
            System.err.println("eRror compiling the shader vertex shader");

            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);

            System.err.println("ERROR COMPILING: default.glsl. fragment shader");
            System.err.println(glGetShaderInfoLog(fragmentId, len));
            throw new IllegalStateException("ERROR COMPILING: default.glsl. fragment shader");
        }

        // link shaders and check for errors

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.err.println("ERROR: default.glsl. Linking shader failed");
            System.err.println(glGetProgramInfoLog(shaderProgram, len));
            throw new IllegalStateException("ERROR linking: default.glsl. program shader");

        }

        // GENERATE VAO, VBO, AND EBO bufffer objects and send to GPU

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // create a float buffer of vertices

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexAarray.length);

        vertexBuffer.put(vertexAarray).flip();


        // crate VBO upload the vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // crate the indices and upload

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add the vertex attribute pointers
        // array bits decoder
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize )* floatSizeBytes;
        // stride how long until the next vertex
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        // bind shader program
        glUseProgram(shaderProgram);

        // bind the VAO that we are using
        glBindVertexArray(vaoId);

        // enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind everything

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);

    }
}
