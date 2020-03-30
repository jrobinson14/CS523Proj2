import java.util.Random;

public class Cell {

    public int ID;
    public States cellState;
    public States nextState;
    public int daysInfected;
    Random rand;

    public Cell(int id){
        this.ID = id;
        this.cellState = States.Susceptible; //initialized to Susceptible
        this.rand = new Random();
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

    /**
     * determine if cell will become infected with virus
     * @param numInfected number of neighbors infected, determined by CellAutomata
     */
    public States infectProb(int numInfected){
        //TODO maybe adjust probability, currently each infected neighbor adds 5% chance of infection
        int risk = numInfected * 15; // 5/100 (5% chance)
        int riskVal = rand.nextInt(100);
        if(riskVal < risk){
            //System.out.println("cell " + ID + " infected");
            return States.Infected;
        } else return States.Susceptible;
    }

    /**
     * progress infection with increasing chance of recovery
     * Recovery chance increases by 2% each day (COVID-19 takes around 2 weeks to recover on avg.)
     */
    public void progressInfectionProb(){
        int recoveryProb = 0;
        if(daysInfected >= 5) { //start recovery at infection day 5, around which symptoms start
            if(daysInfected < 14)
                recoveryProb = daysInfected * 6; //80% of cases resolve after ~2 weeks
            else
                recoveryProb = daysInfected * 3; //Cases taking over 2 weeks (more severe cases) can take up to 6 weeks to resolve
            int recov = rand.nextInt(100);
            if (recov < recoveryProb) {
                //recover
                nextState = States.Recovered;
            } else daysInfected++;
        } else daysInfected++;
    }
}
