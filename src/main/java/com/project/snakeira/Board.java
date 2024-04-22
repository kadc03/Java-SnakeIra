package com.project.snakeira;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    
    private final int B_WIDTH = 500;
    private final int B_HEIGHT = 500;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 49;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int score = 0;
    private int apple_x;
    private int apple_y;
    private int speedApple_x;
    private int speedApple_y;
    private int speedAppleCount = 0;

    private boolean speedAppleVisible = false;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private boolean enterPressed = false;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image speedApple;
    private Image headr;
    private Image headl;
    private Image headu;
    private Image headd;

    private Obstacles obstacles;
    
    public Board() {
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        obstacles = new Obstacles(B_WIDTH - 40, B_HEIGHT - 40, DOT_SIZE);
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/images/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/images/apple.png");
        apple = iia.getImage();
        ImageIcon iis = new ImageIcon("src/images/apple.png");
        speedApple = iis.getImage();

        ImageIcon iihr = new ImageIcon("src/images/head_right.png");
        headr = iihr.getImage();
        ImageIcon iihl = new ImageIcon("src/images/head_left.png");
        headl = iihl.getImage();
        ImageIcon iihu = new ImageIcon("src/images/head_up.png");
        headu = iihu.getImage();
        ImageIcon iihd = new ImageIcon("src/images/head_down.png");
        headd = iihd.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();
        locateSpeedApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {
            g.setColor(Color.white);
            g.drawRect(10, 30, (B_WIDTH - 20), (B_HEIGHT - 40));
            g.drawImage(apple, apple_x, apple_y, this);
            if (speedAppleVisible) {
                g.drawImage(speedApple, speedApple_x, speedApple_y, this);
            }

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    if (rightDirection) {
                        g.drawImage(headr, x[z], y[z], this);
                    } else if (leftDirection) {
                        g.drawImage(headl, x[z], y[z], this);
                    } else if (upDirection) {
                        g.drawImage(headu, x[z], y[z], this);
                    } else if (downDirection) {
                        g.drawImage(headd, x[z], y[z], this);
                    }
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            
            obstacles.drawObstacles(g);

            g.setColor(Color.white);
            g.drawRect(0, 0, B_WIDTH - 1, B_HEIGHT - 1);

            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = getFontMetrics(small);
            String scoreString = "Score: " + score;

            // Adjust the position of the score text
            int scoreX = (B_WIDTH - metr.stringWidth(scoreString)) / 2;
            int scoreY = metr.getHeight() + 5;
            g.setFont(small);
            g.drawString(scoreString, scoreX, scoreY);

            Toolkit.getDefaultToolkit().sync();

        } else {
            if (!enterPressed) {
                showGameOver(g);
            } else {
                restartGame();
            }

        }        
    }

    private void showGameOver(Graphics g) {
        
        String msg = "Game Over";
        String restartMsg = "Press ENTER to play again";
        
        Font small = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2 - 20);
        g.drawString(restartMsg, (B_WIDTH - metr.stringWidth(restartMsg)) / 2, B_HEIGHT / 2 + 20);
    }
    
    private void restartGame() {
        inGame = true;
        enterPressed = false;
        score = 0;
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();

        timer.restart();
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            score++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }
        
        if (x[0] == speedApple_x && y[0] == speedApple_y) {
            dots++;
            score += 1;
            locateSpeedApple();
            speedAppleCount++;
            int adjustedDelay = DELAY - (speedAppleCount * 1);
            timer.setDelay(Math.max(adjustedDelay, 20));
            System.out.println("Current Delay: " + timer.getDelay());
        }
        
        if (obstacles.isObstacleCollision(x[0], y[0])) {
            inGame = false;
        }
        
        if (y[0] >= B_HEIGHT - 10 || y[0] < 30 || x[0] >= B_WIDTH - 10 || x[0] < 10) {
            inGame = false;
        }

        for (int z = dots - 1; z > 0; z--) {
            if (x[0] == x[z] && y[0] == y[z]) {
                inGame = false;
            }
        }

        
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        
        if (apple_x <= 30) {
            apple_x += 40;
        }
        
        if (apple_y <= 30) {
            apple_y += 40;
        } 
        
        if (apple_x >= 460) {
            apple_x -= 40;
        } 
        
        if (apple_y >= 460) {
            apple_y -= 40;
        }
        
        String a = String.format("Apple Normal X = %d, Y = %d", apple_x, apple_y);
        System.out.println(a);
    }
    
    private void locateSpeedApple() {
        int r = (int) (Math.random() * RAND_POS);
        speedApple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        speedApple_y = ((r * DOT_SIZE));

        if (speedApple_x <= 30) {
            speedApple_x += 40;
        }

        if (speedApple_y <= 30) {
            speedApple_y += 40;
        } 

        if (speedApple_x >= 460) {
            speedApple_x -= 40;
        } 

        if (speedApple_y >= 460) {
            speedApple_y -= 40;
        }
        
        if (speedApple_x == apple_x && speedApple_y == apple_y) {
            locateSpeedApple();
            return;
        }

        String a = String.format("Apple Speed X = %d, Y = %d", speedApple_x, speedApple_y);
        System.out.println(a);

        speedAppleVisible = true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            
            if (!enterPressed && key == KeyEvent.VK_ENTER && !inGame) {
                enterPressed = true;
                repaint();
            }

            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}