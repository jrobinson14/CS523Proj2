public class CellAutomata implements Runnable {

    private int size;
    private int neighborhood;
    public int day; //what day it is in the sim
    private Cell[][] cellArray; //stores all cells
    private String simType; //governs if sim is discrete or using probabilities


    public CellAutomata(int size, int neighborhood, String type){
        this.size = size;
        this.neighborhood = neighborhood;
        this.cellArray = new Cell[size][size];
        this.simType = type;
        System.out.println("Running sim in mode: " + simType);
        createAutomata();

    }

    public void run(){
        //each step should be one day
        //go through each cell and look at neighbors current state
        //update each cell
        //update next state -> current state
        //update GUI
        //pause and wait for user input on certain days in order to observe current state
        //sleep

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
        States newState = States.Susceptible;
        //check top left
        try{
            if(cellArray[i-1][j-1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no top left neighbor here\n", cellArray[i][j].ID);
            sickNeighbors++; //this is just to balance cells with fewer neighbors (corner neighbors cannot be infected  in discrete sim without this!)
        }
        //check top center
        try{
            if(cellArray[i-1][j].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no top center neighbor here\n", cellArray[i][j].ID);
        }
        //check top right
        try{
            if(cellArray[i-1][j+1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no top right neighbor here\n", cellArray[i][j].ID);
        }
        //check center left
        try{
            if(cellArray[i][j-1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no center left neighbor here\n", cellArray[i][j].ID);
        }
        //check center right
        try{
            if(cellArray[i][j+1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no  center right neighbor here\n", cellArray[i][j].ID);
        }
        //check bottom left
        try{
            if(cellArray[i+1][j-1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no  bottome left neighbor here\n", cellArray[i][j].ID);
        }
        //check bottome center
        try{
            if(cellArray[i+1][j].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no  bottom center neighbor here\n", cellArray[i][j].ID);
        }
        //check bottome right
        try{
            if(cellArray[i+1][j+1].cellState == States.Infected)
                sickNeighbors++;
        } catch(IndexOutOfBoundsException e){
            System.out.printf("Cell %d has no bottome right neighbor here\n", cellArray[i][j].ID);
        }

        System.out.printf("Cell %d has finished checking, found %d infections\n", cellArray[i][j].ID, sickNeighbors);

        if(simType.equals("Discrete")){
            if(sickNeighbors >= 4) {
                System.out.printf("Cell %d is now infected\n", cellArray[i][j].ID);
                newState = States.Infected;
            } else
                System.out.printf("Cell %d is ok!\n", cellArray[i][j].ID);
        }
        return newState;
    }
}
