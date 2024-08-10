package util;

public class Time {
    public static float timeStarted = System.nanoTime();
   // 56.52
    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }

}
