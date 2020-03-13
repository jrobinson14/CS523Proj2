public class Cell {

    public int ID;
    public States cellState;
    public States nextState;
    public int daysInfected;

    public Cell(int id){
        this.ID = id;
        this.cellState = States.Susceptible; //initialized to Susceptible
        System.out.printf("New Cell %d Created\n", ID);
    }

    public void infect(){
        cellState = States.Infected;
        daysInfected = 0;
    }

    public void progressInfection(){
        if(daysInfected < 7){
            daysInfected++;
        } else{
            nextState = States.Recovered;
            System.out.printf("Cell %d has recovered\n", ID);
        }
    }
}
