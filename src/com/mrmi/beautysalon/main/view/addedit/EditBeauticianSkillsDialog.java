package com.mrmi.beautysalon.main.view.addedit;

import com.mrmi.beautysalon.main.entity.TreatmentTypeCategory;
import com.mrmi.beautysalon.main.exceptions.UserNotFoundException;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.view.UsersFrame;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EditBeauticianSkillsDialog  extends JDialog {
    private final JFrame parent;
    private final int beauticianId;
    private final UserManager userManager;
    private final TreatmentManager treatmentManager;

    public EditBeauticianSkillsDialog(JFrame parent, int beauticianId, UserManager userManager, TreatmentManager treatmentManager) {
        super(parent, true);
        this.parent = parent;
        this.beauticianId = beauticianId;
        this.userManager = userManager;
        this.treatmentManager = treatmentManager;

        setTitle("Edit beauitician's skills");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initialiseViews();
        pack();
    }

    private void initialiseViews() {
        setLayout(new MigLayout("wrap 2", "", "[]10"));

        // Load categories into an array
        Object[] categories = treatmentManager.getTreatmentTypeCategories().values().toArray();
        CheckListItem[] items = new CheckListItem[categories.length];

        List<Integer> knownSkills;
        try {
            knownSkills = userManager.getBeautician(beauticianId).getTreatmentTypeCategoryIDs();
        } catch (UserNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Invalid beautician id", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add a checklistitem to the list for every treatment type category
        for (int i = 0; i < categories.length; i++) {
            TreatmentTypeCategory category = (TreatmentTypeCategory) categories[i];
            items[i] = new CheckListItem(category.getName());

            // If the beautician knows the given skill, select it
            for (int j = 0; j < categories.length; j++) {
                if (knownSkills.contains(category.getId())) {
                    items[i].setSelected(true);
                }
            }
        }

        // List display options
        JList<CheckListItem> list = new JList<>(items);
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Toggle the selected check list item's selected state on click
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                JList<CheckListItem> list = (JList) event.getSource();
                int index = list.locationToIndex(event.getPoint());

                CheckListItem item = list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected()); // Toggle selected state
                list.repaint(list.getCellBounds(index, index)); // Repaint cell
            }
        });
        add(new JScrollPane(list));

        JButton buttonOK = new JButton("OK");
        add(buttonOK);

        buttonOK.addActionListener(e -> {
            try {
                // Clear the beautician's skillset, teach him the skill for each selected item
                userManager.clearSkills(beauticianId);
                for (int i = 0; i < categories.length; i++) {
                    if (items[i].isSelected()) {
                        userManager.teachTreatment(beauticianId, (byte) ((TreatmentTypeCategory) categories[i]).getId());
                    }
                }
            } catch (UserNotFoundException ex) {
                JOptionPane.showMessageDialog(null, "Invalid treatment type category or beautician id", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ((UsersFrame) parent).refreshData();
            this.dispose();
        });
    }
}