package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.entity.User;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UsersFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private final HashMap<Integer, User> users;
    private JTextField filterText;
    private GenericTable table;
    private JButton buttonDelete;
    private JButton buttonLearn;
    private JButton buttonRegister;
    private JButton buttonEdit;
    private JButton buttonBack;
    private JComboBox<String> comboBoxTreatmentTypeCategory;

    public UsersFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager, HashMap<Integer, User> users) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;
        this.users = users;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Users");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Toolbar
        JToolBar mainToolbar = new JToolBar();
        mainToolbar.setFloatable(false);

        ImageIcon addIcon = new ImageIcon("src/images/add.gif");
        addIcon = new ImageIcon(addIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        buttonRegister = new JButton();
        buttonRegister.setIcon(addIcon);
        mainToolbar.add(buttonRegister);

        ImageIcon editIcon = new ImageIcon("src/images/edit.gif");
        editIcon = new ImageIcon(editIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        buttonEdit = new JButton();
        buttonEdit.setIcon(editIcon);
        mainToolbar.add(buttonEdit);

        ImageIcon deleteIcon = new ImageIcon("src/images/remove.gif");
        deleteIcon = new ImageIcon(deleteIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        buttonDelete = new JButton();
        buttonDelete.setIcon(deleteIcon);
        mainToolbar.add(buttonDelete);

        this.add(mainToolbar);

        GenericTableModel tableModel = new GenericTableModel(users, treatmentManager);
        table = new GenericTable(tableModel);
        this.add(new JScrollPane(table), "span, growx");

        filterText = new JTextField("Search", 20);
        this.add(filterText);

        buttonLearn = new JButton("Teach a beautician a new skill");
        this.add(buttonLearn);

        comboBoxTreatmentTypeCategory = new JComboBox<>();
        for (Map.Entry<Integer, TreatmentTypeCategory> entry : treatmentManager.getAvailableTreatmentTypeCategories().entrySet()) {
            comboBoxTreatmentTypeCategory.addItem(entry.getValue().getName() + ", id: " + entry.getKey());
        }
        this.add(comboBoxTreatmentTypeCategory);
        comboBoxTreatmentTypeCategory.setVisible(false);

        buttonBack = new JButton("Back");
        this.add(buttonBack);
    }

    private void initialiseListeners() {
        filterText.addActionListener(e -> table.filter(filterText.getText()));

        buttonDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    User user = users.get(id);
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Are you sure that you want to delete this user?"+
                                    "\n" + user.getUsername(),
                            "Deletion confirmation", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        userManager.deleteUser(id);
                        refreshData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error processing user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonLearn.addActionListener(e -> {
            comboBoxTreatmentTypeCategory.setVisible(true);
            Object category = comboBoxTreatmentTypeCategory.getSelectedItem();
            if (category != null) {
                try {
                    userManager.teachTreatment((int) table.getValueAt(table.getSelectedRow(), 0), Byte.parseByte(category.toString().split(", id: ")[1]));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "This user cannot learn to do any treatment types", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonRegister.addActionListener(e -> {
            this.dispose();
            RegisterFrame registerFrame = new RegisterFrame(salonManager, treatmentManager, userManager, authManager, true, null, false);
            registerFrame.setVisible(true);
            refreshData();
        });

        buttonEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    User user = users.get(id);
                    RegisterFrame registerFrame = new RegisterFrame(salonManager, treatmentManager, userManager, authManager, true, user, false);
                    registerFrame.setVisible(true);
                    refreshData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error processing user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void refreshData() {
        GenericTableModel model = new GenericTableModel(users, treatmentManager);
        table.setModel(model);
    }
}
