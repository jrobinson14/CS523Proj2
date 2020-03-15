public class Main {
    public static void main(String[] args){
        CellAutomata testAut = new CellAutomata(10, 1);
        Display board = new Display(testAut,10);
        testAut.infect(1,1);
        board.update();

    }
}
