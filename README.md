# CS523Proj2

Jonathan Robinson and Jarret Jones
CS 523 (Complex Adaptive Systems)

# Part 2: Cellular Automata to model disease spread

# Running Code

User can run Main.java. Running Main.java will give the user some options in what kind of simulation they would like to run. Users can also create their own main method using the following guide: 

Creating a new cellular automata

1) Create a new cell automata usinge the new command:

        CellAutomata auto = new CellAutomata(args);
        
2) Args for CellAutomata

    Size: How many cells will be created. If size is 10, a 10x10 grid will be created (100 cells)
    
    Note on size: >200 (40,000 cells) will result in a little while for GUI to be created but should run normally
    
    Neighborhood: size of the neighborhood for each cell (should just be 9)
    
    Type: Probabilistic or Deterministic (must use only either of these 2 exact words, case sensative). Runs the sim in probabilistic mode (infection and recovery based on probability) or deterministic (cells recover and are infected at a determined rate)
    
    Virus 2 Infecctiousness: give an int value (0-100) to govern how infectious virus 2 is
    
    Virus 2 recovery: give an int value (0-100) to govern how likely recovery from virus 2 is
    
    forGA: this should be set to false for a single simulation. Only set to ttrue if you want to run the genetic algorithm
    
    list: this is where results of each autonama are stored. If you are running a single simulation, this can be set to null. If you are running a GA, give this an empty int[] array.  
    
3) Create a new thread for the new cell automata:

        Thread newThread = new Thread(auto);
 
4) Start thread:

       newThread.start();
      
      

# CellAutomata

Creates a 2d array of cells and runs simulation

A note on checking infected neighbors in corner cases: since the 
number of neighbors cannot be >3 for corner cells, infected counter is incremented by one

# Cell

Class for individual cell, stores info of cell and contains methods for cell function
