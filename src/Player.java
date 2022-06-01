public class Player {

    private String name;

    private int[][] field;
    public static final int FIELD_SIZE = 10;

    public Player(String name) {
        this.name = name;
        this.field = new int[FIELD_SIZE][FIELD_SIZE];
    }

    public String getName() {
        return name;
    }

    public int[][] getField() {
        return field;
    }

}
