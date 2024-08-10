package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene{

    private boolean changingScene = false;

    private float timeToChangeScene = 2;

    public LevelEditorScene(){
        System.out.println("LevelEditorScene");
    }

    @Override
    public void update(float dt) {
        System.out.println("We are running at " + (1f / dt ) + "fps");


        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0 ){
            timeToChangeScene = timeToChangeScene - dt;
            Window.get().r -= dt * 5.0F;
            Window.get().g -= dt * 5.0F;
            Window.get().b -= dt * 5.0F;

        }else if (changingScene){
            Window.changeScene(1);
        }
    }
}
