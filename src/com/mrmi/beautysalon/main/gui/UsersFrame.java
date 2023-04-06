package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.objects.Database;
import com.mrmi.beautysalon.main.objects.User;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.HashMap;

public class UsersFrame extends JFrame {

    private final TableRowSorter<TableModel> tableSorter;
    private final JTextField filterText;
    public UsersFrame(Database database, HashMap<String, User> users, boolean canEdit, boolean canDelete) {
        UserTableModel tableModel = new UserTableModel(database, users, canEdit);
        JTable table = new JTable(tableModel);
        this.add(new JScrollPane(table));
        this.setTitle("Users");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLayout(new FlowLayout());

        table.setAutoCreateRowSorter(true);
        tableSorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(tableSorter);

        filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText()));
        this.add(filterText);

        // Svinjarija
        if (canDelete) {
            JButton delete = new JButton("Delete user");
            delete.addActionListener(e -> {
                try {
                    database.deleteUser(users.keySet().stream().toList().get(table.getSelectedRow()));
                } catch (UserNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                tableModel.fireTableDataChanged();
            });
            this.add(delete);
        }
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
