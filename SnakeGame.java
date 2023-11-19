 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;
    private static final int GAME_SPEED = 100;

    private LinkedList<Point> snake;
    private Point food;
    private char direction;
    private boolean gameOver;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        snake = new LinkedList<>();
        direction = 'R'; // Initial direction: Right
        gameOver = false;

        // Initialize the snake with three cells
        snake.add(new Point(5, 5));
        snake.add(new Point(4, 5));
        snake.add(new Point(3, 5));

        // Place the initial food randomly
        placeFood();

        // Set up the game loop
        Timer timer = new Timer(GAME_SPEED, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
        setVisible(true);
    }

    private void placeFood() {
        Random rand = new Random();
        int x, y;

        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));

        food = new Point(x, y);
    }

    private void move() {
        Point head = snake.getFirst();
        Point newHead;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                break;
            case 'D':
                newHead = new Point(head.x, (head.y + 1) % GRID_SIZE);
                break;
            case 'L':
                newHead = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                break;
            case 'R':
                newHead = new Point((head.x + 1) % GRID_SIZE, head.y);
                break;
            default:
                return;
        }

        // Check for collision with the snake's body
        if (snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        snake.addFirst(newHead);

        // Check for collision with food
        if (newHead.equals(food)) {
            placeFood();
        } else {
            snake.removeLast();
        }
    }

    private void checkGameOver() {
        if (gameOver) {
            JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private void paintGrid(Graphics g) {
        // Draw grid lines
        g.setColor(Color.GRAY);
        for (int i = 0; i < GRID_SIZE; i++) {
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, getHeight());
            g.drawLine(0, i * CELL_SIZE, getWidth(), i * CELL_SIZE);
        }
    }

    private void paintSnake(Graphics g) {
        // Draw snake cells
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void paintFood(Graphics g) {
        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            checkGameOver();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintGrid(g);
        paintSnake(g);
        paintFood(g);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}
