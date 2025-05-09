package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import static SnakeGame.SnakeBlock.block_height;
import static SnakeGame.SnakeBlock.block_width;

public class Snake extends JPanel {

    private static final Color c = new Color(115,162,78);
    private static final int start = 250;
    private static final int speed = 25;

    private LinkedList<SnakeBlock> body;
    private String direction;
    private Apple apple;
    private Main window;

    public Snake(SnakeGame.Main window) {
        this.window = window;

        this.body = new LinkedList<>();
        body.add(new SnakeBlock(start, start));
        body.add(new SnakeBlock(start - block_width, start));
        body.add(new SnakeBlock(start - 2 * block_width, start));

        this.direction = "right";
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public void addPart() {
        SnakeBlock tail = body.getLast();
        int x = tail.getXaxis();
        int y = tail.getYaxis();

        switch (this.direction) {
            case "right" -> body.addLast(new SnakeBlock(x - block_width, y));
            case "left" -> body.addLast(new SnakeBlock(x + block_width, y));
            case "up" -> body.addLast(new SnakeBlock(x, y + block_height));
            case "down" -> body.addLast(new SnakeBlock(x, y - block_height));
        }
    }

    public void checkCollision() {
        SnakeBlock head = body.getFirst();

        // Wall collision
        int x = head.getXaxis();
        int y = head.getYaxis();
        if (x < 0 || x + block_width > this.getWidth() || y < 0 || y + block_height > this.getHeight()) {
            gameOver("You hit the wall!");
        }

        // Self collision
        for (int i = 1; i < body.size(); i++) {
            if (head.collision(body.get(i))) {
                gameOver("You collided with yourself!");
            }
        }

        // Apple collision
        if (apple != null && head.collision(new SnakeBlock(apple.getXaxis(), apple.getYaxis()))) {
            apple = null;
            addPart();
        }
    }

    private void gameOver(String message) {
        System.out.println(message);
        this.window.setVisible(false);
        JFrame parent = new JFrame("Game Over");
        JOptionPane.showMessageDialog(parent, message + "\nYour score: " + body.size());
        this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
        System.exit(0);
    }

    public void moveSnake() {
        SnakeBlock head = body.getFirst();
        int x = head.getXaxis();
        int y = head.getYaxis();

        switch (this.direction) {
            case "right" -> x += speed;
            case "left" -> x -= speed;
            case "up" -> y -= speed;
            case "down" -> y += speed;
        }

        body.addFirst(new SnakeBlock(x, y));
        body.removeLast(); // remove tail unless apple is eaten (which adds new part)
        checkCollision();
    }

    private void drawSnake(Graphics g) {
        moveSnake();
        Graphics2D g2d = (Graphics2D) g;

        // Draw apple
        if (apple != null) {
            g2d.setPaint(Color.red);
            g2d.fillRect(apple.getXaxis(), apple.getYaxis(), block_width, block_height);
        }

        // Draw snake
        g2d.setPaint(Color.blue);
        for (SnakeBlock block : body) {
            g2d.fillRect(block.getXaxis(), block.getYaxis(), block_width, block_height);
        }
    }

    public void setApple(Apple apple) {
        this.apple = apple;
    }

    public Apple getApple() {
        return this.apple;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(c);
        drawSnake(g);
    }
}