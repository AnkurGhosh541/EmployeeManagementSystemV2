package employee;

import storage.employee.EmployeeRepository;
import user.User;
import user.UserMainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;

public class EmployeeMainFrame extends JFrame implements ActionListener {

    private final User currentUser;
    private boolean ifCEOCreated = false;
    private HashMap<String, Employee> employeeMap;
    private HashMap<String, String> worksUnderMap;
    private final EmployeeRepository employeeRepository;
    private final DefaultTreeModel treeModel;
    private final DefaultTableModel tableModel;
    private final JTree employeeTree;
    private final JTable employeeTable;
    private JComboBox<String> storageType;
    private final CreateForm createForm = new CreateForm();
    private final EditForm editForm = new EditForm();

    public EmployeeMainFrame(User currentUser) {
        this.currentUser = currentUser;
        setExtendedState(MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        employeeMap = new HashMap<>();
        worksUnderMap = new HashMap<>();
        employeeRepository = new EmployeeRepository();
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(tableModel);
        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("temp"));
        employeeTree = new JTree(treeModel);
        employeeTree.setRootVisible(false);
        init();
    }

    private void init() {
        JMenuBar menuBar = new JMenuBar();
        JLabel userLabel = new JLabel("Current User : ");
        JLabel userName = new JLabel(currentUser.getUsername());
        JButton logOutBtn = new JButton("Log Out");

        userLabel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        userLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        userName.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        userName.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 50));
        logOutBtn.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        logOutBtn.setFocusable(false);
        logOutBtn.addActionListener(this);

        menuBar.add(userLabel);
        menuBar.add(userName);
        menuBar.add(logOutBtn, BorderLayout.EAST);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 5, 20));

        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Designation");
        tableModel.addColumn("Department");
        tableModel.addColumn("Salary");

        employeeTable.setBackground(Color.WHITE);
        employeeTable.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        employeeTable.setCellSelectionEnabled(false);
        employeeTable.setColumnSelectionAllowed(false);
        employeeTable.setRowSelectionAllowed(false);
        employeeTable.setRowHeight(24);

        JScrollPane tablePane = new JScrollPane(employeeTable);
        tablePane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        JPanel empPanel = new JPanel();
        BoxLayout bl = new BoxLayout(empPanel, BoxLayout.Y_AXIS);
        empPanel.setLayout(bl);

        JLabel empLabel = new JLabel("Employee List");
        JScrollPane empPane = new JScrollPane();
        empPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        empPane.setBackground(Color.WHITE);
        empPane.setViewportView(employeeTree);

        employeeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) employeeTree.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        employeeTree.setShowsRootHandles(false);
        employeeTree.setPreferredSize(new Dimension(100, -1));
        employeeTree.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));

        empPanel.add(empLabel);
        empPanel.add(empPane);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.75;
        c.weighty = 0.8;
        mainPanel.add(tablePane, c);

        Component b = Box.createRigidArea(new Dimension(20, -1));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.025;
        c.weighty = 0.8;
        mainPanel.add(b, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.425;
        c.weighty = 0.8;
        mainPanel.add(empPanel, c);

        JTabbedPane bottomPane = new JTabbedPane();
        bottomPane.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 50, 20);
        JPanel empTab = new JPanel(fl);
        JPanel optTab = new JPanel(fl);
        JPanel storageTab = new JPanel(fl);

        JButton createBtn = new JButton("CREATE");
        JButton editBtn = new JButton("EDIT");
        JButton deleteBtn = new JButton("DELETE");

        JButton showDetailsBtn = new JButton("Show Details");
        JButton showSubordinatesBtn = new JButton("Show Subordinates");
        JButton showTotalSalaryBtn = new JButton("Show Total Salary");
        JButton showTotalEmpBtn = new JButton("Show Total Number of Employees");

        storageType = new JComboBox<>(new String[]{"File", "Database"});
        JButton saveBtn = new JButton("SAVE");
        JButton loadBtn = new JButton("LOAD");

        createBtn.addActionListener(this);
        editBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        showDetailsBtn.addActionListener(this);
        showSubordinatesBtn.addActionListener(this);
        showTotalEmpBtn.addActionListener(this);
        showTotalSalaryBtn.addActionListener(this);
        saveBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        empTab.add(createBtn);
        empTab.add(editBtn);
        empTab.add(deleteBtn);

        optTab.add(showDetailsBtn);
        optTab.add(showSubordinatesBtn);
        optTab.add(showTotalEmpBtn);
        optTab.add(showTotalSalaryBtn);

        storageTab.add(storageType);
        storageTab.add(saveBtn);
        storageTab.add(loadBtn);

        bottomPane.addTab("Employee", empTab);
        bottomPane.addTab("Operations", optTab);
        bottomPane.addTab("Storage", storageTab);

        setJMenuBar(menuBar);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPane, BorderLayout.SOUTH);
    }

    private void createTable() {
        String[] ids = employeeMap.keySet().toArray(new String[0]);
        Arrays.sort(ids);
        int rows = tableModel.getRowCount();
        for (int i = 0; i < rows; i++) {
            tableModel.removeRow(0);
        }
        employeeTable.revalidate();
        employeeTable.repaint();
        for (String id : ids) {
            Employee employee = employeeMap.get(id);
            String[] row = {employee.getId(), employee.getName(), employee.getDesignation(), employee.getDept(), String.valueOf(employee.getSalary())};
            tableModel.addRow(row);
        }
        employeeTable.revalidate();
        employeeTable.repaint();
    }

    private void deleteTableRow(String id) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (id.equals(tableModel.getValueAt(i, 0))) {
                tableModel.removeRow(i);
                break;
            }
        }
        employeeTable.revalidate();
        employeeTable.repaint();
    }

    private void editTableRow(Employee e) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (e.getId().equals(tableModel.getValueAt(i, 0))) {
                tableModel.setValueAt(e.getName(), i, 1);
                tableModel.setValueAt(e.getDesignation(), i, 2);
                tableModel.setValueAt(e.getDept(), i, 3);
                tableModel.setValueAt(e.getSalary(), i, 4);
                break;
            }
        }
        employeeTable.revalidate();
        employeeTable.repaint();
    }

    public void createTree() {
        treeModel.setRoot(new DefaultMutableTreeNode("temp"));
        ArrayList<DefaultMutableTreeNode> nodeList = new ArrayList<>();
        HashMap<String, DefaultMutableTreeNode> nodeMap = new HashMap<>();

        for (Map.Entry<String, Employee> stringEmployeeEntry : employeeMap.entrySet()) {
            String eid = stringEmployeeEntry.getKey();
            Employee employee = stringEmployeeEntry.getValue();
            if (employee.getDesignation().equalsIgnoreCase("ceo")) {
                ifCEOCreated = true;
                createForm.disableCEOField();
            }
            DefaultMutableTreeNode node;
            if (employee.getDesignation().equalsIgnoreCase("developer") ||
                    employee.getDesignation().equalsIgnoreCase("worker")) {
                node = new DefaultMutableTreeNode(employee, false);
            } else {
                node = new DefaultMutableTreeNode(employee, true);
            }
            nodeList.add(node);
            nodeMap.put(eid, node);
        }

        String nodeId, parentId;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        for (DefaultMutableTreeNode node : nodeList) {
            Employee e = (Employee) node.getUserObject();
            nodeId = e.getId();
            parentId = worksUnderMap.get(nodeId);
            if (parentId == null) {
                root.add(node);
                continue;
            }
            DefaultMutableTreeNode parent = nodeMap.get(parentId);
            parent.add(node);
        }

        treeModel.reload();
        employeeTree.revalidate();
        employeeTree.repaint();
        for (int i = 0; i < employeeTree.getRowCount(); i++) {
            employeeTree.expandRow(i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equalsIgnoreCase("create")) {
            if (ifCEOCreated && (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null)) {
                JOptionPane.showMessageDialog(this, "Select an employee to add its subordinate.", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int opt = JOptionPane.showConfirmDialog(this, createForm, "Create Employee", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                String[] params = createForm.getFields();
                for (String param : params) {
                    if (param.isBlank()) {
                        JOptionPane.showMessageDialog(this, "Fields can not be empty", "Empty Fields", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                try {
                    Integer.parseInt(params[4]);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Salary should be Integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                createForm.clearForm();
                if (params[2].equalsIgnoreCase("worker") || params[2].equalsIgnoreCase("developer")) {
                    Worker w = new Worker(params[0], params[1], params[2], params[3], Integer.parseInt(params[4]));

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(w, false);
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
                    if (!ifCEOCreated) {
                        if (parent == null) {
                            DefaultMutableTreeNode curRoot = (DefaultMutableTreeNode) treeModel.getRoot();
                            curRoot.add(node);
                        } else {
                            Leader superior = (Leader) parent.getUserObject();
                            if (!parent.getAllowsChildren()) {
                                JOptionPane.showMessageDialog(this, "This Employee can not have any subordinates", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            parent.add(node);
                            superior.addSubordinate(w);
                            worksUnderMap.put(w.getId(), superior.getId());
                        }

                    } else {
                        Leader superior = (Leader) parent.getUserObject();
                        if (!parent.getAllowsChildren()) {
                            JOptionPane.showMessageDialog(this, "This Employee can not have any subordinates", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        superior.addSubordinate(w);
                        worksUnderMap.put(w.getId(), superior.getId());
                        treeModel.insertNodeInto(node, parent, parent.getChildCount());
                    }
                    Employee emp = employeeMap.putIfAbsent(w.getId(), w);
                    if (emp != null) {
                        JOptionPane.showMessageDialog(this, "Employee with the same ID already exists.", "ID Conflict", JOptionPane.ERROR_MESSAGE);
                        String sup = worksUnderMap.get(w.getId());
                        worksUnderMap.remove(w.getId(), sup);
                        Leader l = (Leader) employeeMap.get(sup);
                        l.removeSubordinate(w);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }

                    String[] row = {w.getId(), w.getName(), w.getDesignation(), w.getDept(), String.valueOf(w.getSalary())};
                    tableModel.addRow(row);
                    treeModel.reload();
                } else {
                    Leader l = new Leader(params[0], params[1], params[2], params[3], Integer.parseInt(params[4]));
                    if (params[2].equalsIgnoreCase("ceo")) {
                        createForm.disableCEOField();
                        ifCEOCreated = true;
                    }

                    DefaultMutableTreeNode node = new DefaultMutableTreeNode(l, true);
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
                    if (params[2].equalsIgnoreCase("ceo")) {
                        List<DefaultMutableTreeNode> children = new ArrayList<>();
                        for (int i = 0; i < root.getChildCount(); i++) {
                            DefaultMutableTreeNode child = (DefaultMutableTreeNode) root.getChildAt(i);
                            children.add(child);
                        }
                        for (DefaultMutableTreeNode child : children) {
                            node.add(child);
                            l.addSubordinate((Employee) child.getUserObject());
                            Employee sub = (Employee) child.getUserObject();
                            worksUnderMap.put(sub.getId(), l.getId());
                        }
                        root.add(node);
                    } else {
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
                        if (!ifCEOCreated) {
                            if (parent == null) {
                                DefaultMutableTreeNode curRoot = (DefaultMutableTreeNode) treeModel.getRoot();
                                curRoot.add(node);
                            } else {
                                Leader superior = (Leader) parent.getUserObject();
                                if (!parent.getAllowsChildren()) {
                                    JOptionPane.showMessageDialog(this, "This Employee can not have any subordinates", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                parent.add(node);
                                superior.addSubordinate(l);
                                worksUnderMap.put(l.getId(), superior.getId());
                            }

                        } else {
                            Leader superior = (Leader) parent.getUserObject();
                            if (!parent.getAllowsChildren()) {
                                JOptionPane.showMessageDialog(this, "This Employee can not have any subordinates", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            superior.addSubordinate(l);
                            worksUnderMap.put(l.getId(), superior.getId());
                            treeModel.insertNodeInto(node, parent, parent.getChildCount());
                        }
                    }
                    Employee emp = employeeMap.putIfAbsent(l.getId(), l);
                    if (emp != null) {
                        JOptionPane.showMessageDialog(this, "Employee with the same ID already exists.", "ID Conflict", JOptionPane.ERROR_MESSAGE);
                        String sup = worksUnderMap.get(l.getId());
                        worksUnderMap.remove(l.getId(), sup);
                        Leader lm = (Leader) employeeMap.get(sup);
                        lm.removeSubordinate(l);
                        return;
                    } else {
                        JOptionPane.showMessageDialog(this, "Employee created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                    String[] row = {l.getId(), l.getName(), l.getDesignation(), l.getDept(), String.valueOf(l.getSalary())};
                    tableModel.addRow(row);
                    treeModel.reload();
                }
            }
            employeeTree.revalidate();
            employeeTree.repaint();

            employeeTable.revalidate();
            employeeTable.repaint();

            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }

        } else if (cmd.equalsIgnoreCase("edit")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected to edit", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode empNode = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) empNode.getUserObject();

            editForm.setFields(emp);

            int opt = JOptionPane.showConfirmDialog(this, editForm, "Edit Employee", JOptionPane.OK_CANCEL_OPTION);
            if (opt == JOptionPane.OK_OPTION) {
                String[] params = editForm.getFields();

                for (String param : params) {
                    if (param.isBlank()) {
                        JOptionPane.showMessageDialog(this, "Fields can not be empty", "Empty Fields", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                try {
                    Integer.parseInt(params[4]);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Salary should be an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String id = params[0];
                emp = employeeMap.get(id);
                emp.setName(params[1]);
                emp.setDesignation(params[2]);
                emp.setDept(params[3]);
                emp.setSalary(Integer.parseInt(params[4]));

            }
            editTableRow(emp);
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }
            employeeTree.revalidate();
            employeeTree.repaint();

        } else if (cmd.equalsIgnoreCase("Delete")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            employeeMap.remove(emp.getId());
            if (emp.getDesignation().equalsIgnoreCase("ceo")) {
                ifCEOCreated = false;
                createForm.enableCEOField();
            }

            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            List<DefaultMutableTreeNode> children = new ArrayList<>();
            for (int i = 0; i < node.getChildCount(); i++) {
                DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
                children.add(child);
            }
            Employee superior = null;
            if (!parent.isRoot()) {
                superior = (Employee) parent.getUserObject();
            }
            for (DefaultMutableTreeNode child : children) {
                parent.add(child);
                Employee c = (Employee) child.getUserObject();
                if (superior == null) {
                    worksUnderMap.remove(c.getId());
                } else {
                    worksUnderMap.replace(c.getId(), superior.getId());
                }
            }
            parent.remove(node);

            deleteTableRow(emp.getId());
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }
            employeeTree.revalidate();
            employeeTree.repaint();

        } else if (cmd.equalsIgnoreCase("Show Details")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            String worksUnder = worksUnderMap.get(emp.getId());
            if (worksUnder == null)
                worksUnder = "";
            ViewDetails details = new ViewDetails(emp, worksUnder);
            JOptionPane.showMessageDialog(this, details);
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }

        } else if (cmd.equalsIgnoreCase("Show Subordinates")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();

            if (!node.getAllowsChildren()) {
                JOptionPane.showMessageDialog(this, "This employee can not have any subordinates", "", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Leader l = (Leader) node.getUserObject();
            if (l.getSubordinates().isEmpty()) {
                JOptionPane.showMessageDialog(this, "This employee has no subordinates", "", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ViewSubordinates viewSubordinates = new ViewSubordinates(l.getSubordinates().toArray(new Employee[0]));
            viewSubordinates.setVisible(true);
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }

        } else if (cmd.equalsIgnoreCase("Show Total Salary")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            String msg = "Total Salary payable: " + emp.getTotalSalary();
            JOptionPane.showMessageDialog(this, msg);
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }

        } else if (cmd.equalsIgnoreCase("Show Total Number of Employees")) {
            if (employeeMap.isEmpty() || employeeTree.getLastSelectedPathComponent() == null) {
                JOptionPane.showMessageDialog(this, "No employee selected", "Employee not selected", JOptionPane.ERROR_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) employeeTree.getLastSelectedPathComponent();
            Employee emp = (Employee) node.getUserObject();

            String msg = "Total Number of employees: " + emp.getTotalEmployees();
            JOptionPane.showMessageDialog(this, msg);
            treeModel.reload();
            for (int i = 0; i < employeeTree.getRowCount(); i++) {
                employeeTree.expandRow(i);
            }

        } else if (cmd.equalsIgnoreCase("save")) {
            String type = (String) storageType.getSelectedItem();
            employeeRepository.getRepository(type);
            if (employeeMap.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No employee to save !", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean res = employeeRepository.save(employeeMap, worksUnderMap);

            if (!res) {
                JOptionPane.showMessageDialog(this, "Save operation failed !", "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Save operation successful !", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (cmd.equalsIgnoreCase("load")) {
            String type = (String) storageType.getSelectedItem();
            employeeRepository.getRepository(type);

            HashMap[] mapArr = employeeRepository.read();

            if (mapArr == null) {
                JOptionPane.showMessageDialog(this, "Load operation failed !", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            employeeMap = mapArr[0];
            worksUnderMap = mapArr[1];

            createTable();
            createTree();

        } else if (cmd.equalsIgnoreCase("log out")) {
            int opt = JOptionPane.showConfirmDialog(this, "Are you sure ?", "LOG OUT", JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                UserMainFrame uf = new UserMainFrame();
                dispose();
                uf.setVisible(true);
            }
        }
    }
}
