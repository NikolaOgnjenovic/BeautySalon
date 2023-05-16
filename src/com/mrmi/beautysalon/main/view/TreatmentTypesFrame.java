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
    private final HashMap<Integer, TreatmentType> treatmentTypes;
    private JTextField filterText;
    private JButton buttonAdd;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private GenericTable table;
    private GenericTableModel tableModel;
    public TreatmentTypesFrame(TreatmentManager treatmentManager, HashMap<Integer, TreatmentType> treatmentTypes) {
        this.treatmentManager = treatmentManager;
        this.treatmentTypes = treatmentTypes;
        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment types");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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

        tableModel = new GenericTableModel(treatmentTypes, treatmentManager);
        table = new GenericTable(tableModel);
        this.add(new JScrollPane(table), "span, growx");

        filterText = new JTextField(20);
        this.add(filterText);
    }

    private void initialiseListeners() {
        filterText.addActionListener(e -> table.filter(filterText.getText()));

        buttonAdd.addActionListener(e -> {
            AddEditTreatmentTypeDialog edit = new AddEditTreatmentTypeDialog(this, treatmentManager, null, -1);
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
                    TreatmentType treatmentType = treatmentManager.getTreatmentType(id);
                    AddEditTreatmentTypeDialog add = new AddEditTreatmentTypeDialog(this, treatmentManager, treatmentType, id);
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
                    TreatmentType treatmentType = treatmentManager.getTreatmentType(id);
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Are you sure that you want to delete this treatment type?"+
                                    "\n" + treatmentType.getName() +
                                    "\n" + treatmentType.getPrice() +
                                    "\n" + treatmentType.getDuration(),
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
    }

    public void refreshData() {
        GenericTableModel model = new GenericTableModel(treatmentTypes, treatmentManager);
        table.setModel(model);
    }
}
