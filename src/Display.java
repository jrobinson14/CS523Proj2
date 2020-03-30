import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Display {

    CellAutomata myAut;
    Cell[][] array;
    int size;
    //DefaultTableModel tm = new DefaultTableModel(0,0);
    JFrame frame;
    JPanel[][] panelHolder;


    public Display(CellAutomata myAut, int size){
        this.myAut = myAut;
        this.array = myAut.getArray();
        this.size = size;
        /*String[][] test = {{"00", "01"},{"1,0","1,1"}};
        String[] names = {"1"};
        JFrame frame = new JFrame("Test");
        tm.setColumnCount(size);
        JTable table = new JTable();
        table.setModel(tm);
        table.setBounds(30, 40, 300, 300);
        for(int i = 0; i < size; i++){
            tm.addRow(array[i]);
        }*/


        this.frame = new JFrame("Day: 0");
        this.panelHolder = new JPanel[size][size];

        frame.setLayout(new GridLayout(size,size));

        for(int m = 0; m < size; m++) {
            for(int n = 0; n < size; n++) {
                panelHolder[m][n] = new JPanel();
                //panelHolder[m][n].setBorder(BorderFactory.createLineBorder(Color.BLACK)); //TODO: remove in final, dont want borders
                frame.add(panelHolder[m][n]);
                if (array[m][n].cellState == States.Susceptible){
                    panelHolder[m][n].setBackground(Color.GREEN);
                }
            }
        }
        JTextArea counter = new JTextArea("Infected: " + myAut.numberInfected);
        //JScrollPane sp = new JScrollPane(table);
        frame.setSize(500, 500);
        frame.setVisible(true);

    }

    public void update(Cell[][] upArray, int day){
        //System.out.println("Updating display\nCell 0,0 is:" + upArray[0][0].cellState);
        //array = myAut.getArray();
        for(int m = 0; m < size; m++) {
            for(int n = 0; n < size; n++) {
                if (array[m][n].cellState == (States.Susceptible)){
                    panelHolder[m][n].setBackground(Color.GREEN);
                } else if (array[m][n].cellState == (States.Infected)){
                    panelHolder[m][n].setBackground(Color.RED);
                } else if (array[m][n].cellState == (States.Recovered)) {
                    panelHolder[m][n].setBackground(Color.BLUE);
                } else if (array[m][n].cellState == (States.InfectedVirus2)) {
                    panelHolder[m][n].setBackground(Color.ORANGE);
                } else if (array[m][n].cellState == (States.RecoveredVirus2)){
                    panelHolder[m][n].setBackground(Color.BLACK);
                }
            }
        }
        frame.setTitle("Day: " + Integer.toString(day));
    }
}
