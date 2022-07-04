import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class Game {

    private static final Scanner scanner = new Scanner(System.in);

    public static final int FIELD_SIZE = Player.FIELD_SIZE;

    private static LinkedList<Player> players = new LinkedList<>();
    private static boolean player_1_won = false;
    private static boolean player_2_won = false;

    private final static String[] shipNames = new String[]{"4-ёх палубный", "3-ёх палубный", "2-ух палубный", "1-но палубный"};

    // Переменная для ввода позиции кораблей. Уменьшает длину кораблей от 4 клеток до 1
    private static int iters;

    private static Integer x;
    private static Integer y;
    private static int direction;

    public static void main(String[] args) {
        createPlayers();
        inputPositionShips();
        startGame();
    }

    // Метод для создания игроков перед расстановкой кораблей
    public static void createPlayers() {
        clearScreen();
        for (int i = 1; i <= 2; i++) {
            System.out.printf("Игрок № %d, введите ваше имя - ", i);
            players.add(new Player(scanner.next()));
        }
        clearScreen();
    }

    // Метод для ввода позиции кораблей перед игрой
    public static void inputPositionShips() {
        for (Player player : players) {
            iters = shipNames.length;
            for (int i = 0; i < shipNames.length; i++) {
                System.out.printf("%s, расставьте ваш корабль (%s) на поле боя:\n", player.getName(), shipNames[i]);

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

    // Отрисовка поля согласно расстановке кораблей игрока
    public static void drawField(int[][] field) {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");
        for (int i = 0; i < FIELD_SIZE; i++) {
            System.out.printf("\n%d", i);
            for (int j = 0; j < FIELD_SIZE; j++) {
                switch (field[j][i]) {
                    case 0 -> System.out.print("  "); // Пустая клетка
                    case 1 -> System.out.print(" #"); // Клетка с палубой корабля
                    case 2 -> System.out.print(" -"); // Клетка после промаха
                    case 3 -> System.out.print(" +"); // Клетка после попадания по палубе корабля противника
                    case 4 -> System.out.print(" X"); // Клетка после попадания противника по палубе собственного корабля
                }
            }
        }
        System.out.println();
    }

    // Расстановка кораблей в собственном поле игрока согласно введённым данным
    public static void dispositonShips(Player player, int x, int y, int direction) {
        for (int i = 0; i < iters; i++) {
            if (direction == 1) player.setMyField(x, y + i, 1);
            if (direction == 2) player.setMyField(x + i, y, 1);
        }
        iters--;
    }

    // Метод, в котором происходит поочерёдная атака игроков
    public static void startGame() {
        do {
            for (Player player : players) {
                if (player_1_won || player_2_won) break;
                playerTurn(player);
            }
        } while (!player_1_won && !player_2_won);

        // Конец игры, статистика
        System.out.println("Конец игры!");
        stopThread(10000);
    }

    public static void playerTurn(Player player) {
        System.out.printf("%s, введите координаты для атаки:\n", player.getName());

        drawField(player.getEnemyField());

        System.out.print("\nx - ");
        x = scanner.nextInt();
        System.out.print("y - ");
        y = scanner.nextInt();

        checkAttack(player, x, y);

        clearScreen();
    }

    // Метод для проверки результата выстрела игрока
    public static void checkAttack(Player player, int x, int y) {
        // Определение противника
        Player enemyPlayer = players.getFirst();
        if (player.getName().equals(enemyPlayer.getName())) {
            enemyPlayer = players.getLast();
        }

        clearScreen();

        // Проверка результата выстрела
        if (enemyPlayer.getMyField()[x][y] == 1) { // Попадание
            // Увеличиваем счётчик попаданий на 1
            player.incrementCountHits();

            System.out.printf("Попадание! По координатам (%d, %d) был корабль противника!\n", x, y);

            // Пометка и отрисовка подбитой палубы на копии поля противника
            player.setEnemyField(x, y, 3);
            drawField(player.getEnemyField());

            // Противник помечает попадание по своему кораблю на своём поле
            enemyPlayer.setMyField(x, y, 4);

            // Проверка на условия победы (10 потопленных палуб кораблей противника)
            if (player.getCountHits() >= 10) {
                player_1_won = true;
                System.out.printf("\n%s, поздравляем! Вы потопили 10 вражеских палуб! Вы становитесь победителем Морского боя!", player.getName());
                stopThread(10000);
                return;
            }

            stopThread(5000);
            clearScreen();
            playerTurn(player);
        } else { // Промах
            System.out.printf("Промах! По координатам (%d, %d) не было корабля противника!\n", x, y);

            // Пометка о промахе на копии поля противника
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
