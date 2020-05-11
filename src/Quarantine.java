import java.util.Random;

public class Quarantine {

    public static void main(String[] args) {
        int size = 200;
        CellAutomata testAut = new CellAutomata(size, 9, "Probabilistic", 0, 0,
                false, null, true);
        Thread automata = new Thread(testAut);
        int percentQuarantine = (int) Math.round((size*size)*0.02);
        for(int i = 0; i < percentQuarantine; i++){
            Random rand = new Random();
            int rowVal = rand.nextInt(size - 1);
            int colVal = rand.nextInt(size - 1);
            testAut.cellArray[rowVal][colVal].isolating = true;
        }
        automata.start();
    }
}
