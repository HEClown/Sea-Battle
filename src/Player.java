public class Player {

    private String name;

    private int[][] myField;
    private int[][] enemyField;
    public static final int FIELD_SIZE = 10;

    public Player(String name) {
        this.name = name;
        this.myField = new int[FIELD_SIZE][FIELD_SIZE];
        this.enemyField = new int[FIELD_SIZE][FIELD_SIZE];
    }

    public String getName() {
        return name;
    }

    public int[][] getMyField() {
        return myField;
    }

    public int[][] getEnemyField() {
        return enemyField;
    }

    public void setMyField(int x, int y, int value) {
        this.myField[x][y] = value;
    }

    public void setEnemyField(int x, int y, int value) {
        this.enemyField[x][y] = value;
    }

}
