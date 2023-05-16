package com.mrmi.beautysalon.main.view.addedit;

import com.mrmi.beautysalon.main.entity.TreatmentType;
import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.view.TreatmentTypesFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AddEditTreatmentTypeDialog extends JDialog {
    private final JFrame parent;
    private final TreatmentManager treatmentManager;
    private final TreatmentType treatmentType;
    private final int id;

    public AddEditTreatmentTypeDialog(JFrame parent, TreatmentManager treatmentManager, TreatmentType treatmentType, int id) {
        super(parent, true);
        this.parent = parent;

        if (treatmentType != null) {
            setTitle("Edit treatment type");
        } else {
            setTitle("Add treatment type");
        }

        this.treatmentManager = treatmentManager;
        this.treatmentType = treatmentType;
        this.id = id;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiseViews();
        pack();
    }

    private void initialiseViews() {
        setLayout(new MigLayout("wrap 2", "", "[]10"));

        //String name, double price, int categoryId, int duration, boolean isDeleted
        add(new JLabel("Name"));
        JTextField textName = new JTextField(20);
        add(textName);

        add(new JLabel("Price"));
        JTextField textPrice = new JTextField(5);
        add(textPrice);

        add(new JLabel("Category"));
        JComboBox<TreatmentTypeCategory> comboBoxCategory = new JComboBox<>();
        for (TreatmentTypeCategory category : treatmentManager.getAvailableTreatmentTypeCategories().values()) {
            comboBoxCategory.addItem(category);
        }
        add(comboBoxCategory);

        add(new JLabel("Duration (minutes):"));
        JTextField textDuration = new JTextField(3);
        add(textDuration);

        JButton buttonCancel = new JButton("Cancel");
        add(buttonCancel);

        JButton buttonOK = new JButton("OK");
        add(buttonOK);

        // If a treatment type is being edited then the treatment type parameter of the constructor != null
        if (treatmentType != null) {
            textName.setText(treatmentType.getName());
            textPrice.setText(String.valueOf(treatmentType.getPrice()));
            comboBoxCategory.setSelectedItem(treatmentManager.getTreatmentTypeCategories().get(treatmentType.getCategoryId()));
            textDuration.setText(String.valueOf(treatmentType.getDuration()));
        }

        buttonOK.addActionListener(e -> {
            String name = textName.getText();
            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double price = Double.parseDouble(textPrice.getText());
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TreatmentTypeCategory category = (TreatmentTypeCategory) comboBoxCategory.getSelectedItem();
            if (category == null) {
                JOptionPane.showMessageDialog(null, "Invalid treatment type category", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int treatmentTypeCategoryId = treatmentManager.getTreatmentTypeCategoryIdByName(category.getName());

            int duration = Integer.parseInt(textDuration.getText());
            if (duration < 0) {
                JOptionPane.showMessageDialog(null, "Invalid duration", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (treatmentType != null) {
                treatmentManager.updateTreatmentType(id, treatmentType, name, price, treatmentTypeCategoryId, duration);
            } else {
                treatmentManager.addTreatmentType(name, price, treatmentTypeCategoryId, duration);
            }

            ((TreatmentTypesFrame) parent).refreshData();
            this.dispose();
        });

        buttonCancel.addActionListener(e -> this.dispose());
    }
}
