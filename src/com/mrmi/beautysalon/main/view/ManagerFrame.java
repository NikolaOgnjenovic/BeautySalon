package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.controller.AuthController;
import com.mrmi.beautysalon.main.controller.TreatmentController;
import com.mrmi.beautysalon.main.controller.UserController;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ManagerFrame extends JFrame {
    private final TreatmentController treatmentController;
    private final UserController userController;
    private final BeautySalon beautySalon;
    private final AuthController authController;
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
    private JButton logout;

    public ManagerFrame(TreatmentController treatmentController, UserController userController, BeautySalon beautySalon, AuthController authController) {
        this.treatmentController = treatmentController;
        this.userController = userController;
        this.beautySalon = beautySalon;
        this.authController = authController;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2, debug", "[center, grow]40", "[center, grow]40"));
        this.setTitle("Beauty salon - Manager");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        editUsers = new JButton("Edit users");
        Utility.setFont(editUsers, 24);
        this.add(editUsers, "span");

        JLabel loyaltyThresholdLabel = new JLabel("Loyalty threshold");
        Utility.setFont(loyaltyThresholdLabel, 24);
        this.add(loyaltyThresholdLabel, "align right");

        loyaltyThreshold = new JTextField(String.valueOf(beautySalon.getLoyaltyThreshold()));
        Utility.setFont(loyaltyThreshold, 24);
        this.add(loyaltyThreshold, "align left");

        editTreatmentTypeCategories = new JButton("Edit treatment type categories");
        Utility.setFont(editTreatmentTypeCategories, 24);
        this.add(editTreatmentTypeCategories);

        editTreatmentTypes = new JButton("Edit treatment types");
        Utility.setFont(editTreatmentTypes, 24);
        this.add(editTreatmentTypes);

        editTreatments = new JButton("Edit all treatments");
        Utility.setFont(editTreatments, 24);
        this.add(editTreatments);

        treatmentStatusGraphButton = new JButton("View treatment status graph");
        Utility.setFont(treatmentStatusGraphButton, 24);
        this.add(treatmentStatusGraphButton);

        beauticianStatsButton = new JButton("View beautician finished treatments graph");
        Utility.setFont(beauticianStatsButton, 24);
        this.add(beauticianStatsButton);

        beauticianReport = new JButton("Beautician profit report");
        Utility.setFont(beauticianReport, 24);
        this.add(beauticianReport);

        treatmentCategoryProfitButton = new JButton("View profit by treatment category graph");
        Utility.setFont(treatmentCategoryProfitButton, 24);
        this.add(treatmentCategoryProfitButton);

        cancellationReport = new JButton("View cancellation report");
        Utility.setFont(cancellationReport, 24);
        this.add(cancellationReport);

        // TODO:
        //  salon profit & loss in a date interval
        //  registerEmployee();

        logout = new JButton("Logout");
        Utility.setFont(logout, 24);
        this.add(logout, "span");
    }

    private void initialiseListeners() {
        editUsers.addActionListener(e -> {
            HashMap<String, User> users = userController.getUsers();
            UsersFrame usersFrame = new UsersFrame(userController, treatmentController, authController, beautySalon, users, true, true);
        });

        loyaltyThreshold.addActionListener(e -> beautySalon.setLoyaltyThreshold(Double.parseDouble(loyaltyThreshold.getText())));

        editTreatmentTypeCategories.addActionListener(e -> {
            TreatmentTypeCategoriesFrame treatmentTypeCategoriesFrame = new TreatmentTypeCategoriesFrame(treatmentController, treatmentController.getTreatmentTypeCategories(), true, true);
        });

        editTreatmentTypes.addActionListener(e -> {
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(treatmentController, treatmentController.getTreatmentTypes(), true, true);
        });

        editTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> treatments = treatmentController.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentController, userController, treatments, true, true, true, beautySalon.getLoyaltyThreshold(), false);
        });

        HashMap<Treatment.Status, Integer> statusCount = treatmentController.getStatusCountMap();
        treatmentStatusGraphButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Treatments by cancellation reason").build();
            chart.getStyler().setSeriesColors(getChartColors(statusCount.size()));
            for (Map.Entry<Treatment.Status, Integer> entry : statusCount.entrySet()) {
                chart.addSeries(String.valueOf(entry.getKey()), entry.getValue());
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
            CancellationReportFrame cancellationReportFrame = new CancellationReportFrame(treatmentController);
        });

        beauticianReport.addActionListener(e -> {
            BeauticianProfitFrame beauticianProfitFrame = new BeauticianProfitFrame(treatmentController);
        });

        logout.addActionListener(e -> {
            authController.logout();
            this.dispose();
            MainFrame mainFrame = new MainFrame();
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
