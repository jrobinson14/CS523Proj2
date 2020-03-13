public class CellAutomata implements Runnable {

    private int size;
    private int neighborhood;
    public int day; //what day it is in the sim
    private Cell[][] cellArray; //stores all cells


    public CellAutomata(int size, int neighborhood){
        this.size = size;
        this.neighborhood = neighborhood;
        this.cellArray = new Cell[size][size];
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
}
