/*
 Цели:
  1. Основная механика игры
  2. ООП
  3. Проверки корректности ввода
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private static final Scanner scanner = new Scanner(System.in);

    public static final int FIELD_SIZE = Player.FIELD_SIZE;

    private static ArrayList<Player> players = new ArrayList<>(2);
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

                drawField(player);

                System.out.print("\nВыберите координату Х - ");
                x = scanner.nextInt();
                System.out.print("Выберите координату Y - ");
                y = scanner.nextInt();

                // Игрок вводит положене корабля, если длина расставляемого корабля > 1
                direction = 1;
                if (iters > 1) {
                    System.out.print("\nВыберите положение корабля:\n1. Вертикальное\n2. Горизонтальное\n- ");
                    direction = scanner.nextInt();
                }

                dispositonShips(player, x, y, direction);

                clearScreen();
            }
        }
        clearScreen();
    }

    // Отрисовка поля в консоли согласно расстановке кораблей игрока
    public static void drawField(Player player) {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%d", i);
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (player.getField()[j][i] == 0) System.out.print("  ");
                if (player.getField()[j][i] == 1) System.out.print(" #");
            }
        }
        System.out.println();
    }

    // Расстановка кораблей в поле (массиве) игрока согласно введённым данным
    public static void dispositonShips(Player player, int x, int y, int direction) {
        for (int k = 0; k < iters; k++) {
            if (direction == 1) player.getField()[x][y + k] = 1;
            if (direction == 2) player.getField()[x + k][y] = 1;
        }
        iters--;
    }

    public static void startGame() {
        do {
            for (Player player: players) {
                System.out.printf("%s, введите координаты для атаки:\n", player.getName());

                drawEmptyField();

                System.out.print("\nx - ");
                x = scanner.nextInt();
                System.out.print("y - ");
                y = scanner.nextInt();

                clearScreen();
            }
        } while (!player_1_won && !player_2_won);
    }

    public static void drawEmptyField() {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%d", i);
            for (int j = 0; j < FIELD_SIZE; j++) {
               System.out.print("  ");
            }
        }
        System.out.println();
    }

    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

}
