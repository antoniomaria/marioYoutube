package jade;

import static org.lwjgl.glfw.GLFW.*;

// Min 41.12
// https://www.glfw.org/docs/3.3/input_guide.html#input_keyboard
public class KeyListener {

    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (instance == null) {
            KeyListener.instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keycode){
        return get().keyPressed[keycode];
    }
}
