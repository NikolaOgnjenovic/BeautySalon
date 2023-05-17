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
    private final TreatmentType type;

    public AddEditTreatmentTypeDialog(JFrame parent, TreatmentManager treatmentManager, TreatmentType type) {
        super(parent, true);
        this.parent = parent;

        if (type != null) {
            setTitle("Edit treatment type");
        } else {
            setTitle("Add treatment type");
        }

        this.treatmentManager = treatmentManager;
        this.type = type;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiseViews();
        pack();
    }

    private void initialiseViews() {
        setLayout(new MigLayout("wrap 2", "", "[]10"));

        //String name, float price, int categoryId, int duration, boolean isDeleted
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
        if (type != null) {
            textName.setText(type.getName());
            textPrice.setText(String.valueOf(type.getPrice()));
            comboBoxCategory.setSelectedItem(treatmentManager.getTreatmentTypeCategories().get(type.getCategoryId()));
            textDuration.setText(String.valueOf(type.getDuration()));
        }

        buttonOK.addActionListener(e -> {
            String name = textName.getText();
            if (name.length() < 1) {
                JOptionPane.showMessageDialog(null, "Invalid name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            float price = Float.parseFloat(textPrice.getText());
            if (price < 0) {
                JOptionPane.showMessageDialog(null, "Invalid price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TreatmentTypeCategory category = (TreatmentTypeCategory) comboBoxCategory.getSelectedItem();
            if (category == null) {
                JOptionPane.showMessageDialog(null, "Invalid treatment type category", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int categoryId = category.getId();

            int duration = Integer.parseInt(textDuration.getText());
            if (duration < 0) {
                JOptionPane.showMessageDialog(null, "Invalid duration", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (type != null) {
                treatmentManager.updateTreatmentType(type, name, price, categoryId, duration);
            } else {
                treatmentManager.addTreatmentType(name, price, categoryId, duration);
            }

            ((TreatmentTypesFrame) parent).refreshData();
            this.dispose();
        });

        buttonCancel.addActionListener(e -> this.dispose());
    }
}
