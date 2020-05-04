public class TravelTest {

    public static void main(String[] args){
        CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", 0, 0,
                false, null, true);
        Thread automata = new Thread(testAut);
        automata.start();

        CellAutomata testAut2 = new CellAutomata(200, 9, "Probabilistic", 0, 0,
                false, null, false);
        Thread automata2 = new Thread(testAut2);
        automata2.start();
    }
}
