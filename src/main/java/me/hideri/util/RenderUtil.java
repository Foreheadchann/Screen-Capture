package me.hideri.util;

public class RenderUtil
{
    public static void enableHWAcceleration()
    {
        System.setProperty("sun.java2d.opengl", "true");
    }

    public static void disableHWAcceleration()
    {
        System.setProperty("sun.java2d.opengl", "false");
    }

    public static int normalize(int val, int min, int max)
    {
        return (val - min) / (max - min);
    }

    public static int denormalize(int val, int min, int max)
    {
        return val * (max - min) + min;
    }
}
