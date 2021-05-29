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

    private int setting_x, setting_y, setting_width, setting_height;
    private int x, y, width, height;
    private int mouseX, mouseY;

    public JFrame settingWindow;
    public JFrame window;

    private JTextField widthField;
    private JTextField heightField;
    private JButton setSize;

    private boolean pressed;

    public Window(int x, int y, int width, int height, int setting_x, int setting_y, int setting_width, int setting_height)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.setting_width = setting_width + 16;
        this.setting_height = setting_height + 39;
        this.setting_x = setting_x;
        this.setting_y = setting_y;
    }

    public void initWindow()
    {
        window = new JFrame("Screen Capture");
        window.setResizable(false);
        window.setBounds(x, y, width, height);
        window.setUndecorated(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setBackground(new Color(0, 0, 0));
        window.setVisible(true);
        window.setAlwaysOnTop(true);
        this.initMouseEvents();
    }

    public void initSettingWindow()
    {
        settingWindow = new JFrame("Settings");
        settingWindow.setResizable(false);
        settingWindow.setBounds(setting_x, setting_y, setting_width, setting_height);
        settingWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        settingWindow.setBackground(new Color(0, 0, 0));
        settingWindow.repaint();
        settingWindow.setLayout(null);
        settingWindow.setVisible(true);
        this.initSettings();
        this.initSettingEvents();
    }

    public void initSettings()
    {
        widthField = new JTextField(String.valueOf(this.getWidth()));
        heightField = new JTextField(String.valueOf(this.getHeight()));
        widthField.setBounds(65, 5, settingWindow.getContentPane().getWidth() - 70, 25);
        heightField.setBounds(65, 35, settingWindow.getContentPane().getWidth() - 70, 25);
        widthField.setVisible(true);
        heightField.setVisible(true);
        settingWindow.getContentPane().add(widthField);
        settingWindow.getContentPane().add(heightField);
        settingWindow.getContentPane().revalidate();
        settingWindow.getContentPane().repaint();

        setSize = new JButton("Set Size");
        setSize.setBounds(5, settingWindow.getContentPane().getHeight() - 25, settingWindow.getContentPane().getWidth() - 10, 20);
        setSize.setVisible(true);
        settingWindow.getContentPane().add(setSize);
    }

    public void drawSettingStrings()
    {
        this.drawStringOnScreen(settingWindow.getContentPane().getGraphics(), "Width", 5, 21);
        this.drawStringOnScreen(settingWindow.getContentPane().getGraphics(), "Height", 5, 51);
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

    public void initSettingEvents()
    {
        widthField.addActionListener(event ->
        {
            String width = widthField.getText();

            if(width.equals("") || width.charAt(0) == ' ')
            {
                return;
            }

            this.setPos(this.x, this.y, Integer.parseInt(width), this.height);
        });

        heightField.addActionListener(event ->
        {
            String height = heightField.getText();

            if(height.equals("") || height.charAt(0) == ' ')
            {
                return;
            }

            this.setPos(this.x, this.y, this.width, Integer.parseInt(height));
        });

        setSize.addActionListener(event ->
        {
            String width = widthField.getText();
            String height = heightField.getText();

            if(height.equals("") || height.charAt(0) == ' ' || width.equals("") || width.charAt(0) == ' ')
            {
                return;
            }

            this.setPos(this.x, this.y, Integer.parseInt(width), Integer.parseInt(height));
        });
    }

    public void drawCursor(Graphics graphics, int x, int y)
    {
        this.setMousePos((int) (x / ((double) window.getWidth() / (double) window.getHeight())), (int) (y / ((double) window.getWidth() / (double) window.getHeight())));
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
        graphics.setFont(new Font("Arial", Font.PLAIN, 14));
        graphics.drawString(text, x, y);
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

    public int getSetting_x()
    {
        return setting_x;
    }

    public void setSetting_x(int setting_x)
    {
        this.setting_x = setting_x;
    }

    public int getSetting_y()
    {
        return setting_y;
    }

    public void setSetting_y(int setting_y)
    {
        this.setting_y = setting_y;
    }

    public int getSetting_width()
    {
        return setting_width;
    }

    public void setSetting_width(int setting_width)
    {
        this.setting_width = setting_width;
    }

    public int getSetting_height()
    {
        return setting_height;
    }

    public void setSetting_height(int setting_height)
    {
        this.setting_height = setting_height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getMouseX()
    {
        return mouseX;
    }

    public void setMouseX(int mouseX)
    {
        this.mouseX = mouseX;
    }

    public int getMouseY()
    {
        return mouseY;
    }

    public void setMouseY(int mouseY)
    {
        this.mouseY = mouseY;
    }

    public void setPos(int x, int y, int width, int height)
    {
        window.setBounds(x, y, width, height);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setSettingPos(int x, int y, int width, int height)
    {
        settingWindow.setBounds(x, y, width, height);
        this.setting_x = x;
        this.setting_y = y;
        this.setting_width = width;
        this.setting_height = height;
    }

    public void setMousePos(int x, int y)
    {
        this.mouseX = x;
        this.mouseY = y;
    }
}
