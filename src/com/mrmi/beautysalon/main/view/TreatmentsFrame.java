package com.mrmi.beautysalon.main.view;


import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.view.addedit.AddEditTreatmentDialog;
import com.mrmi.beautysalon.main.view.table.GenericTable;
import com.mrmi.beautysalon.main.view.table.GenericTableModel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class TreatmentsFrame extends JFrame {
    private final UserManager userManager;
    private final TreatmentManager treatmentManager;
    private final HashMap<Integer, Treatment> treatments;
    private final boolean canEdit;
    private final boolean isClient;

    private JTextField filterText;
    private GenericTable table;
    private GenericTableModel tableModel;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JButton buttonBack;

    public TreatmentsFrame(TreatmentManager treatmentManager, UserManager userManager, HashMap<Integer, Treatment> treatments, boolean canEdit, boolean isClient) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.treatments = treatments;
        this.canEdit = canEdit;
        this.isClient = isClient;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 1", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Treatments");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Toolbar
        JToolBar mainToolbar = new JToolBar();
        mainToolbar.setFloatable(false);

        ImageIcon editIcon = new ImageIcon("src/images/edit.gif");
        editIcon = new ImageIcon(editIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        buttonEdit = new JButton();
        buttonEdit.setIcon(editIcon);
        mainToolbar.add(buttonEdit);

        if (!isClient) {
            ImageIcon deleteIcon = new ImageIcon("src/images/remove.gif");
            deleteIcon = new ImageIcon(deleteIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            buttonDelete = new JButton();
            buttonDelete.setIcon(deleteIcon);
            mainToolbar.add(buttonDelete);
        }

        this.add(mainToolbar);

        // Table model & search
        tableModel = new GenericTableModel(treatments, treatmentManager);
        table = new GenericTable(tableModel);
        this.add(new JScrollPane(table), "span, growx");

        JLabel searchLabel = new JLabel("Search");
        this.add(searchLabel, "split 2");

        filterText = new JTextField(20);
        this.add(filterText);

        double totalCost = treatmentManager.getTotalCost(treatments);
        JLabel total = new JLabel("Total cost: " + totalCost);
        this.add(total);

        JLabel totalRefund = new JLabel(totalCost + " will be refunded if the salon cancels the treatments");
        this.add(totalRefund);

        JLabel clientRefund = new JLabel(0.9*totalCost + " will be refunded if the client cancels the treatments");
        this.add(clientRefund);

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");
    }

    private void initialiseListeners() {
        filterText.addActionListener(e -> table.filter(filterText.getText()));

        buttonEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                    Treatment treatment = treatmentManager.getTreatment(id);
                    if (isClient) {
                        int clientId = userManager.getClientIdByUsername(treatment.getClientUsername());
                        String cancellationReason = JOptionPane.showInputDialog("Why are you cancelling the treatment?");
                        treatmentManager.cancelTreatment(clientId, id, false, cancellationReason, userManager);
                        refreshData();
                    } else {
                        AddEditTreatmentDialog add = new AddEditTreatmentDialog(this, treatmentManager, userManager, treatment, id, false);
                        add.setVisible(true);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error processing treatment", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        });

        if (canEdit) {
            buttonDelete.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a valid table row.", "Error", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        int id = Integer.parseInt(table.getValueAt(row, 0).toString());
                        Treatment treatment = treatmentManager.getTreatment(id);
                        int choice = JOptionPane.showConfirmDialog(null,
                                "Are you sure that you want to delete this treatment?"+
                                        "\n" + treatment.getScheduledDate().getTime() +
                                        "\n" + treatment.getClientUsername() +
                                        "\n" + treatment.getBeauticianUsername(),
                                "Deletion confirmation", JOptionPane.YES_NO_OPTION);
                        if(choice == JOptionPane.YES_OPTION) {
                            treatmentManager.deleteTreatment(id);
                            refreshData();
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error processing treatment", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        buttonBack.addActionListener(e -> this.dispose());
    }

    public void refreshData() {
        tableModel = new GenericTableModel(treatments, treatmentManager);
        table.setModel(tableModel);
    }
}
