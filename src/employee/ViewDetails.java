package employee;

import javax.swing.*;
import java.awt.*;

public class ViewDetails extends JPanel {

    public ViewDetails(Employee e, String worksUnder) {
        setLayout(new GridLayout(6, 2));

        JLabel idL1 = new JLabel("ID");
        JLabel nameL1 = new JLabel("Name");
        JLabel desiL1 = new JLabel("Designation");
        JLabel deptL1 = new JLabel("Department");
        JLabel salL1 = new JLabel("Salary");
        JLabel worksUnderL1 = new JLabel("Works under");

        JLabel idL2 = new JLabel(e.getId());
        JLabel nameL2 = new JLabel(e.getName());
        JLabel desiL2 = new JLabel(e.getDesignation());
        JLabel deptL2 = new JLabel(e.getDept());
        JLabel salL2 = new JLabel(String.valueOf(e.getSalary()));
        JLabel worksUnderL2 = new JLabel(worksUnder);

        add(idL1);
        add(idL2);
        add(nameL1);
        add(nameL2);
        add(desiL1);
        add(desiL2);
        add(deptL1);
        add(deptL2);
        add(salL1);
        add(salL2);
        add(worksUnderL1);
        add(worksUnderL2);
    }
}
