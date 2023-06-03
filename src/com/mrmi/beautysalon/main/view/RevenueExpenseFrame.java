package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.entity.Employee;
import com.mrmi.beautysalon.main.entity.Treatment;
import com.mrmi.beautysalon.main.entity.User;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import net.miginfocom.swing.MigLayout;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.util.*;

public class RevenueExpenseFrame extends JFrame {
    private final TreatmentManager treatmentManager;

    private JDatePickerImpl fromDatePicker;
    private JDatePickerImpl toDatePicker;
    private Date fromDate;
    private Date toDate;
    private JButton buttonBack;
    private final HashMap<Integer, Treatment> treatments;
    private final HashMap<Integer, User> users;
    private JLabel labelRevenue;
    private JLabel labelExpenses;

    public RevenueExpenseFrame(TreatmentManager treatmentManager, UserManager userManager) {
        this.treatmentManager = treatmentManager;
        this.treatments = treatmentManager.getTreatments();
        this.users = userManager.getUsers();

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow]", "[center, grow]"));
        this.setTitle("Beauty salon - Revenue & Expenses");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        add(new JLabel("Salon revenue & expenses"), "span");


        JLabel fromLabel = new JLabel("From");
        this.add(fromLabel, "align right");
        fromDatePicker = DatePicker.getDatePicker();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        fromDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR));
        fromDatePicker.getModel().setSelected(true);
        this.add(fromDatePicker, "align left");

        JLabel toLabel = new JLabel("To");
        this.add(toLabel, "align right");
        toDatePicker = DatePicker.getDatePicker();
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        toDatePicker.getModel().setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_YEAR));
        toDatePicker.getModel().setSelected(true);
        this.add(toDatePicker, "align left");

        this.add(new JLabel("Revenue:"), "align right");
        labelRevenue = new JLabel();
        this.add(labelRevenue, "align left");

        this.add(new JLabel("Expenses:"), "align right");
        labelExpenses = new JLabel();
        this.add(labelExpenses, "align left");
        refreshData();

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span");
    }

    private void initialiseListeners() {
        fromDatePicker.addActionListener(e -> {
            fromDate = (Date) fromDatePicker.getModel().getValue();
            refreshData();
        });

        toDatePicker.addActionListener(e -> {
            toDate = (Date) toDatePicker.getModel().getValue();
            refreshData();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void refreshData() {
        if (fromDate == null || toDate == null || fromDate.after(toDate)) {
            labelRevenue.setText("0");
            labelExpenses.setText("0");
            return;
        }

        float profit = 0;
        float expenses = 0;
        for (Treatment treatment : treatments.values()) {
            if (treatment.getStatus().equals(Treatment.Status.CANCELLED_BY_CLIENT) || treatment.getStatus().equals(Treatment.Status.CANCELLED_BY_SALON)) {
                Calendar calendar = treatment.getCancellationDate();
                if (calendar == null) {
                    continue;
                }
                Date cancellationDate = calendar.getTime();
                if (cancellationDate.after(fromDate) && cancellationDate.before(toDate)) {
                    expenses += treatment.getPrice() * 0.9;
                }
            } else {
                profit += treatment.getPrice();
            }
        }

        int monthDifference = toDate.getYear() * 12 + toDate.getMonth() - fromDate.getYear() * 12 - fromDate.getMonth();
        for (User user : users.values()) {
            if (user instanceof Employee) {
                expenses += ((Employee) user).getFullSalary(treatmentManager.getBonus()) * monthDifference;
            }
        }

        labelRevenue.setText(String.valueOf(profit));
        labelExpenses.setText(String.valueOf(expenses));

        revalidate();
        repaint();
    }
}
