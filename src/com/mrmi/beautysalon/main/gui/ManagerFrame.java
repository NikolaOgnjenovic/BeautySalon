package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.*;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ManagerFrame extends JFrame {
    public ManagerFrame(Database database) {
        this.setTitle("Manager");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        JButton editUsers = new JButton("Edit users");
        editUsers.addActionListener(e -> {
            this.dispose();
            HashMap<String, User> users = database.getUsers();
            UsersFrame usersFrame = new UsersFrame(database, users, true, true);
        });
        this.add(editUsers);

        JLabel loyaltyThresholdLabel = new JLabel("Loyalty threshold");
        this.add(loyaltyThresholdLabel);
        JTextField loyaltyThreshold = new JTextField(String.valueOf(database.getLoyaltyThreshold()));
        loyaltyThreshold.addActionListener(e -> database.setLoyaltyThreshold(Double.parseDouble(loyaltyThreshold.getText())));
        this.add(loyaltyThreshold);

        JButton editTreatmentTypeCategories = new JButton("Edit treatment type categories");
        editTreatmentTypeCategories.addActionListener(e -> {
            this.dispose();
            TreatmentTypeCategoriesFrame treatmentTypeCategoriesFrame = new TreatmentTypeCategoriesFrame(database, database.getTreatmentTypeCategories(), true, true);
        });
        this.add(editTreatmentTypeCategories);

        // TODO: graph by profit
        JButton editTreatmentTypes = new JButton("Edit treatment types");
        editTreatmentTypes.addActionListener(e -> {
            this.dispose();
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(database, database.getTreatmentTypes(), true, true);
        });
        this.add(editTreatmentTypes);

        // TODO: graph treatments by cancellation reason by date
        JButton editTreatments = new JButton("Edit all treatments");
        editTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> treatments = database.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(database, treatments, true, true, true, this);
        });
        this.add(editTreatments);

        Random random = new Random();
        JButton treatmentStatusGraphButton = new JButton("View treatment status graph");
        Collection<Treatment> treatments = database.getTreatments().values();
        Color[] sliceColors = new Color[treatments.size()];
        for (int i = 0; i < treatments.size(); i++) {
            sliceColors[i] = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }
        Color[] finalSliceColors1 = sliceColors;
        HashMap<String, Integer> statusCount = database.getStatusCountMap();
        treatmentStatusGraphButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Treatments by cancellation reason").build();
            chart.getStyler().setSeriesColors(finalSliceColors1);
            for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
                chart.addSeries(entry.getKey(), entry.getValue());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });
        this.add(treatmentStatusGraphButton);

        JButton beauticianStatsButton = new JButton("View beautician finished treatments graph");
        Collection<Beautician> beauticians = database.getBeauticians().values();
        sliceColors = new Color[beauticians.size()];
        for (int i = 0; i < beauticians.size(); i++) {
            sliceColors[i] = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }
        Color[] finalSliceColors = sliceColors;
        beauticianStatsButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Beauticians by number of finished treatments").build();
            chart.getStyler().setSeriesColors(finalSliceColors);
            for (Beautician b : beauticians) {
                chart.addSeries(b.getName(), b.getFinishedTreatments());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });
        this.add(beauticianStatsButton);

        JButton treatmentCategoryProfitButton = new JButton("View profit by treatment category graph");
        Collection<TreatmentTypeCategory> categories = database.getTreatmentTypeCategories().values();
        Color[] chartColors = new Color[beauticians.size()];
        for (int i = 0; i < categories.size(); i++) {
            chartColors[i] = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }
        treatmentCategoryProfitButton.addActionListener(e -> {
            XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Profit").yAxisTitle("Time").build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setSeriesColors(finalSliceColors);
            for (TreatmentTypeCategory category : categories) {
                //chart.addSeries(category.getName(), category.getProfit(), category.getTreatmentTypeIds());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });
        this.add(treatmentCategoryProfitButton);


        // TODO:
        //  salon profit & loss in a date interval
        //  registerEmployee();
    }
}
