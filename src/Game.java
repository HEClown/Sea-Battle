/*
Цели:
 + 1. Два игрока, ввод имён в начале игры
 2. Механизм работы игры
 */

import java.util.Scanner;

public class Game {

    private static Scanner scanner;

    private static Player player_1;
    private static Player player_2;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        createPlayers();
    }

    public static void render() {

    }

    public static void createPlayers() {
        // Ввод своих имён пользователями
        System.out.print("Игрок № 1, введите ваше имя - ");
        String namePlayer_1 = scanner.next();
        System.out.print("Игрок № 1, введите ваше имя - ");
        String namePlayer_2 = scanner.next();
        // Создание объектов игроков с введёнными именами
        player_1 = new Player(namePlayer_1);
        player_2 = new Player(namePlayer_2);
    }

}
