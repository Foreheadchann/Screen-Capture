package me.hideri.render;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Window
{
    public final ScreenImageCapture imageCapture = new ScreenImageCapture();
    private final List<BufferedImage> frameBuffer = new ArrayList<>();
    private final BufferedImage cursor = imageCapture.getImage("assets/Cursor.png");
    public BufferedImage latestImage;
    private int x, y, width, height;
    private int mouseX, mouseY;
    public JFrame window;

    private boolean pressed;

    public Window(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.x = 25;
        this.y = 25;
    }

    public void initWindow()
    {
        window = new JFrame();
        window.setResizable(false);
        window.setBounds(x, y, width, height);
        window.setUndecorated(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(new Color(0, 0, 0));
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        this.initMouseEvents();
    }

    public void initMouseEvents()
    {
        final Point[] point = new Point[1];

        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point[0] = e.getPoint();
                pressed = true;
            }
        });

        window.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(pressed)
                {
                    setPos(e.getXOnScreen() - point[0].x, e.getYOnScreen() - point[0].y, width, height);
                }
            }
        });
    }

    public void drawCursor(Graphics graphics, int x, int y)
    {
        this.setMousePos((int) Math.round(x / ((double) this.width / (double) this.height)), (int) Math.round(y / ((double) this.width / (double) this.height)));
        System.out.println("@" + mouseX + "," + mouseY);
        final Graphics2D g2d = (Graphics2D) graphics;
        graphics.drawImage(cursor, mouseX, mouseY, null);
        g2d.dispose();
        graphics.dispose();
    }

    public void drawImage(Graphics graphics, boolean scaled)
    {
        this.newImage();

        if (scaled)
        {
            this.drawScaledScreen(graphics);
        }
        else
        {
            this.drawScreen(graphics);
        }
    }

    public void drawScreen(Graphics graphics)
    {
        graphics.drawImage(latestImage, 0, 0, null);
        graphics.dispose();
    }

    public void drawScaledScreen(Graphics graphics)
    {
        final Graphics2D g2d = (Graphics2D) graphics;
        this.latestImage = frameBuffer.get(frameBuffer.size() - 1);
        g2d.drawImage(latestImage, 0, 0, width, height, new Color(0, 0, 0), null);
        g2d.dispose();
        graphics.dispose();
    }

    public void newImage()
    {
        if(frameBuffer.size() > 16)
        {
            this.clearFrameBuffer();
        }

        frameBuffer.add(imageCapture.getScreenImage());
    }

    public void clearFrameBuffer()
    {
        frameBuffer.clear();
    }

    public void drawStringOnScreen(Graphics graphics, String text, int x, int y)
    {
        final Graphics2D g2d = (Graphics2D) graphics;
        g2d.drawString(text, x, y);
        g2d.dispose();
        graphics.dispose();
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        window.setBounds(x, y, width, height);
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        window.setBounds(x, y, width, height);
        this.height = height;
    }

    public void setPos(int x, int y, int width, int height)
    {
        window.setBounds(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setMousePos(int x, int y)
    {
        this.mouseX = x;
        this.mouseY = y;
    }
}
