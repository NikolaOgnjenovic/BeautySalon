package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.entity.Beautician;
import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.User;
import com.mrmi.beautysalon.main.view.addedit.EditBeauticianSkillsDialog;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

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
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

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

        buttonLearn = new JButton("Edit a beautician's known treatment type categories");
        this.add(buttonLearn);

        buttonBack = new JButton("Back");
        this.add(buttonBack);

        setVisible(true);
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
            if (table.getSelectedRow() == -1 || !(users.get(table.getValueAt(table.getSelectedRow(), 0)) instanceof Beautician)) {
                JOptionPane.showMessageDialog(null, "This user cannot learn to do any treatment types", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            EditBeauticianSkillsDialog editSkills = new EditBeauticianSkillsDialog(this, (int) table.getValueAt(table.getSelectedRow(), 0), userManager, treatmentManager);
            editSkills.setVisible(true);
        });

        buttonRegister.addActionListener(e -> {
            RegisterFrame registerFrame = new RegisterFrame(salonManager, treatmentManager, userManager, authManager, true, null, false);
            registerFrame.setVisible(true);
            refreshData();
            this.dispose();
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

    public void refreshData() {
        GenericTableModel model = new GenericTableModel(users, treatmentManager);
        table.setModel(model);
    }
}
