package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ManagerFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private JButton editUsers;
    private JTextField loyaltyThreshold;
    private JButton editTreatmentTypeCategories;
    private JButton editTreatmentTypes;
    private JButton editTreatments;
    private JButton treatmentStatusGraphButton;
    private JButton treatmentCategoryProfitButton;
    private JButton beauticianStatsButton;

    private JButton cancellationReport;
    private JButton beauticianReport;

    public ManagerFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setTitle("Manager");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        editUsers = new JButton("Edit users");
        this.add(editUsers);

        JLabel loyaltyThresholdLabel = new JLabel("Loyalty threshold");
        this.add(loyaltyThresholdLabel);
        loyaltyThreshold = new JTextField(String.valueOf(beautySalon.getLoyaltyThreshold()));
        this.add(loyaltyThreshold);

        editTreatmentTypeCategories = new JButton("Edit treatment type categories");
        this.add(editTreatmentTypeCategories);

        editTreatmentTypes = new JButton("Edit treatment types");
        this.add(editTreatmentTypes);

        editTreatments = new JButton("Edit all treatments");
        this.add(editTreatments);

        treatmentStatusGraphButton = new JButton("View treatment status graph");
        this.add(treatmentStatusGraphButton);

        beauticianStatsButton = new JButton("View beautician finished treatments graph");
        this.add(beauticianStatsButton);

        treatmentCategoryProfitButton = new JButton("View profit by treatment category graph");
        this.add(treatmentCategoryProfitButton);

        cancellationReport = new JButton("View cancellation report");
        this.add(cancellationReport);

        beauticianReport = new JButton("Beautician profit report");
        this.add(beauticianReport);
        // TODO:
        //  salon profit & loss in a date interval
        //  registerEmployee();
    }

    private void initialiseListeners() {
        editUsers.addActionListener(e -> {
            this.dispose();
            HashMap<String, User> users = userController.getUsers();
            UsersFrame usersFrame = new UsersFrame(userController, users, true, true);
        });

        loyaltyThreshold.addActionListener(e -> beautySalon.setLoyaltyThreshold(Double.parseDouble(loyaltyThreshold.getText())));

        editTreatmentTypeCategories.addActionListener(e -> {
            this.dispose();
            TreatmentTypeCategoriesFrame treatmentTypeCategoriesFrame = new TreatmentTypeCategoriesFrame(treatmentController, treatmentController.getTreatmentTypeCategories(), true, true);
        });

        editTreatmentTypes.addActionListener(e -> {
            this.dispose();
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(treatmentController, treatmentController.getTreatmentTypes(), true, true);
        });

        editTreatments.addActionListener(e -> {
            this.dispose();
            HashMap<Integer, Treatment> treatments = treatmentController.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, treatments, true, true, true, this, beautySalon.getLoyaltyThreshold(), false);
        });

        HashMap<String, Integer> statusCount = treatmentController.getStatusCountMap();
        treatmentStatusGraphButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Treatments by cancellation reason").build();
            chart.getStyler().setSeriesColors(getChartColors(statusCount.size()));
            for (Map.Entry<String, Integer> entry : statusCount.entrySet()) {
                chart.addSeries(entry.getKey(), entry.getValue());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        Collection<Beautician> beauticians = userController.getBeauticians().values();
        beauticianStatsButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Beauticians by number of finished treatments").build();
            chart.getStyler().setSeriesColors(getChartColors(beauticians.size()));
            for (Beautician b : beauticians) {
                chart.addSeries(b.getName(), b.getFinishedTreatments());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories = treatmentController.getTreatmentTypeCategories();
        treatmentCategoryProfitButton.addActionListener(e -> {
            XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Profit").yAxisTitle("Time").build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setSeriesColors(getChartColors(treatmentTypeCategories.size()));
            chart.setXAxisTitle("Month");
            chart.setYAxisTitle("Profit");
            chart.setTitle("Profit by treatment type category per month");
            for (Map.Entry<Integer, TreatmentTypeCategory> category : treatmentTypeCategories.entrySet()) {
                chart.addSeries(category.getValue().getName(), treatmentController.getCategoryProfitByMonths(category.getKey()));
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        cancellationReport.addActionListener(e -> {
            this.dispose();
            CancellationReportFrame cancellationReportFrame = new CancellationReportFrame(treatmentController);
        });

        beauticianReport.addActionListener(e -> {
            this.dispose();
            BeauticianProfitFrame beauticianProfitFrame = new BeauticianProfitFrame(treatmentController);
        });
    }
    private Color[] getChartColors(int size) {
        Color[] chartColors = new Color[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            chartColors[i] = new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        }

        return chartColors;
    }
}
