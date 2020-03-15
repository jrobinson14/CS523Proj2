public class Main {
    public static void main(String[] args) throws InterruptedException {
        CellAutomata testAut = new CellAutomata(100, 1);
        Display board = new Display(testAut,100);
        Thread.sleep(500);
        testAut.infect(1,1);
        board.update();

    }
}
