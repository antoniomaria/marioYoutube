package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWAllocateCallback;
import org.lwjgl.glfw.Callbacks.*;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;


public class Window {

    int width,height;
    String title;
    private static Window window = null;
    private long glfwWindow;

    private Window(){
        this.width = 1920 /2;
        this.height = 1080/2;
        this.title = "Mario";
    }

    public static Window get(){
        if (window == null){
            Window.window = new Window();
        }
        return window;
    }

    public void run() {
        System.out.println("Hello LWJGL "+ Version.getVersion() +" !");
        
        init();
        loop();

        // free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // terminate glfw and the free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

    }



    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to init");
        }

        // configure glfw

        glfwDefaultWindowHints();

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE );
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE );
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE );

        // create the window
        glfwWindow = glfwCreateWindow(this.width, height, this.title, NULL, NULL );

        if (glfwWindow == NULL){
            throw new IllegalStateException("failed to create the window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // keyboard
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback );


        // make the opengl context current

        glfwMakeContextCurrent(glfwWindow);
        // enable v sync
        glfwSwapInterval(1);

        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    private void loop() {

        while(!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents();

            glClearColor(0.0f, 1.0f,0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
                System.out.println("SPACE KEY is pressed!");
            }

            glfwSwapBuffers(glfwWindow);


        }
    }
}
