import java.util.Random;

/**
 * Class for testing the movement of cells in a single automata (this could maybe be implemented in CellAutomata class.
 * This is just a test class, figuring out how to move cells. Creates a new Cellular Automata and then moves cells within
 * a local area
 */
public class MovingCells {


    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", 0, 0,
                false, null);
        Thread automata = new Thread(testAut);
        automata.start();
        while(true) {
            Thread.sleep(1000);
            for(int i = 0; i < 25; i++){
                int x1 = rand.nextInt(199);
                int y1 = rand.nextInt(199);
                int x2 = rand.nextInt(20) + (x1 - 10);
                int y2 = rand.nextInt(20) + (y1 - 10);
                while(x2 > 199 || x2 < 0){
                    x2 = rand.nextInt(20) + (x1 - 10);
                }
                while(y2 > 199 || y2 < 0){
                    y2 = rand.nextInt(20) + (y1 - 10);
                }
                Cell tempCell = testAut.cellArray[x1][y1];
                testAut.cellArray[x1][y1] = testAut.cellArray[x2][y2];
                testAut.cellArray[x2][y2] = tempCell;

            }
            System.out.println("Finished switching cells");
        }
    }
}
