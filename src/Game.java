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
            System.out.printf("\n%s, расставьте ваши корабли на поле боя:", player.getName());

            drawField(player);

            System.out.print("\n\nВыберите координату Х - ");
            int x = scanner.nextInt();
            System.out.print("Выберите координату Y - ");
            int y = scanner.nextInt();
            System.out.print("\nВыберите положение корабля:\n1. Вертикальное\n2. Горизонтальное\n- ");
            int position = scanner.nextInt();

            dispositonShips(player, x, y);

            drawField(player);

            System.out.println();
        }
    }

    public static void drawField(Player player) {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%d", i);
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (player.getFieldPlayer()[j][i] == 0) System.out.print("  ");
                if (player.getFieldPlayer()[j][i] == 1) System.out.print(" #");
            }
        }
    }

    public static void dispositonShips(Player player, int x, int y) {
        for (int i = 0; i < FIELD_SIZE; i++) {
            for (int j = 0; j < FIELD_SIZE; j++) {
                if (i == x && j == y) player.getFieldPlayer()[i][j] = 1;
            }
        }
    }

}
