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


    public CellAutomata(int size, int neighborhood, String type){
        this.size = size;
        this.neighborhood = neighborhood;
        this.cellArray = new Cell[size][size];
        this.simType = type;
        System.out.println("Running sim in mode: " + simType);
        createAutomata();
        this.display = new Display(this, size);
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
        System.out.println("Starting Sim");
        Random rand = new Random();
        int rowVal = rand.nextInt(size-1);
        int colVal = rand.nextInt(size-1);
        cellArray[rowVal][colVal].cellState = States.InfectedVirus2;
        System.out.println("cell infected:" + cellArray[rowVal][colVal].ID);

        //if the sim type is prob., we neeed two viruses running. Infect another cell with virus 2
        if(simType == "Probabilistic"){
            rowVal = rand.nextInt(size-1);
            colVal = rand.nextInt(size-1);
            cellArray[rowVal][colVal].cellState = States.InfectedVirus2;
        }
        //display = new Display(this, size);
        display.update(cellArray, day);

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

            //update cells
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(cellArray[i][j].cellState == States.Infected || cellArray[i][j].cellState == States.InfectedVirus2){
                        if(simType == "Deterministic") {
                            cellArray[i][j].progressInfectionDeterm(); //use deterministic model if specified, otherwise use probabilistic
                        } else cellArray[i][j].progressInfectionProb();
                    }
                    cellArray[i][j].cellState = cellArray[i][j].nextState;
                }
            }

            //update display
            day++;
            display.update(cellArray, day);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(day % 25 == 0){
                System.out.println("Continue Sim? (Y/N)");
                String ans = input.nextLine();
                if(ans == "Y" || ans == "y")
                    System.out.println("Continuing");
                if(ans == "N" || ans == "n") {
                    System.out.println("Ending Sim");
                    System.exit(0);
                }
            }
        }
    }

    /**
     * create a 2d array of new cells
     * goes through each element of cellArray and creates a new cell at that element
     */
    private void createAutomata(){
        int cellID = 0;
        for(Cell[] cellRow: cellArray){
            for(int i = 0; i < cellRow.length; i++){
                cellRow[i] = new Cell(cellID);
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
        if(cellArray[i][j].cellState == States.Susceptible) {
            try {
                if (cellArray[i - 1][j - 1].cellState == States.Infected) {
                    sickNeighbors++;
                }
                else if (cellArray[i - 1][j - 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no top left neighbor here\n", cellArray[i][j].ID);
                if(simType != "Deterministic")
                    sickNeighborsVirus2++; //this is just to balance cells with fewer neighbors (corner neighbors cannot be infected  in discrete sim without this!)
            }
            //check top center
            try {
                if (cellArray[i - 1][j].cellState == States.Infected) {
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
                if (cellArray[i - 1][j + 1].cellState == States.Infected) {
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
                if (cellArray[i][j - 1].cellState == States.Infected) {
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
                if (cellArray[i][j + 1].cellState == States.Infected) {
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
                if (cellArray[i + 1][j - 1].cellState == States.Infected) {
                    sickNeighbors++;
                }
                else if (cellArray[i + 1][j - 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no  bottome left neighbor here\n", cellArray[i][j].ID);
            }
            //check bottom center
            try {
                if (cellArray[i + 1][j].cellState == States.Infected) {
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
                if (cellArray[i + 1][j + 1].cellState == States.Infected) {
                    sickNeighbors++;
                }
                else if (cellArray[i + 1][j + 1].cellState == States.InfectedVirus2) {
                    sickNeighborsVirus2++;
                }
            } catch (IndexOutOfBoundsException e) {
                //System.out.printf("Cell %d has no bottome right neighbor here\n", cellArray[i][j].ID);
                if(simType != "Deterministic")
                    sickNeighborsVirus2++;
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

            if(simType.equals("Probabilistic")){
                //choose if infected based on probability
            }
        }
        return newState;
    }
}
