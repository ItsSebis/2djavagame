package net.sebis.game;

import net.sebis.game.entity.Player;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // Screen Settings
    final int originalTileSize = 16; // 16x16 tiles
    final int scale = 3;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;

    final int tileSize = originalTileSize * scale; // 48x48 tiles
    final int screenWidth = tileSize * maxScreenCol; // 768px
    final int screenHeight = tileSize * maxScreenRow; // 576px

    // FPS
    int fps = 60;
    int realFPS = fps;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Player player = new Player(this, keyH);

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        player.draw(g2);
        g2.drawString(String.valueOf(realFPS), 10, 10);
        g2.dispose();

    }

    @Override
    public void run() {

        double drawInterval = 1000000000D/fps; // 0.016s
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // UPDATE: update local information about players and obstacles
                update();

                // DRAW: draw changes on screen
                repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                realFPS = drawCount;
                timer = 0;
                drawCount = 0;
            }
        }
    }

    public int getTileSize() {
        return tileSize;
    }
}
