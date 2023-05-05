package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.SalonController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.BeautySalon;
import com.mrmi.beautysalon.main.entity.TreatmentType;
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
    public UsersFrame(UserController userController, TreatmentController treatmentController, AuthController authController, BeautySalon beautySalon, HashMap<String, User> users, boolean canEdit, boolean canDelete) {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Users");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);

        UserTableModel tableModel = new UserTableModel(userController, treatmentController, users, canEdit);
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
        Utility.setFont(table, 20);
        table.setRowHeight(22);
        this.add(new JScrollPane(table), "span, growx");
        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        Utility.setFont(filterText, 24);
        this.add(filterText);

        if (canDelete) {
            JButton delete = new JButton("Delete user");
            Utility.setFont(delete, 24);
            delete.addActionListener(e -> {
                try {
                    userController.deleteUser(new ArrayList<>(users.keySet()).get(table.getSelectedRow()));
                } catch (UserNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }

        if (canEdit) {
            JButton learnButton = new JButton("Teach a beautician a new skill");
            Utility.setFont(learnButton, 24);
            this.add(learnButton);

            JComboBox<String> comboBox = new JComboBox<>();
            for (Map.Entry<Integer, TreatmentType> entry : treatmentController.getTreatmentTypes().entrySet()) {
                comboBox.addItem(entry.getValue().getName() + ", id: " + entry.getKey());
            }
            Utility.setFont(comboBox, 24);
            this.add(comboBox);
            comboBox.setVisible(false);

            learnButton.addActionListener(e -> {
                comboBox.setVisible(true);
                userController.teachTreatment(new ArrayList<>(users.keySet()).get(table.getSelectedRow()), Byte.parseByte(Objects.requireNonNull(comboBox.getSelectedItem()).toString().split(", id: ")[1]));
            });
        }

        JButton register = new JButton("Register");
        Utility.setFont(register, 24);
        register.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame(treatmentController, userController, beautySalon, authController, true);
        });
        this.add(register);

        JButton back = new JButton("Back");
        back.addActionListener(e -> this.dispose());
        Utility.setFont(back, 24);
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
