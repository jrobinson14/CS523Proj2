public class Cell {

    public int ID;
    public States cellState;
    public States nextState;
    public int daysInfected;

    public Cell(int id){
        this.ID = id;
        this.cellState = States.Susceptible; //initialized to Susceptible
        //System.out.printf("New Cell %d Created\n", ID);
    }

    public void infect(){
        cellState = States.Infected;
        daysInfected = 0;
    }

    public void progressInfectionDeterm(){
        if(daysInfected < 7){
            daysInfected++;
        } else{
            if(cellState == States.Infected)
                nextState = States.Recovered;
            else if(cellState == States.InfectedVirus2)
                nextState = States.RecoveredVirus2;
            //System.out.printf("Cell %d has recovered\n", ID);
        }
    }

    public void progressInfectionProb(){
        //recover based on fixed probability at each time step
    }
}
