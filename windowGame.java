import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class windowGame extends JFrame implements KeyListener {

    private static final int UP = 0;
    private static final int DOWN = 1;
    private static final int LEFT = 2;
    private static final int RIGHT = 3;

    private static final int GRID_WIDTH = 500;
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 500;
    private static final int MATRIX_SIZE = 25;

    private int[][] matrix = new int[MATRIX_SIZE][MATRIX_SIZE];
    private int sizeOfSnakeBody = GRID_WIDTH / MATRIX_SIZE;

    private int headX = 7, headY = 7;
    private List<Integer> bodyX = new ArrayList<Integer>();
    private List<Integer> bodyY = new ArrayList<Integer>();

    private List<Integer> foodPositionX = new ArrayList<Integer>();
    private List<Integer> foodPositionY = new ArrayList<Integer>();

    private Random random = new Random();

    private int direction = RIGHT;
    private Timer timer;

    private boolean isGameOver = false;
    private boolean isDirectionChanged = false;

    private int applesEaten = 0;
    private int score = 0;

    private int greenAppleTicks = 0;
    private int greenAppleX = -1;
    private int greenAppleY = -1;

    private Image dbImage;
    private Graphics dbg;

    public windowGame() {
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);

        Insets insets = getInsets();
        setSize(WINDOW_WIDTH + insets.left + insets.right, WINDOW_HEIGHT + insets.top + insets.bottom);
        setLocationRelativeTo(null);

        addKeyListener(this);
        resetGame();
    }

    private void resetGame() {
        isGameOver = false;
        direction = RIGHT;
        isDirectionChanged = false;
        applesEaten = 0;
        score = 0;
        greenAppleTicks = 0;
        headX = 7;
        headY = 7;

        bodyX.clear();
        bodyY.clear();

        bodyX.add(7); bodyX.add(6); bodyX.add(5); bodyX.add(4);
        bodyY.add(7); bodyY.add(7); bodyY.add(7); bodyY.add(7);

        for(int row = 0; row < MATRIX_SIZE; row++) {
            for(int col = 0; col < MATRIX_SIZE; col++) {
                matrix[row][col] = 0;
            }
        }

        matrix[11][11] = 1;

        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                moveSnake();
            }
        }, 0, 150);
    }

    private int[] getNexPositionOfHead() {
        int futureHeadX = headX;
        int futureHeadY = headY;

        switch(direction) {
            case UP: futureHeadY--; break;
            case DOWN: futureHeadY++; break;
            case LEFT: futureHeadX--; break;
            case RIGHT: futureHeadX++; break;
        }

        int[] result = new int[2];
        result[0] = futureHeadX;
        result[1] = futureHeadY;

        return result;
    }

    private boolean isSnakeHitBody() {
        for (int i = 1; i < bodyX.size(); i++) {
            if (bodyX.get(i) == headX && bodyY.get(i) == headY) {
                return true;
            }
        }
        return false;
    }

    // دالة جديدة: تفحص ما إذا كان الإحداثي المعطى يحتوي على رأس أو جسم الثعبان
    private boolean isSnakeAt(int x, int y) {
        if (headX == x && headY == y) return true;
        for (int i = 0; i < bodyX.size(); i++) {
            if (bodyX.get(i) == x && bodyY.get(i) == y) {
                return true;
            }
        }
        return false;
    }

    private int[] getFoodPosition() {
        foodPositionX.clear();
        foodPositionY.clear();

        for (int y = 0; y < MATRIX_SIZE; y++) {
            for (int x = 0; x < MATRIX_SIZE; x++) {
                // التأكد من أن المربع فارغ من التفاح ومن جسم الثعبان في نفس الوقت
                if (matrix[y][x] == 0 && !isSnakeAt(x, y)) {
                    foodPositionX.add(x);
                    foodPositionY.add(y);
                }
            }
        }

        // حماية برمجية في حال امتلاء الشاشة تماماً بالثعبان
        if (foodPositionX.isEmpty()) {
            return new int[]{0, 0};
        }

        int randomIndex = random.nextInt(foodPositionX.size());

        int[] result = new int[2];
        result[0] = foodPositionX.get(randomIndex);
        result[1] = foodPositionY.get(randomIndex);

        return result;
    }

    private void moveSnake() {
        if (isGameOver) return;

        if (greenAppleTicks > 0) {
            greenAppleTicks--;
            if (greenAppleTicks == 0 && matrix[greenAppleY][greenAppleX] == 2) {
                matrix[greenAppleY][greenAppleX] = 0;
            }
        }

        int[] futurePosition = getNexPositionOfHead();

        if (futurePosition[0] < 0 || futurePosition[0] >= MATRIX_SIZE ||
            futurePosition[1] < 0 || futurePosition[1] >= MATRIX_SIZE) {
            isGameOver = true;
            timer.cancel();
            repaint();
            return;
        }

        int oldTailX = bodyX.get(bodyX.size() - 1);
        int oldTailY = bodyY.get(bodyY.size() - 1);

        for (int i = bodyX.size() - 1; i > 0; i--) {
            bodyX.set(i, bodyX.get(i - 1));
            bodyY.set(i, bodyY.get(i - 1));
        }

        bodyX.set(0, headX);
        bodyY.set(0, headY);

        headX = futurePosition[0];
        headY = futurePosition[1];

        isDirectionChanged = false;

        if (isSnakeHitBody()) {
            isGameOver = true;
            timer.cancel();
            repaint();
            return;
        }

        if (matrix[headY][headX] == 1) {
            bodyX.add(oldTailX);
            bodyY.add(oldTailY);
            matrix[headY][headX] = 0;
            applesEaten++;
            score += 5;

            int[] newFoodPosition = getFoodPosition();
            matrix[newFoodPosition[1]][newFoodPosition[0]] = 1;

            if (applesEaten % 5 == 0) {
                int[] greenFoodPosition = getFoodPosition();
                greenAppleX = greenFoodPosition[0];
                greenAppleY = greenFoodPosition[1];
                matrix[greenAppleY][greenAppleX] = 2;
                greenAppleTicks = 66;
            }
        }
        else if (matrix[headY][headX] == 2) {
            for (int i = 0; i < 3; i++) {
                bodyX.add(oldTailX);
                bodyY.add(oldTailY);
            }
            matrix[headY][headX] = 0;
            score += 10;
            greenAppleTicks = 0;
        }

        repaint();
    }

    public static void main(String[] args) {
        new windowGame();
    }

    @Override
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        if (dbImage != null) {
            dbg = dbImage.getGraphics();
            draw(dbg);
            g.drawImage(dbImage, 0, 0, this);
        } else {
            draw(g);
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());

        Insets insets = getInsets();
        int offsetX = insets.left;
        int offsetY = insets.top;

        int panelWidth = WINDOW_WIDTH - GRID_WIDTH;
        int panelStartX = offsetX + GRID_WIDTH;

        g.setColor(Color.BLACK);
        Font fontTitle = new Font("Arial", Font.BOLD, 18);
        g.setFont(fontTitle);
        String textScore = "Score";
        FontMetrics fmTitle = g.getFontMetrics(fontTitle);
        int xTitle = panelStartX + (panelWidth - fmTitle.stringWidth(textScore)) / 2;
        g.drawString(textScore, xTitle, offsetY + 50);

        g.setColor(Color.RED);
        Font fontScore = new Font("Arial", Font.BOLD, 28);
        g.setFont(fontScore);
        String textNum = String.valueOf(score);
        FontMetrics fmScore = g.getFontMetrics(fontScore);
        int xNum = panelStartX + (panelWidth - fmScore.stringWidth(textNum)) / 2;
        g.drawString(textNum, xNum, offsetY + 85);

        if (greenAppleTicks > 0) {
            int secondsLeft = (greenAppleTicks * 150) / 1000;

            g.setColor(new Color(34, 139, 34));
            Font fontBonus = new Font("Arial", Font.BOLD, 16);
            g.setFont(fontBonus);
            String textBonus = "Bonus";
            FontMetrics fmBonus = g.getFontMetrics(fontBonus);
            int xBonus = panelStartX + (panelWidth - fmBonus.stringWidth(textBonus)) / 2;
            g.drawString(textBonus, xBonus, offsetY + 150);

            Font fontSec = new Font("Arial", Font.BOLD, 26);
            g.setFont(fontSec);
            String textSec = secondsLeft + " s";
            FontMetrics fmSec = g.getFontMetrics(fontSec);
            int xSec = panelStartX + (panelWidth - fmSec.stringWidth(textSec)) / 2;
            g.drawString(textSec, xSec, offsetY + 185);
        }

        int hX = headX * sizeOfSnakeBody + offsetX;
        int hY = headY * sizeOfSnakeBody + offsetY;

        g.setColor(Color.ORANGE);
        g.fillRect(hX, hY, sizeOfSnakeBody, sizeOfSnakeBody);

        g.setColor(Color.BLACK);
        if (direction == UP || direction == DOWN) {
            g.drawLine(hX + 5, hY + 5, hX + 5, hY + sizeOfSnakeBody - 5);
            g.drawLine(hX + sizeOfSnakeBody - 5, hY + 5, hX + sizeOfSnakeBody - 5, hY + sizeOfSnakeBody - 5);
        } else {
            g.drawLine(hX + 5, hY + 5, hX + sizeOfSnakeBody - 5, hY + 5);
            g.drawLine(hX + 5, hY + sizeOfSnakeBody - 5, hX + sizeOfSnakeBody - 5, hY + sizeOfSnakeBody - 5);
        }

        g.setColor(Color.ORANGE);
        for (int i = 0; i < bodyX.size(); i++) {
            g.fillRect(bodyX.get(i) * sizeOfSnakeBody + offsetX, bodyY.get(i) * sizeOfSnakeBody + offsetY, sizeOfSnakeBody, sizeOfSnakeBody);
        }

        g.setColor(Color.black);
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                int x = col * sizeOfSnakeBody + offsetX;
                int y = row * sizeOfSnakeBody + offsetY;

                if (matrix[row][col] == 1 || matrix[row][col] == 2) {
                    if (matrix[row][col] == 1) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(new Color(34, 139, 34));
                    }

                    g.fillOval(x, y, sizeOfSnakeBody, sizeOfSnakeBody);

                    g.setColor(Color.BLACK);
                    int centerX = x + (sizeOfSnakeBody / 2);
                    g.drawLine(centerX, y + 8, centerX + 2, y - 4);
                    g.drawLine(centerX + 1, y + 8, centerX + 3, y - 4);

                } else {
                    g.setColor(Color.black);
                    g.drawRect(x, y, sizeOfSnakeBody, sizeOfSnakeBody);
                }
            }
        }

        if (isGameOver) {
            String msg1 = "GAME OVER";
            String msg2 = "Press ENTER to Restart";

            Font bigFont = new Font("Arial", Font.BOLD, 55);
            FontMetrics metrics1 = g.getFontMetrics(bigFont);
            int x1 = offsetX + (GRID_WIDTH - metrics1.stringWidth(msg1)) / 2;
            int y1 = offsetY + (WINDOW_HEIGHT / 2);

            g.setColor(Color.red);
            g.setFont(bigFont);
            g.drawString(msg1, x1, y1);

            Font smallFont = new Font("Arial", Font.BOLD, 22);
            FontMetrics metrics2 = g.getFontMetrics(smallFont);
            int x2 = offsetX + (GRID_WIDTH - metrics2.stringWidth(msg2)) / 2;
            int y2 = y1 + 40;

            g.setColor(Color.black);
            g.setFont(smallFont);
            g.drawString(msg2, x2, y2);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (isGameOver && e.getKeyCode() == KeyEvent.VK_ENTER) {
            resetGame();
            return;
        }

        if (!isDirectionChanged) {
            if (e.getKeyCode() == KeyEvent.VK_UP && direction != DOWN) {
                direction = UP;
                isDirectionChanged = true;
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != UP) {
                direction = DOWN;
                isDirectionChanged = true;
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != RIGHT) {
                direction = LEFT;
                isDirectionChanged = true;
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != LEFT) {
                direction = RIGHT;
                isDirectionChanged = true;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}