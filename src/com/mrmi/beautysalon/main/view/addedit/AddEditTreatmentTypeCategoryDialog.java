package com.mrmi.beautysalon.main.view.addedit;

import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.view.TreatmentTypeCategoriesFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AddEditTreatmentTypeCategoryDialog extends JDialog {
    private final JFrame parent;
    private final TreatmentManager treatmentManager;
    private final TreatmentTypeCategory category;

    public AddEditTreatmentTypeCategoryDialog(JFrame parent, TreatmentManager treatmentManager, TreatmentTypeCategory category) {
        super(parent, true);
        this.parent = parent;

        if (category != null) {
            setTitle("Edit treatment type category");
        } else {
            setTitle("Add treatment type category");
        }

        this.treatmentManager = treatmentManager;
        this.category = category;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiseViews();
        pack();
    }

    private void initialiseViews() {
        setLayout(new MigLayout("wrap 2", "", "[]10"));

        add(new JLabel("Name"));
        JTextField textName = new JTextField(20);
        add(textName);

        JButton buttonCancel = new JButton("Cancel");
        add(buttonCancel);

        JButton buttonOK = new JButton("OK");
        add(buttonOK);

        if (category != null) {
            textName.setText(category.getName());
        }

        buttonOK.addActionListener(e -> {
            String name = textName.getText();
            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (category != null) {
                treatmentManager.updateTreatmentTypeCategoryName(category, name);
            } else {
                treatmentManager.addTreatmentTypeCategory(name);
            }

            ((TreatmentTypeCategoriesFrame) parent).refreshData();
            this.dispose();
        });

        buttonCancel.addActionListener(e -> this.dispose());
    }
}