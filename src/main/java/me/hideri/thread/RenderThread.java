package me.hideri.thread;

import me.hideri.render.Window;

import java.awt.*;

public class RenderThread
{
    public static final Window window = new Window(350, 25, 1280, 720, 0, 0, 300, 500);
    public static final long frame_delay = 0;

    public void initRenderThread()
    {
        final Thread renderThread = new Thread(new Render());
        renderThread.setPriority(Thread.MAX_PRIORITY);
        window.initSettingWindow();
        window.initWindow();
        renderThread.start();
    }

    private void renderAllWindowObjects()
    {
        final Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        window.drawImage(window.window.getGraphics(), true);
        window.drawCursor(window.window.getGraphics(), mouseLoc.x, mouseLoc.y);
        window.drawSettingStrings();
    }

    public class Render implements Runnable
    {
        @Override
        public void run()
        {
            while(true)
            {
                renderAllWindowObjects();

                try
                {
                    Thread.sleep(frame_delay);
                    Thread.yield();
                }
                catch (InterruptedException ignored)
                {

                }
            }
        }
    }
}
