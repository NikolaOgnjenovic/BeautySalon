package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.view.addedit.AddEditTreatmentTypeCategoryDialog;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TreatmentTypeCategoriesFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final HashMap<Integer, TreatmentTypeCategory> categories;
    private JTextField filterText;
    private JButton buttonAdd;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JButton buttonBack;
    private GenericTable table;
    private GenericTableModel tableModel;
    public TreatmentTypeCategoriesFrame(TreatmentManager treatmentManager, HashMap<Integer, TreatmentTypeCategory> categories) {
        this.treatmentManager = treatmentManager;
        this.categories = categories;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatment type categories");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("All treatment type categories"), "span");

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

        tableModel = new GenericTableModel(categories, treatmentManager);
        table = new GenericTable(tableModel);
        this.add(new JScrollPane(table), "span, growx");

        filterText = new JTextField(20);
        this.add(filterText);

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");

        setVisible(true);
    }

    private void initialiseListeners() {
        filterText.addActionListener(e -> table.filter(filterText.getText()));

        buttonAdd.addActionListener(e -> {
            AddEditTreatmentTypeCategoryDialog add = new AddEditTreatmentTypeCategoryDialog(this, treatmentManager, null);
            add.setVisible(true);
            tableModel.fireTableDataChanged();
        });

        buttonEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    TreatmentTypeCategory category = treatmentManager.getTreatmentTypeCategory(id);
                    AddEditTreatmentTypeCategoryDialog edit = new AddEditTreatmentTypeCategoryDialog(this, treatmentManager, category);
                    edit.setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing treatment type category", "Error", JOptionPane.ERROR_MESSAGE);
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
                    TreatmentTypeCategory category = treatmentManager.getTreatmentTypeCategory(id);
                    int choice = JOptionPane.showConfirmDialog(null,
                            "Are you sure that you want to delete this treatment type?"+
                                    "\n" + category.getName(),
                            "Deletion confirmation", JOptionPane.YES_NO_OPTION);
                    if(choice == JOptionPane.YES_OPTION) {
                        treatmentManager.deleteTreatmentTypeCategory(id);
                        refreshData();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing treatment type category", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    public void refreshData() {
        GenericTableModel model = new GenericTableModel(categories, treatmentManager);
        table.setModel(model);
    }
}
