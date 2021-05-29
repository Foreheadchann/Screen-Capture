package me.hideri.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScreenImageCapture
{
    private final ExecutorService EXEC_POOL = Executors.newFixedThreadPool(2);
    private BufferedImage createdImage;

    public Robot imageRobot;

    public ScreenImageCapture()
    {
        try
        {
            imageRobot = new Robot();
        }
        catch (AWTException e)
        {
            e.printStackTrace();
        }
    }

    public BufferedImage getImage(String path)
    {
        try
        {
            return ImageIO.read(new File(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public BufferedImage getScreenImage()
    {
        return imageRobot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
    }

    public BufferedImage getScreenImage(int x, int y, int width, int height)
    {
        EXEC_POOL.execute(() -> createdImage =  imageRobot.createScreenCapture(new Rectangle(x, y, width, height)));
        return createdImage;
    }
}
