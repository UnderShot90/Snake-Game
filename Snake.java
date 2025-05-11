package SnakeGame;

import static SnakeGame.SnakeBlock.block_height;
import static SnakeGame.SnakeBlock.block_width;
import java.awt.*;
import javax.swing.*;

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
    private boolean isGameOver = false;
    private SinglyLinkedList<Integer> board = new SinglyLinkedList<>();
    private boolean scoresDisplayed = false;

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
            gameOver();
        }

        Node current = head.next;
        while (current != null) {
            if (headBlock.collision(current.data)) {
                gameOver();
            }
            current = current.next;
        }

        if (apple != null && headBlock.collision(new SnakeBlock(apple.getXaxis(), apple.getYaxis()))) {
            apple = null;
            addPart();
        }
    }

    private void gameOver() {
        isGameOver = true;
        scoresDisplayed = false;
        repaint();
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

    public void resetGame() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.direction = "right";
        this.apple = null;
        this.isGameOver = false;

        addFirst(new SnakeBlock(250, 250));
        addLast(new SnakeBlock(250 - block_width, 250));
        addLast(new SnakeBlock(250 - 2 * block_width, 250));
        repaint();
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    private void drawSnake(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;

    if (!isGameOver) {
        moveSnake();

        // Draw apple
        if (apple != null) {
            g2d.setPaint(Color.red);
            g2d.fillRect(apple.getXaxis(), apple.getYaxis(), block_width, block_height);
        }

        // Draw snake
        g2d.setPaint(Color.blue);
        Node current = head;
        while (current != null) {
            SnakeBlock block = current.data;
            g2d.fillRect(block.getXaxis(), block.getYaxis(), block_width, block_height);
            current = current.next;
        }
    } else {
        // Game over text
        g2d.setPaint(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("Game Over", 225, 300);

        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        g2d.drawString("Press Enter to Play Again", 200, 350);

        if (!scoresDisplayed) {
        SinglyLinkedList<Integer> temp = new SinglyLinkedList<>();
        boolean added = false;

        // Insert new score in descending order
        while (!board.isEmpty()) {
            int currentScore = board.removeFirst();

            if (!added && size >= currentScore) {
                temp.addLast(size);  // Add new score before smaller score
                added = true;
            }

            temp.addLast(currentScore);
        }

        // If score is lowest and not yet added, add it at the end
        if (!added) {
            temp.addLast(size);
        }

        // Limit to top 3 scores only
        int count = 0;
        while (!temp.isEmpty() && count < 3) {
            board.addLast(temp.removeFirst());
            count++;
        }

        scoresDisplayed = true;
    }

    // Display the top scores
    g2d.setFont(new Font("Arial", Font.PLAIN, 24));
    g2d.drawString("Top Scores:", 200, 400);

    int y = 430;
    int rank = 1;

    // Display only the scores already stored (max 3)
    int scoreCount = board.size();
    for (int i = 0; i < scoreCount; i++) {
        int score = board.removeFirst();
        g2d.drawString(rank + ". " + score, 200, y);
        board.addLast(score);  // Maintain order
        y += 30;
        rank++;
    }
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