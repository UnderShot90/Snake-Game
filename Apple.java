package SnakeGame;

import java.util.Random;
import java.util.TimerTask;

public class Apple extends TimerTask {


    private int Xaxis;
    private int Yaxis;
    private Snake snake;


    public int getXaxis() {
        return Xaxis;
    }

    public int getYaxis() {
        return Yaxis;
    }

    public Apple(Snake snake) {
        this.snake = snake;
    }

    public Apple() {
        this.Xaxis = 25 * new Random().nextInt(20);
        this.Yaxis = 25 * new Random().nextInt(20);
    }

    @Override
    public void run() {
        if (this.snake.getApple() == null) {
            this.snake.setApple(new Apple());
        }
    }
}