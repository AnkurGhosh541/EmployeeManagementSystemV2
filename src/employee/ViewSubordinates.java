package employee;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;

public class ViewSubordinates extends JFrame {
    Employee[] subordinates;

    public ViewSubordinates(Employee[] subordinates) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (d.getWidth() / 2 - 400), (int) (d.getHeight() / 2 - 200));
        this.subordinates = subordinates;
        init();
        pack();
    }

    private void init() {
        JScrollPane main = new JScrollPane();
        main.setPreferredSize(new Dimension(800, 400));
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Designation");
        model.addColumn("Department");
        model.addColumn("Salary");

        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        table.setCellSelectionEnabled(false);
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setRowHeight(20);

        for (Employee subordinate : subordinates) {
            Object[] row = new Object[]{
                    subordinate.getId(),
                    subordinate.getName(),
                    subordinate.getDesignation(),
                    subordinate.getDept(),
                    subordinate.getSalary()
            };
            model.addRow(row);
        }

        table.revalidate();
        table.repaint();
        main.setViewportView(table);
        add(main);
    }
}
