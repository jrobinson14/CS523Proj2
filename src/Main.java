import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //testing code
        //CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", 10, 5, true);
        //Thread automata = new Thread(testAut);
        //automata.start();
        runGA(10);


    }

    /**
     * Run genetic algorithm
     * @param i number of automata desired
     * @return ideal infectiousness and recovery probability for Virus 2
     */
    public static int[][] runGA(int i){
        Random rand = new Random();
        //create pool of 100 automata
        ArrayList<CellAutomata> autoList = new ArrayList<CellAutomata>();

        for(int x = 0; x < i; x++){
            int infectiosness = rand.nextInt(100);
            int recovery = rand.nextInt(100);
            CellAutomata newAut = new CellAutomata(200, 9, "Probabilistic", infectiosness, recovery, true);
            autoList.add(newAut);
        }

        //run automata
        //TODO run a single automata, store the infect and recover values along with results in list, move to next automata
        // and do same then compare results of all to get next gen
        for(CellAutomata a:autoList){
            Thread runThread = new Thread(a);
            runThread.start();
            System.out.println("Completed a run");
        }

        System.out.println("completed running all automata");
        System.out.printf("Results for 1: %d %d\n", autoList.get(0).numberRecoveredV1, autoList.get(0).numberRecoveredV2);
        return null;
    }
}
