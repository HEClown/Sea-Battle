public class Player {

    private String name;

    // Счётчик попаданий. Нужен для определения победы
    private int countHits;

    private boolean won;

    // Личное поле игрока, на к-ом он расставляет свои корабли, обновляет их статус
    private int[][] myField;
    // Поле атак, в к-ом игрок помечает свои атаки и их результат
    private int[][] attackField;
    public static final int FIELD_SIZE = 10;

    public Player(String name) {
        this.name = name;
        this.countHits = 0;
        this.won = false;
        this.myField = new int[FIELD_SIZE][FIELD_SIZE];
        this.attackField = new int[FIELD_SIZE][FIELD_SIZE];
    }

    public String getName() {
        return name;
    }

    public boolean isWon() {
        return won;
    }

    public int[][] getMyField() {
        return myField;
    }

    public int[][] getAttackField() {
        return attackField;
    }

    public int getCountHits() {
        return countHits;
    }

    public void setMyField(int x, int y, int value) {
        this.myField[x][y] = value;
    }

    public void setEnemyField(int x, int y, int value) {
        this.attackField[x][y] = value;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public void incrementCountHits() {
        this.countHits++;
    }
}
