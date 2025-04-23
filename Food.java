import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;
public class Food {
    static LinkedList<Point> snake = new LinkedList<>();
    static Boolean[][] food = new Boolean[20][20];
    static Point p;
    static int x;
    static int y;
    public static void spawnFood() {
        Random rand = new Random();
        do {
            x = rand.nextInt(20);
            y = rand.nextInt(20);
            p = new Point(x,y);
        } while (snake.contains(p) && food[x][y] == true);
        food[x][y] = true;
    }
    public static void main(String[] args) {
        spawnFood();
        System.out.println(food[x][y]);
        System.out.println(x);
        System.out.println(y);

    }
}