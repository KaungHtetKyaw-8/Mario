package jade;

import components.SpriteRenderer;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

    public LevelEditorScene(){

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f(-250,-100));

        GameObject obj1 = new GameObject("Object1",new Transform(new Vector2f(100,100),new Vector2f(256,256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testimage1.png")));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object2",new Transform(new Vector2f(400,100),new Vector2f(256,265)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("assets/images/testimage2.png")));
        this.addGameObjectToScene(obj2);

        loadResources();
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    @Override
    public void update(float dt) {
        System.out.println("FPS : " + (1.0 / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
