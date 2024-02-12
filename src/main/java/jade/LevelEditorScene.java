package jade;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private float[] vertexArray = {
            // position            // color                // UV Coordinates
             100f,   0f, 0.0f,     1.0f,0.0f,0.0f,1.0f,    1, 1,  // Bottom Right 0
               0f, 100f, 0.0f,     0.0f,1.0f,0.0f,1.0f,    0, 0,  // Top Left     1
             100f, 100f, 0.0f,     1.0f,0.0f,1.0f,1.0f,    1, 0,  // Top Right    2
               0f,   0f, 0.0f,     1.0f,1.0f,0.0f,1.0f,    0, 1,  // Bottom left  3
    };

    // IMPORTANT : Must be in counter-clockwisse order
    private int[] elementArray = {
            /*
                    x       x


                    x       x
             */
            2,1,0, // Top Right triangle
            0,1,3  // Bottom Left triangle
    };

    private int vaoID,vboID,eboID;

    private Shader defaultShader;

    private Texture textTexture;

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-100.0f,-100.0f));
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        this.textTexture = new Texture("assets/images/testImage1.png");


        // =============================================================
        // Generate VAO, VBO, and EBO buffer object, and send to GPU
        // =============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        // Crate the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,elementBuffer,GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0,positionSize,GL_FLOAT,false,vertexSizeBytes,0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1,colorSize,GL_FLOAT,false,vertexSizeBytes,positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2,uvSize,GL_FLOAT,false,vertexSizeBytes,(positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {

        //camera.position.x -= dt * 50.0f;
        //camera.position.y -= dt * 20.0f;

        defaultShader.use();

        // Upload Texture to Shader
        defaultShader.uploadTexture("TEX_SAMPLER",0);
        glActiveTexture(GL_TEXTURE0);
        textTexture.bind();

        defaultShader.uploadMat4f("uProjection",camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView",camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES,elementArray.length,GL_UNSIGNED_INT,0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
