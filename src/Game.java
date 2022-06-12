/*
 Цели:
  1. Основная механика игры
  2. ООП
  3. Проверки корректности ввода
 */

import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Game {

    private static final Scanner scanner = new Scanner(System.in);

    public static final int FIELD_SIZE = Player.FIELD_SIZE;

    private static LinkedList<Player> players = new LinkedList<>();
    private static boolean player_1_won = false;
    private static boolean player_2_won = false;

    private final static String[] ships = new String[] {"4-ёх палубный", "3-ёх палубный", "2-ух палубный", "1-но палубный"};
    // Переменная, уменьшающаяся после ввода позиции каждого из кораблей от 4 до 1
    private static int iters;

    private static int x;
    private static int y;
    private static int direction;

    public static void main(String[] args) {
        createPlayers();
        inputPositionShips();
        startGame();
    }

    public static void createPlayers() {
        clearScreen();
        for (int i = 1; i <= 2; i++) {
            System.out.printf("Игрок № %d, введите ваше имя - ", i);
            players.add(new Player(scanner.next()));
        }
        clearScreen();
    }

    public static void inputPositionShips() {
        for (Player player: players) {
            iters = ships.length;
            for (int i = 0; i < ships.length; i++) {
                System.out.printf("%s, расставьте ваш корабль (%s) на поле боя:\n", player.getName(), ships[i]);

                drawField(player.getMyField());

                System.out.print("\nВведите координату Х - ");
                x = scanner.nextInt();
                System.out.print("Введите координату Y - ");
                y = scanner.nextInt();

                // Игрок вводит положене корабля, если длина расставляемого корабля > 1
                direction = 1;
                if (iters > 1) {
                    System.out.print("\nВыберите положение корабля:\n1. Вертикальное\n2. Горизонтальное\n- ");
                    direction = scanner.nextInt();
                }

                dispositonShips(player, x, y, direction);

                clearScreen();

                if (i >= 3) {
                    System.out.println("Ваши корабли на заданных позициях:");
                    drawField(player.getMyField());
                    stopThread(5000);
                    clearScreen();
                }
            }
        }
    }

    // Отрисовка поля в консоли согласно расстановке кораблей игрока
    public static void drawField(int[][] field) {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%d", i);
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (field[j][i] == 0) System.out.print("  "); // Пустая клетка
                if (field[j][i] == 1) System.out.print(" #"); // Клетка с кораблём
                if (field[j][i] == 2) System.out.print(" -"); // Клетка после промаха
                if (field[j][i] == 3) System.out.print(" +"); // Клетка после попадания по вражескому кораблю
                if (field[j][i] == 4) System.out.print(" X"); // Клетка после попадания врага по собственному кораблю
            }
        }
        System.out.println();
    }

    // Расстановка кораблей в поле (массиве) игрока согласно введённым данным
    public static void dispositonShips(Player player, int x, int y, int direction) {
        for (int k = 0; k < iters; k++) {
            if (direction == 1) player.setMyField(x, y + k, 1);
            if (direction == 2) player.setMyField(x + k, y, 1);
        }
        iters--;
    }

    public static void startGame() {
        do {
            for (Player player: players) {
                System.out.printf("%s, введите координаты для атаки:\n", player.getName());

                drawField(player.getEnemyField());

                System.out.print("\nx - ");
                x = scanner.nextInt();
                System.out.print("y - ");
                y = scanner.nextInt();

                checkAttack(player, x, y);

                clearScreen();
            }
        } while (!player_1_won && !player_2_won);
    }

    public static void checkAttack(Player player, int x, int y) {
        Player enemyPlayer = players.getFirst();
        if (player.getName().equals(enemyPlayer.getName())) {
            enemyPlayer = players.getLast();
        }

        clearScreen();

        if (enemyPlayer.getMyField()[x][y] == 1) {
            System.out.printf("Попадание! По координатам (%d, %d) был корабль противника!\n", x, y);
            player.setEnemyField(x, y, 3);
            drawField(player.getEnemyField());
            enemyPlayer.setMyField(x, y, 4);
            stopThread(5000);
        } else {
            System.out.printf("Промах! По координатам (%d, %d) не было корабля противника!\n", x, y);
            player.setEnemyField(x, y, 2);
            drawField(player.getEnemyField());
            stopThread(5000);
        }
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopThread(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
