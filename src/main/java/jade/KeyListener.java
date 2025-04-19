package jade;

import org.lwjgl.system.CallbackI;

import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener(){
        HashSet<Integer> i = new HashSet<>();
        Scanner sc= new Scanner(System.in);
        sc.next(Pattern.compile(" "));
        String s = "hell wotl ";
        int length = s.split("").length;    
    }

    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }
    public static void keyCallback(long window,int key,int scancode,int action,int mods){
        if (action == GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }
    public static boolean isKeyPressed(int keyCode){
        if (keyCode < get().keyPressed.length){
            return get().keyPressed[keyCode];
        }else {
            return false;
        }
    }
}
