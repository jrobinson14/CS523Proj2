import java.util.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Scanner input = new Scanner(System.in);
        System.out.println("Novel Coronavirus Simulation\nPlease enter '1' for Deterministic Ruleset and '2' for " +
                        "Probabilistic Ruleset");
        System.out.println("Or enter '3' to run Genetic Algorithm");
        String response = input.nextLine();
        if(response.equals("1")){
            //run determinstic sim
            System.out.println("Running sim in deterministic mode (loading GUI)");
            CellAutomata determ = new CellAutomata(200, 3, "Deterministic",
                    0, 0, false, null);
            Thread newThread = new Thread(determ);
            newThread.start();
        } else if(response.equals("2")){
            System.out.println("Enable Virus 2? (y/n)");
            String ans = input.nextLine();
            if(ans.equals("Y") || ans.equals("y")){
                System.out.println("Enter a value 1-100 for how infectious Virus 2 should be");
                ans = input.nextLine();
                int infectiousness = Integer.parseInt(ans);
                System.out.println(("Enter a value 1-100 for the rate of recovery (1 is slow, 100 is fast)"));
                ans = input.nextLine();
                int recovery = Integer.parseInt(ans);
                System.out.println("Starting probabilistic sim with Virus 2 (loading GUI");
                CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", infectiousness, recovery,
                        false, null);
                Thread automata = new Thread(testAut);
                automata.start();
            } else if(ans.equals("N") || ans.equals("n")){
                System.out.println("Starting probabilistic sim (just novel coronavirus) (loading GUI)");
                CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", 0, 0,
                        false, null);
                Thread automata = new Thread(testAut);
                automata.start();
            }
        } else if(response.equals("3")){
            System.out.println("Running Genetic algorithm, pleas allow a few minutes for GA to complete...");
            int[] results = runGA(20);
            System.out.printf("Results:\n Ideal infectiousness found: %d\n, Ideal recovery rate found: %d\n", results[0], results[1]);
            //run automata with results of GA
            CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", results[0], results[1],
                    false, null);
            Thread automata = new Thread(testAut);
            automata.start();
        }
        //testing code
        //CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", 20, 10, false, null);
        //Thread automata = new Thread(testAut);
        //automata.start();
        //run GA
        /*int[] results = runGA(20);
        System.out.printf("Results: %d, %d\n", results[0], results[1]);
        //run automata with results of GA
        CellAutomata testAut = new CellAutomata(200, 9, "Probabilistic", results[0], results[1],
                false, null);
        Thread automata = new Thread(testAut);
        automata.start();*/


    }

    /**
     * Run genetic algorithm
     * @param size number of automata desired
     * @return ideal infectiousness and recovery probability for Virus 2
     */
    public static int[] runGA(int size){
        Random rand = new Random();
        //create pool of 100 automata
        ArrayList<CellAutomata> autoList = new ArrayList<CellAutomata>();
        ArrayList<int[]> results = new ArrayList<int[]>();
        ArrayList<int[]> sortedResults = new ArrayList<int[]>();
        for(int x = 0; x < size; x++){
            int infectiousness = rand.nextInt(100);
            int recovery = rand.nextInt(100);
            CellAutomata newAut = new CellAutomata(200, 9, "Probabilistic", infectiousness, recovery, true, results);
            autoList.add(newAut);
        }

        //run automata
        //TODO run a single automata, store the infect and recover values along with results in list, move to next automata
        // and do same then compare results of all to get next gen
        for(int x = 0; x < 5; x++) {
            for (CellAutomata a : autoList) {
                Thread runThread = new Thread(a);
                runThread.run();
                System.out.println("Completed a run");
            }

            System.out.println("completed running all automata");
            //sort results list, code adapted from https://stackoverflow.com/questions/19596950/sort-an-arraylist-of-integer-arrays
            Collections.sort(results, new Comparator<int[]>() {
                private static final int INDEX = 2;

                @Override
                public int compare(int[] o1, int[] o2) {
                    return Integer.compare(o1[INDEX], o2[INDEX]);
                }
            });
            for (int[] autoResult : results) {
                sortedResults.add(autoResult);
            }
            //print sorted results
            for(int i = 0; i < sortedResults.size();i++){
                System.out.printf("%d: %d, %d, %d\n", i, sortedResults.get(i)[0], sortedResults.get(i)[1], sortedResults.get(i)[2]);
            }

            results.clear(); //clear previous values from list
            //Collections.reverse(sortedResults); //reverse the
            int limit = (int) Math.ceil(size * 0.2);
            //select fittest top 20% and put them in a list
            for (int i = 0; i < limit; i++) {
                results.add(sortedResults.get(i));
                //System.out.println(results.get(i));
            }

            sortedResults.clear(); //clear sorted results
            //create "offspring" based on those fittest
            //1st: 40% of new offspring get a random "trait" from each parent
            for (int j = 0; j < Math.ceil(size * 0.4); j++) {
                int selector1 = rand.nextInt(limit - 1); //select random parent for 1st trait
                int selector2 = rand.nextInt(limit - 1); //select random parent for 2nd trait
                int[] newVal = {results.get(selector1)[0], results.get(selector2)[1]};
                results.add(newVal);
            }
            //the next 20% of offspring get one parent value for the first trait and a random value for the second trait
            for (int i = 0; i < Math.ceil(size * 0.2); i++) {
                int selector = rand.nextInt(limit - 1);
                int[] newVal = {results.get(selector)[0], rand.nextInt(100)}; //randomize second trait
                results.add(newVal);
            }
            //now do similar for last 20% of offspring, except randomize the 1st trait now and select the second from a parent
            for (int i = 0; i < Math.ceil(size * 0.2); i++) {
                int selector = rand.nextInt(limit - 1);
                int[] newVal = {rand.nextInt(100), results.get(selector)[1]}; //randomize second trait
                results.add(newVal);
            }
            //print results
            for (int q = 0; q < results.size(); q++) {
                System.out.printf("%d, %d\n", results.get(q)[0], results.get(q)[1]);
            }

            //finally, create the new cell automata. Clear last list of automata and create a list of new ones
            autoList.clear();
            for (int i = 0; i < results.size(); i++) {
                CellAutomata newAut = new CellAutomata(200, 9, "Probabilistic",
                        results.get(i)[0], results.get(i)[1], true, results);
                autoList.add(newAut);
            }
            results.clear();
            System.out.println(autoList.get(1).V2infectivity);
        }
        return new int[]{autoList.get(0).V2infectivity, autoList.get(1).V2recovery};
    }
}
