package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.entity.User;
import com.mrmi.beautysalon.main.view.table.UserTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UsersFrame extends JFrame {

    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public UsersFrame(UserManager userManager, TreatmentManager treatmentManager, AuthManager authManager, SalonManager salonManager, HashMap<String, User> users, boolean canEdit, boolean canDelete) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Users");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        UserTableModel tableModel = new UserTableModel(userManager, treatmentManager, users, canEdit);
        JTable table = new JTable(tableModel){
            final DefaultTableCellRenderer renderLeft = new DefaultTableCellRenderer();

            {
                renderLeft.setHorizontalAlignment(SwingConstants.LEFT);
            }

            @Override
            public TableCellRenderer getCellRenderer (int arg0, int arg1) {
                return renderLeft;
            }
        };
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span, growx");
        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        this.add(filterText);

        if (canDelete) {
            JButton delete = new JButton("Delete user");
            delete.addActionListener(e -> {
                try {
                    userManager.deleteUser(new ArrayList<>(users.keySet()).get(table.getSelectedRow()));
                } catch (UserNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        if (canEdit) {
            JButton learnButton = new JButton("Teach a beautician a new skill");
            this.add(learnButton);

            JComboBox<String> comboBox = new JComboBox<>();
            for (Map.Entry<Integer, TreatmentTypeCategory> entry : treatmentManager.getAvailableTreatmentTypeCategories().entrySet()) {
                comboBox.addItem(entry.getValue().getName() + ", id: " + entry.getKey());
            }
            this.add(comboBox);
            comboBox.setVisible(false);

            learnButton.addActionListener(e -> {
                comboBox.setVisible(true);
                userManager.teachTreatment(new ArrayList<>(users.keySet()).get(table.getSelectedRow()), Byte.parseByte(Objects.requireNonNull(comboBox.getSelectedItem()).toString().split(", id: ")[1]));
            });
        }

        JButton register = new JButton("Register");
        register.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame(treatmentManager, userManager, salonManager, authManager, true);
        });
        this.add(register);

        JButton back = new JButton("Back");
        back.addActionListener(e -> this.dispose());
        this.add(back);
    }

    private void filter(String text) {
        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }
}
