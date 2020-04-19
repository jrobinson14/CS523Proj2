import java.util.Random;

public class Cell {

    public int ID;
    public States cellState;
    public States nextState;
    public int daysInfected;
    Random rand;
    public boolean immuneV1;
    public boolean immuneV2;
    public boolean isolating;

    public Cell(int id){
        this.ID = id;
        this.cellState = States.Susceptible; //initialized to Susceptible
        this.rand = new Random();
        this.immuneV1 = false;
        this.immuneV2 = false;
        this.isolating = false;
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
            if(cellState == States.Infected) {
                nextState = States.Recovered;
                immuneV1 = true;
            }
            else if(cellState == States.InfectedVirus2) {
                nextState = States.RecoveredVirus2;
                immuneV2 = true;
                isolating = false;
            }
            //System.out.printf("Cell %d has recovered\n", ID);
        }
    }

    /**
     * determine if cell will become infected with virus
     * @param numInfected number of neighbors infected, determined by CellAutomata
     */
    public States infectProb(int numInfected, int numInfectedV2, int V2infectivity){
        //TODO maybe adjust probability, currently each infected neighbor adds 5% chance of infection
        int risk = numInfected * 10; // 10/100 (10% chance)
        int riskV2 = numInfectedV2 * V2infectivity;
        int riskVal = rand.nextInt(100);
        int riskValV2 = rand.nextInt(100);
        if(riskVal < risk && riskValV2 < riskV2){ //TODO test this, should trigger if risk of infection for both viruses
            if(riskVal > riskValV2) //risk for virus 1 higher than virus 2, infected with virus 1
                return States.Infected;
            else return States.InfectedVirus2;
        } else if(riskVal < risk){ //infected with virus 1
            return States.Infected;
        } else if(riskValV2 < riskV2){ //infected with virus 2
            return States.InfectedVirus2;
        } else return cellState;
    }

    /**
     * progress infection with increasing chance of recovery for Virus 1 (based on COVID-19
     * Recovery chance increases by 2% each day (COVID-19 takes around 2 weeks to recover on avg.)
     */
    public void progressInfectionProbV1(){
        int recoveryProb = 0;
        //TODO work on this isolation probability
        //if not isolating already, maybe isolate
        if(!isolating) {
            int isolationProb = rand.nextInt(100) * daysInfected;
            if (isolationProb > 25)
                isolating = true;
        }

        //progress the infection probabilistically
        if(daysInfected >= 5) { //start recovery at infection day 5, around which symptoms start
            if(daysInfected < 14)
                recoveryProb = daysInfected * 6; //80% of cases resolve after ~2 weeks
            else
                recoveryProb = daysInfected * 3; //Cases taking over 2 weeks (more severe cases) can take up to 6 weeks to resolve
            int recov = rand.nextInt(100);
            if (recov < recoveryProb) {
                //recover
                nextState = States.Recovered;
                immuneV1 = true;
                isolating = false;
            } else daysInfected++;
        } else daysInfected++;
    }

    /**
     * progress infection with increasing chance of recovery for Virus 2 (novel virus entering environment)
     * @param probability modifier to how lilkey recovery will be (use to evolve virus 2)
     */
    public void progressInfectionProbV2(int probability){
        int recoveryProb = 0;
        recoveryProb = daysInfected * probability;
        int recov = rand.nextInt(100);
        if (recov < recoveryProb) {
            //recover
            nextState = States.RecoveredVirus2;
            immuneV2 = true;
        } else daysInfected++;
    }
}
