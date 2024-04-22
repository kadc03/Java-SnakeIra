package com.project.snakeira;

import javax.swing.*;
import java.awt.*;

public class SnakeIra extends JFrame {
    
    public SnakeIra() {
        initUI();
    }
    
    private void initUI() {
        add(new Board());
               
        setResizable(false);
        pack();
        
        setTitle("SnakeIra");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ImageIcon logoIcon = new ImageIcon("src/images/snake.png");
        setIconImage(logoIcon.getImage());
    }
    

    public static void main(String[] args) {
        
        EventQueue.invokeLater(() -> {
            JFrame ex = new SnakeIra();
            ex.setVisible(true);
        });
    }
}
