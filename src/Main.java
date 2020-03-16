public class Main {
    public static void main(String[] args) throws InterruptedException {
        //testing code
        CellAutomata testAut = new CellAutomata(100, 3, "Discrete");
        Thread automata = new Thread(testAut);
        automata.start();
        /*Display board = new Display(testAut,3);
        Thread.sleep(1000);
        testAut.infect(0,1);
        testAut.infect(1,0);
        testAut.infect(1,1);
        States newState = testAut.checkNeighbors(0,0);
        board.update(1);
        Thread.sleep(1000);
        testAut.getArray()[0][0].cellState = newState;*/

    }
}
