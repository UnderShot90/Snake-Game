package SnakeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

import static SnakeGame.SnakeBlock.block_height;
import static SnakeGame.SnakeBlock.block_width;

public class Snake extends JPanel {

    private static final Color c = new Color(115, 162, 78);
    private static final int start = 250;
    private static final int speed = 25;

    private Node head;
    private Node tail;
    private int size;

    private String direction;
    private Apple apple;
    private Main window;

    // Node class (linked list)
    private static class Node {
        SnakeBlock data;
        Node next;

        Node(SnakeBlock data) {
            this.data = data;
        }
    }

    public Snake(SnakeGame.Main window) {
        this.window = window;

        // Initialize the snake with 3 segments
        addFirst(new SnakeBlock(start, start));
        addLast(new SnakeBlock(start - block_width, start));
        addLast(new SnakeBlock(start - 2 * block_width, start));

        this.direction = "right";
    }

    private void addFirst(SnakeBlock block) {
        Node newNode = new Node(block);
        newNode.next = head;
        head = newNode;
        if (tail == null) tail = head;
        size++;
    }

    private void addLast(SnakeBlock block) {
        Node newNode = new Node(block);
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
        if (head == null) head = tail;
        size++;
    }

    private void removeLast() {
        if (head == null || head.next == null) {
            head = tail = null;
        } else {
            Node current = head;
            while (current.next != null && current.next != tail) {
                current = current.next;
            }
            current.next = null;
            tail = current;
        }
        size--;
    }

    private SnakeBlock get(int index) {
        Node current = head;
        int i = 0;
        while (current != null && i < index) {
            current = current.next;
            i++;
        }
        return current != null ? current.data : null;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return this.direction;
    }

    public void addPart() {
        SnakeBlock tailBlock = tail.data;
        int x = tailBlock.getXaxis();
        int y = tailBlock.getYaxis();

        switch (direction) {
            case "right" -> addLast(new SnakeBlock(x - block_width, y));
            case "left" -> addLast(new SnakeBlock(x + block_width, y));
            case "up" -> addLast(new SnakeBlock(x, y + block_height));
            case "down" -> addLast(new SnakeBlock(x, y - block_height));
        }
    }

    public void checkCollision() {
        SnakeBlock headBlock = head.data;

        int x = headBlock.getXaxis();
        int y = headBlock.getYaxis();
        if (x < 0 || x + block_width > this.getWidth() || y < 0 || y + block_height > this.getHeight()) {
            gameOver("You hit the wall!");
        }

        Node current = head.next;
        while (current != null) {
            if (headBlock.collision(current.data)) {
                gameOver("You collided with yourself!");
            }
            current = current.next;
        }

        if (apple != null && headBlock.collision(new SnakeBlock(apple.getXaxis(), apple.getYaxis()))) {
            apple = null;
            addPart();
        }
    }

    private void gameOver(String message) {
        System.out.println(message);
        this.window.setVisible(false);
        JOptionPane.showMessageDialog(new JFrame("Game Over"), message + "\nYour score: " + size);
        this.window.dispatchEvent(new WindowEvent(this.window, WindowEvent.WINDOW_CLOSING));
        System.exit(0);
    }

    public void moveSnake() {
        SnakeBlock headBlock = head.data;
        int x = headBlock.getXaxis();
        int y = headBlock.getYaxis();

        switch (direction) {
            case "right" -> x += speed;
            case "left" -> x -= speed;
            case "up" -> y -= speed;
            case "down" -> y += speed;
        }

        addFirst(new SnakeBlock(x, y));
        removeLast();
        checkCollision();
    }

    private void drawSnake(Graphics g) {
        moveSnake();
        Graphics2D g2d = (Graphics2D) g;

        if (apple != null) {
            g2d.setPaint(Color.red);
            g2d.fillRect(apple.getXaxis(), apple.getYaxis(), block_width, block_height);
        }

        g2d.setPaint(Color.blue);
        Node current = head;
        while (current != null) {
            SnakeBlock block = current.data;
            g2d.fillRect(block.getXaxis(), block.getYaxis(), block_width, block_height);
            current = current.next;
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
