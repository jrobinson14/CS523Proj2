import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CellAutomata implements Runnable {

    private int size;
    private int neighborhood;
    public int day; //what day it is in the sim
    protected Cell[][] cellArray; //stores all cells
    private String simType; //governs if sim is discrete or using probabilities
    Display display;
    Scanner input = new Scanner(System.in);
    int numberInfectedV1 = 0;
    int numberInfectedV2 = 0;
    int numberRecoveredV1 = 0;
    int numberRecoveredV2 = 0;
    int numberSusceptible; // + numberInfectedV2);
    int V2infectivity; //how infectious virus 2 is
    int V2recovery; //how likely recovery from virus 2 is
    boolean forGA;
    boolean startInfection; //start a CA with no infections
    ArrayList<int[]> resultList;


    public CellAutomata(int size, int neighborhood, String type, int infectivty,
                        int recovery, boolean forGA, ArrayList<int[]> list, boolean infected){
        this.size = size;
        this.neighborhood = neighborhood;
        this.cellArray = new Cell[size][size];
        this.simType = type;
        this.V2infectivity = infectivty;
        this.V2recovery = recovery;
        this.forGA = forGA;
        //System.out.println("Running sim in mode: " + simType);
        this.resultList = list;
        this.startInfection = infected;
        this.numberSusceptible = size * size;
        createAutomata();
        if(forGA == false) {
            this.display = new Display(this, size);
        }
        //run();

    }

    public void run(){
        //each step should be one day
        //go through each cell and look at neighbors current state
        //update each cell
        //update next state -> current state
        //update GUI
        //pause and wait for user input on certain days in order to observe current state
        //sleep
        //System.out.println("Starting Sim");
        if(startInfection) {
            Random rand = new Random();
            int rowVal = rand.nextInt(size - 1);
            int colVal = rand.nextInt(size - 1);
            cellArray[rowVal][colVal].cellState = States.Infected;
            numberInfectedV1++;
            //System.out.println("cell infected:" + cellArray[rowVal][colVal].ID);
        }

        /*TODO activate this for adding additional virus
        //if the sim type is prob., we need two viruses running. Infect another cell with virus 2
        if(simType == "Probabilistic"){
            rowVal = rand.nextInt(size-1);
            colVal = rand.nextInt(size-1);
            cellArray[rowVal][colVal].cellState = States.InfectedVirus2;
        }*/
        //display = new Display(this, size);
        if(forGA == false) {
            display.update(cellArray, day);
        }

        //for recording purposes, comment out if not recording simulation
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Start Sim? (Y/N)");
        String in = input.nextLine();
        if (in.equals("Y") || in.equals("y")) {
            System.out.println("Continuing");
        }
        if (in.equals("N") || in.equals("n")) {
            System.out.println("Ending Sim");
            System.exit(0);
        }

        while(day < 1000) {
            //rowVal = rand.nextInt(size-1);
            //colVal = rand.nextInt(size-1);
            //cellArray[rowVal][colVal].cellState = States.Infected;

            //check neighbors for infection
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    cellArray[i][j].nextState = checkNeighbors(i, j);
                }
            }

            //update cells, if a cell is infected, progress the infection until recovery. Cell's next state becomes current state
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(cellArray[i][j].cellState == States.Infected || cellArray[i][j].cellState == States.InfectedVirus2){
                        if(simType == "Deterministic") {
                            cellArray[i][j].progressInfectionDeterm(); //use deterministic model if specified, otherwise use probabilistic
                        } else if(cellArray[i][j].cellState == States.Infected){ //progress infection of virus 1
                            cellArray[i][j].progressInfectionProbV1();
                            if(cellArray[i][j].nextState == States.Recovered)
                                numberRecoveredV1=numberRecoveredV1; //this shouldnt be here
                        } else if(cellArray[i][j].cellState == States.InfectedVirus2){
                            cellArray[i][j].progressInfectionProbV2(V2recovery); //progress infection of virus 2
                            if(cellArray[i][j].nextState == States.RecoveredVirus2)
                                numberRecoveredV2++;
                        }
                    }
                    cellArray[i][j].cellState = cellArray[i][j].nextState;
                }
            }

            //update display
            day++;
            if(forGA == false) {
                display.update(cellArray, day);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*if (day % 100 == 0) {
                    System.out.printf("Recovered from Virus 1: %d\nRecovered from Virus 2: %d\n", numberRecoveredV1, numberRecoveredV2);

                    System.out.println("Continue Sim? (Y/N)");
                    String ans = input.nextLine();
                    if (ans.equals("Y") || ans.equals("y")) {
                        System.out.println("Continuing");
                    }
                    if (ans.equals("N") || ans.equals("n")) {
                        System.out.println("Ending Sim");
                        System.exit(0);
                    }
                }*/
            }
            moveCells();
        }
        /*if(forGA == false) {
            System.out.println("Complete, Final Results:");
            System.out.printf("Cells Infected with Virus 1: %d\nCells Infected with Virus 2: %d\n" +
                            "Cells Recovered from Virus 1: %d\nCells Recovered from Virus 2: %d\n" +
                            "Difference is: %d\n", numberInfectedV1,
                    numberInfectedV2, numberRecoveredV1, numberRecoveredV2, Math.abs(numberRecoveredV1 - numberRecoveredV2));
        }*/
        if(forGA){ //add results to results list for GA
            int[] results = {V2infectivity, V2recovery, Math.abs(numberRecoveredV1 - numberRecoveredV2)}; //returns data from sim to main for GA
            resultList.add(results);
        }
        //System.exit(0);
    }

    /**
     * create a 2d array of new cells
     * goes through each element of cellArray and creates a new cell at that element
     */
    private void createAutomata(){
        int cellID = 0;
        for(Cell[] cellRow: cellArray){
            for(int i = 0; i < cellRow.length; i++){
                cellRow[i] = new Cell(cellID, this);
                cellID++;
            }
        }
    }

    public Cell[][] getArray(){return cellArray;}

    //just for GUI testing
    public void infect(int x, int y){
        cellArray[x][y].cellState = States.Infected;
    }

    public States checkNeighbors(int i, int j){
        int sickNeighbors = 0;
        int sickNeighborsVirus2 = 0;
        States newState = cellArray[i][j].cellState;
        //check top left
        if((cellArray[i][j].cellState == States.Susceptible && simType == "Deterministic") ||
                (simType == "Probabilistic" && (cellArray[i][j].cellState == States.Susceptible ||
                                                cellArray[i][j].cellState == States.Recovered ||
                                                cellArray[i][j].cellState == States.RecoveredVirus2))){ //|| cellArray[i][j].cellState == States.Recovered || cellArray[i][j].cellState == States.RecoveredVirus2) {
            try {
                if (cellArray[i - 1][j - 1].cellState == States.Infected && cellArray[i - 1][j - 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i - 1][j - 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no top left neighbor here\n", cellArray[i][j].ID);
                //if(simType != "Deterministic")
                  //  sickNeighborsVirus2++; //this is just to balance cells with fewer neighbors (corner neighbors cannot be infected  in discrete sim without this!)
            }
            //check top center
            try {
                if (cellArray[i - 1][j].cellState == States.Infected && cellArray[i - 1][j].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i - 1][j].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                // System.out.printf("Cell %d has no top center neighbor here\n", cellArray[i][j].ID);
            }
            //check top right
            try {
                if (cellArray[i - 1][j + 1].cellState == States.Infected && cellArray[i - 1][j + 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i - 1][j + 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no top right neighbor here\n", cellArray[i][j].ID);
            }
            //check center left
            try {
                if (cellArray[i][j - 1].cellState == States.Infected && cellArray[i][j - 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i][j - 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no center left neighbor here\n", cellArray[i][j].ID);
            }
            //check center right
            try {
                if (cellArray[i][j + 1].cellState == States.Infected && cellArray[i][j + 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i][j + 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no  center right neighbor here\n", cellArray[i][j].ID);
            }
            //check bottom left
            try {
                if (cellArray[i + 1][j - 1].cellState == States.Infected && cellArray[i + 1][j - 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i + 1][j - 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no  bottom left neighbor here\n", cellArray[i][j].ID);
            }
            //check bottom center
            try {
                if (cellArray[i + 1][j].cellState == States.Infected && cellArray[i - 1][j].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i + 1][j].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no  bottom center neighbor here\n", cellArray[i][j].ID);
            }
            //check bottom right
            try {
                if (cellArray[i + 1][j + 1].cellState == States.Infected && cellArray[i + 1][j + 1].isolating == false) {
                    sickNeighbors++;
                }
                else if (cellArray[i + 1][j + 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no bottom right neighbor here\n", cellArray[i][j].ID);
                //if(simType != "Deterministic")
                  //  sickNeighborsVirus2++;
            }

            //System.out.printf("Cell %d has finished checking, found %d infections\n", cellArray[i][j].ID, sickNeighbors);
            //for deterministic model: check if neighbor is infected, if true, become infected
            if (simType.equals("Deterministic")) {
                if (sickNeighbors >= 1 && cellArray[i][j].cellState != States.Infected ) {
                    //System.out.printf("Cell %d is now infected\n", cellArray[i][j].ID);
                    newState = States.Infected;
                } else if (sickNeighborsVirus2 >= 1 && cellArray[i][j].cellState != States.InfectedVirus2){
                    newState = States.InfectedVirus2;
                }
            }

            else if(simType.equals("Probabilistic")){
                //choose if infected based on probability
                if((sickNeighbors > 0 && cellArray[i][j].immuneV1 == false) || (sickNeighborsVirus2 > 0 && cellArray[i][j].immuneV2 == false)) {
                    //System.out.println("Checking infections");
                    newState = cellArray[i][j].infectProb(sickNeighbors, sickNeighborsVirus2, V2infectivity);
                }
            }
        }
        /*if(newState == States.Infected)
            numberInfectedV1 = numberRecoveredV1;
        else if(newState == States.InfectedVirus2)
            numberInfectedV2++;*/
        return newState;
    }

    private void moveCells(){
        Random rand = new Random();
        for(int i = 0; i < 25; i++){ // move 25 ranom cells within a local area
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
            Cell tempCell = cellArray[x1][y1];
            cellArray[x1][y1] = cellArray[x2][y2];
            cellArray[x2][y2] = tempCell;

        }
    }

}
