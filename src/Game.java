import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/*
 Правила игры данного Морского боя:
  1. Каждый игрок расставляет по 4 корабля длиной от 1 до 4 клеток на личном поле, а в дальнейшем обновляет его
  2. При атаке игрок, если попадает по палубе корабля противника, он продолжает атаковать пока не промахнётся
  3. Каждую результат своей атаки игрок помечает на поле атак
  4. Победителем будет являться тот, кто затопит все 4 корабля противника (10 палуб кораблей)
*/

public class Game {

    private static final Scanner scanner = new Scanner(System.in);

    private static LinkedList<Player> players = new LinkedList<>();
    private static Player winner;

    private final static String[] shipNames = new String[]{"4-ёх палубный", "3-ёх палубный", "2-ух палубный", "1-но палубный"};

    public static final int FIELD_SIZE = Player.FIELD_SIZE;

    // Переменная для ввода позиции кораблей. Уменьшает длину кораблей от 4 клеток до 1
    private static int iters;

    // Коордианты X и Y для расстановки кораблей и атаки
    private static int x;
    private static int y;
    // Расположение (вертикальное и горизонтальное), к-ое игрок задаёт при расположении кораблей
    private static int dispositionShip;

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
            // Инициализируем итератор количеством кораблей
            iters = shipNames.length;

            // В этом цикле игроку 4 раза предлагается ввести координаты X и Y для расставноки корабля
            for (int i = 0; i < shipNames.length; i++) {
                System.out.printf("%s, расставьте ваш корабль (%s) на поле боя:\n", player.getName(), shipNames[i]);

                // Отрисовка текущего состояния личного поля
                drawField(player.getMyField());

                // Ввод и считывание координат
                System.out.print("\nВведите координату Х - ");
                x = scanner.nextInt();
                System.out.print("Введите координату Y - ");
                y = scanner.nextInt();

                // Игрок вводит расположение корабля, если длина расставляемого корабля > 1 клетки
                dispositionShip = 1;
                if (iters > 1) {
                    System.out.print("\nВыберите положение корабля:\n1. Вертикальное\n2. Горизонтальное\n- ");
                    dispositionShip = scanner.nextInt();
                }

                // Расставнока кораблей на личном поле
                placementShips(player, x, y, dispositionShip);

                clearScreen();

                // После расстановки всех 4 кораблей, отображается личное поле игрока со всеми его расставленными кораблями
                if (i >= 3) {
                    System.out.println("Ваши корабли на заданных позициях:");
                    drawField(player.getMyField());

                    stopThread(5000);

                    clearScreen();
                }
            }
        }
    }

    // Отрисовка личного поля игрока
    public static void drawField(int[][] field) {
        System.out.print("\n  0 1 2 3 4 5 6 7 8 9");

        for (int y = 0; y < FIELD_SIZE; y++) {
            System.out.printf("\n%d", y);

            for (int x = 0; x < FIELD_SIZE; x++) {
                switch (field[x][y]) {
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

    // Расстановка кораблей в личном поле игрока согласно введённым данным
    public static void placementShips(Player player, int x, int y, int dispositionShip) {
        for (int i = 0; i < iters; i++) {
            switch (dispositionShip) {
                case 1 -> player.setMyField(x, y + i, 1);
                case 2 -> player.setMyField(x + i, y, 1);
            }
        }

        iters--;
    }

    // Метод, в котором происходит поочерёдная атака игроков
    public static void startGame() {
        // Игра не остановится пока кто-нибудь из игроков не победит
        do {
            for (Player player : players) {
                if (players.getFirst().isWon() || players.getLast().isWon()) break;

                playerTurn(player);
            }
        } while (!players.getFirst().isWon() && !players.getLast().isWon());

        // Конец игры
        System.out.println("Конец игры!");
        System.out.printf("Победителем Морского боя стал %s", winner.getName());

        stopThread(10000);
    }

    // Метод, представляющий из себя ход игрока
    public static void playerTurn(Player player) {
        System.out.printf("%s, введите координаты для атаки:\n", player.getName());

        drawField(player.getAttackField());

        System.out.print("\nx - ");
        x = scanner.nextInt();
        System.out.print("y - ");
        y = scanner.nextInt();

        // Проверка результата атаки
        checkAttack(player, x, y);

        clearScreen();
    }

    // Метод для проверки результата атаки игрока
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

            // Пометка и отрисовка подбитой палубы на поле атаки
            player.setEnemyField(x, y, 3);
            drawField(player.getAttackField());

            // Противник помечает попадание по своему кораблю на своём личном поле
            enemyPlayer.setMyField(x, y, 4);

            // Проверка на условия победы (10 потопленных палуб кораблей противника)
            if (player.getCountHits() >= 10) {
                player.setWon(true);
                winner = player;

                System.out.printf("\n%s, поздравляем! Вы потопили все корабли противника! Вы становитесь победителем Морского боя!", player.getName());
                stopThread(10000);
                return;
            }

            stopThread(5000);

            clearScreen();

            playerTurn(player);
        } else { // Промах
            System.out.printf("Промах! По координатам (%d, %d) не было корабля противника!\n", x, y);

            // Пометка о промахе на поле атаки
            player.setEnemyField(x, y, 2);
            drawField(player.getAttackField());

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
