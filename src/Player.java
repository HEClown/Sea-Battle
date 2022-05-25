public class Player<i> {

    private String name;

    private int[][] fieldPlayer;
    public static final int FIELD_SIZE = 10;

    public Player(String name) {
        this.name = name;
        this.fieldPlayer = new int[FIELD_SIZE][FIELD_SIZE];
    }

    public String getName() {
        return name;
    }

    public int[][] getFieldPlayer() {
        return fieldPlayer;
    }

}
