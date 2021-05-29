package me.hideri;

import me.hideri.thread.RenderThread;
import me.hideri.util.RenderUtil;

public class Main
{
    public static final RenderThread thread = new RenderThread();

    public static void main(String[] args)
    {
        RenderUtil.enableHWAcceleration();
        thread.initRenderThread();
    }
}
