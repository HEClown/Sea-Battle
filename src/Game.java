/*
 Цели:
  1. Основная механика игры
  2. ООП
  3. Проверки корректности ввода
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private static final Scanner scanner = new Scanner(System.in);

    public static final int FIELD_SIZE = Player.FIELD_SIZE;

    private static ArrayList<Player> players;

    private final static String[] ships = new String[] {"4-ёх палубный", "3-ёх палубный", "2-ух палубный", "1-но палубный"};
    // Переменная, уменьшающаяся после ввода позиции каждого из кораблей от 4 до 1
    private static int iters;

    public static void main(String[] args) {
        players = new ArrayList<>(2);

        createPlayers();
        startGame();
    }

    public static void createPlayers() {
        for (int i = 1; i <= 2; i++) {
            System.out.printf("Игрок № %d, введите ваше имя - ", i);
            players.add(new Player(scanner.next()));
        }
    }

    public static void startGame() {
        for (Player player: players) {
            // Инициализация переменной и запуск метода для расставновки 4-ёх кораблей длиной от 1 до 4 клеток
            iters = ships.length;
            inputPositionShips(player);

            drawField(player);

            System.out.println();
        }
    }

    public static void inputPositionShips(Player player) {
        for (int i = 0; i < ships.length; i++) {
            System.out.printf("\n%s, расставьте ваш корабль (%s) на поле боя:\n", player.getName(), ships[i]);

            drawField(player);

            System.out.print("\n\nВыберите координату Х - ");
            int x = scanner.nextInt();
            System.out.print("Выберите координату Y - ");
            int y = scanner.nextInt();

            // Игрок вводит положене корабля, если длина расставляемого корабля > 1
            int direction = 1;
            if (iters > 1) {
                System.out.print("\nВыберите положение корабля:\n1. Вертикальное\n2. Горизонтальное\n- ");
                direction = scanner.nextInt();
            }

            dispositonShips(player, x, y, iters, direction);
            iters--;
        }
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
    }

    // Расстановка кораблей в поле (массиве) игрока согласно введённым данным
    public static void dispositonShips(Player player, int x, int y, int iters, int direction) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (i == x && j == y) {
                    for (int k = 0; k < iters; k++) {
                        if (direction == 1) player.getField()[i][j + k] = 1;
                        if (direction == 2) player.getField()[i + k][j] = 1;
                    }
                }
            }
        }
    }

}
