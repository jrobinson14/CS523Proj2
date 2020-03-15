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


        this.frame = new JFrame("Test");
        this.panelHolder = new JPanel[size][size];

        frame.setLayout(new GridLayout(size,size));

        for(int m = 0; m < size; m++) {
            for(int n = 0; n < size; n++) {
                panelHolder[m][n] = new JPanel();
                panelHolder[m][n].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                frame.add(panelHolder[m][n]);
                if (array[m][n].cellState == States.Susceptible){
                    panelHolder[m][n].setBackground(Color.BLUE);
                }
            }
        }
        //JScrollPane sp = new JScrollPane(table);
        //frame.add();
        frame.setSize(500, 500);
        frame.setVisible(true);

    }

    public void update(){
        for(int m = 0; m < size; m++) {
            for(int n = 0; n < size; n++) {
                if (array[m][n].cellState == States.Susceptible){
                    panelHolder[m][n].setBackground(Color.GREEN);
                } else if (array[m][n].cellState == States.Infected){
                    panelHolder[m][n].setBackground(Color.RED);
                } else if (array[m][n].cellState == States.Recovered) {
                    panelHolder[m][n].setBackground(Color.BLUE);
                }
            }
        }
    }
}
