package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.view.addedit.AddEditTreatmentTypeDialog;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TreatmentTypesFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final HashMap<Integer, TreatmentType> types;
    private JTextField filterText;
    private JButton buttonAdd;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JButton buttonBack;
    private GenericTable table;
    private GenericTableModel tableModel;
    public TreatmentTypesFrame(TreatmentManager treatmentManager, HashMap<Integer, TreatmentType> types) {
        this.treatmentManager = treatmentManager;
        this.types = types;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment types");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("All treatment types"), "span");

        // Toolbar
        JToolBar mainToolbar = new JToolBar();
        mainToolbar.setFloatable(false);

        ImageIcon addIcon = new ImageIcon("src/images/add.gif");
        addIcon = new ImageIcon(addIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        buttonAdd = new JButton();
        buttonAdd.setIcon(addIcon);
        mainToolbar.add(buttonAdd);

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

        tableModel = new GenericTableModel(types, treatmentManager);
        table = new GenericTable(tableModel);
        this.add(new JScrollPane(table), "span, growx");

        filterText = new JTextField(20);
        this.add(filterText);

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");
    }

    private void initialiseListeners() {
        filterText.addActionListener(e -> table.filter(filterText.getText()));

        buttonAdd.addActionListener(e -> {
            AddEditTreatmentTypeDialog edit = new AddEditTreatmentTypeDialog(this, treatmentManager, null);
            edit.setVisible(true);
            tableModel.fireTableDataChanged();
        });

        buttonEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    TreatmentType type = treatmentManager.getTreatmentType(id);
                    AddEditTreatmentTypeDialog add = new AddEditTreatmentTypeDialog(this, treatmentManager, type);
                    add.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing treatment type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    TreatmentType type = treatmentManager.getTreatmentType(id);
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Are you sure that you want to delete this treatment type?"+
                                    "\n" + type.getName() +
                                    "\n" + type.getPrice() +
                                    "\n" + type.getDuration(),
                            "Deletion confirmation", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        treatmentManager.deleteTreatmentType(id);
                        refreshData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing treatment type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    public void refreshData() {
        GenericTableModel model = new GenericTableModel(types, treatmentManager);
        table.setModel(model);
    }
}
