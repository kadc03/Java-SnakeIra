package com.project.snakeira;

import javax.swing.*;
import java.awt.*;

public class Obstacles {

    private final int B_WIDTH;
    private final int B_HEIGHT;
    private final int DOT_SIZE;
    private final int OBSTACLE_WIDTH;
    private final int OBSTACLE_HEIGHT;

    private final int[] obstacleX;
    private final int[] obstacleY;

    private Image obstacle;

    public Obstacles(int width, int height, int dotSize) {
        B_WIDTH = width;
        B_HEIGHT = height;
        DOT_SIZE = dotSize;
        OBSTACLE_WIDTH = 40;
        OBSTACLE_HEIGHT = 20;

        obstacleX = new int[5];
        obstacleY = new int[5];

        loadImages();
        initObstacles();
    }

    private void loadImages() {
        ImageIcon iio = new ImageIcon("src/images/obstacle.png");
        obstacle = iio.getImage();
    }

    private void initObstacles() {
        // Set 4 corner squares
        obstacleX[0] = 50;
        obstacleY[0] = 80;

        obstacleX[1] = B_WIDTH - OBSTACLE_WIDTH - 10;
        obstacleY[1] = 80;

        obstacleX[2] = 50;
        obstacleY[2] = B_HEIGHT - OBSTACLE_HEIGHT - 40;

        obstacleX[3] = B_WIDTH - OBSTACLE_WIDTH - 10;
        obstacleY[3] = B_HEIGHT - OBSTACLE_HEIGHT - 40;

        // Set the center square
        int centerX = (B_WIDTH + 30 - OBSTACLE_WIDTH) / 2;
        int centerY = (B_HEIGHT - OBSTACLE_HEIGHT) / 2;
        obstacleX[4] = centerX;
        obstacleY[4] = centerY;
    }

    public void drawObstacles(Graphics g) {
        for (int i = 0; i < 5; i++) {
            g.drawImage(obstacle, obstacleX[i], obstacleY[i], OBSTACLE_WIDTH, OBSTACLE_HEIGHT - 10, null);
        }
    }

    public boolean isObstacleCollision(int x, int y) {
        for (int i = 0; i < 5; i++) {
            if (x >= obstacleX[i] && x <= obstacleX[i] + OBSTACLE_WIDTH - 10
                    && y >= obstacleY[i] && y <= obstacleY[i] + OBSTACLE_HEIGHT - 20) {
                return true;
            }
        }
        return false;
    }
}
