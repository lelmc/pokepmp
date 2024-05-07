package lelmc.pokepmp.ranking;

public class Data {
    private String ID;
    private int score;
    private int winne;
    private int loser;

    public Data(String ID, int score, int winne, int loser) {
        this.ID = ID;
        this.score = score;
        this.winne = winne;
        this.loser = loser;
    }

    public Data() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = Math.max(score, 0);
    }

    public int getWinne() {
        return winne;
    }

    public void setWinne(int winne) {
        this.winne = winne;
    }

    public int getLoser() {
        return loser;
    }

    public void setLoser(int loser) {
        this.loser = loser;
    }
}
