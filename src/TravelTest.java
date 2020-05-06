public class TravelTest {

    public static void main(String[] args){
        CellAutomata testAut = new CellAutomata(3, 9, "Probabilistic", 0, 0,
                false, null, true);
        Thread automata = new Thread(testAut);
        automata.start();
    }
}
