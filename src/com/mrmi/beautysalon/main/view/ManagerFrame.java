package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class ManagerFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private final AuthManager authManager;
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
    private JTextField salonNameField;
    private JTextField salonOpeningHourField;
    private JTextField salonClosingHourField;

    public ManagerFrame(TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager, AuthManager authManager) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;
        this.authManager = authManager;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow]40", "[center, grow]40"));
        this.setTitle("Beauty salon - Manager");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        

        editUsers = new JButton("Edit users");
        
        this.add(editUsers, "span");

        JLabel loyaltyThresholdLabel = new JLabel("Loyalty threshold");
        
        this.add(loyaltyThresholdLabel, "align right");

        loyaltyThreshold = new JTextField(String.valueOf(salonManager.getLoyaltyThreshold()), 7);
        
        this.add(loyaltyThreshold, "align left");

        JLabel salonNameLabel = new JLabel("Salon name");
        
        this.add(salonNameLabel, "align right");

        salonNameField = new JTextField(salonManager.getName(), 10);
        
        this.add(salonNameField, "align left");

        JLabel salonOpeningHourLabel = new JLabel("Opening hour");
        
        this.add(salonOpeningHourLabel, "align right");

        salonOpeningHourField = new JTextField(String.valueOf(salonManager.getOpeningHour()), 2);
        
        this.add(salonOpeningHourField, "align left");

        JLabel salonClosingHourLabel = new JLabel("Closing hour");
        
        this.add(salonClosingHourLabel, "align right");

        salonClosingHourField = new JTextField(String.valueOf(salonManager.getClosingHour()), 2);
        
        this.add(salonClosingHourField, "align left");

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

        beauticianReport = new JButton("Beautician profit report");
        
        this.add(beauticianReport);

        treatmentCategoryProfitButton = new JButton("View profit by treatment category graph");
        
        this.add(treatmentCategoryProfitButton);

        cancellationReport = new JButton("View cancellation report");
        
        this.add(cancellationReport);

        // TODO:
        //  salon profit & loss in a date interval

        logout = new JButton("Logout");
        
        this.add(logout, "span");
    }

    private void initialiseListeners() {
        editUsers.addActionListener(e -> {
            HashMap<String, User> users = userManager.getUsers();
            UsersFrame usersFrame = new UsersFrame(userManager, treatmentManager, authManager, salonManager, users, true, true);
        });

        loyaltyThreshold.addActionListener(e -> salonManager.setLoyaltyThreshold(Double.parseDouble(loyaltyThreshold.getText())));

        salonNameField.addActionListener(e -> salonManager.setName(salonNameField.getText()));
        salonOpeningHourField.addActionListener(e -> salonManager.setOpeningHour(Byte.parseByte(salonOpeningHourField.getText())));
        salonClosingHourField.addActionListener(e -> salonManager.setClosingHour(Byte.parseByte(salonClosingHourField.getText())));

        editTreatmentTypeCategories.addActionListener(e -> {
            TreatmentTypeCategoriesFrame treatmentTypeCategoriesFrame = new TreatmentTypeCategoriesFrame(treatmentManager, treatmentManager.getTreatmentTypeCategories(), true, true);
        });

        editTreatmentTypes.addActionListener(e -> {
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(treatmentManager, treatmentManager.getTreatmentTypes(), true, true);
        });

        editTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, treatments, true, true, true, salonManager.getLoyaltyThreshold(), false);
        });

        HashMap<Treatment.Status, Integer> statusCount = treatmentManager.getStatusCountMap();
        treatmentStatusGraphButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Treatments by cancellation reason").build();
            chart.getStyler().setSeriesColors(getChartColors(statusCount.size()));
            for (Map.Entry<Treatment.Status, Integer> entry : statusCount.entrySet()) {
                chart.addSeries(String.valueOf(entry.getKey()), entry.getValue());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        Collection<Beautician> beauticians = userManager.getBeauticians().values();
        beauticianStatsButton.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Beauticians by number of finished treatments").build();
            chart.getStyler().setSeriesColors(getChartColors(beauticians.size()));
            for (Beautician b : beauticians) {
                chart.addSeries(b.getName(), b.getFinishedTreatments());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories = treatmentManager.getTreatmentTypeCategories();
        treatmentCategoryProfitButton.addActionListener(e -> {
            XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Profit").yAxisTitle("Time").build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setSeriesColors(getChartColors(treatmentTypeCategories.size()));
            chart.setXAxisTitle("Month");
            chart.setYAxisTitle("Profit");
            chart.setTitle("Profit by treatment type category per month");
            for (Map.Entry<Integer, TreatmentTypeCategory> category : treatmentTypeCategories.entrySet()) {
                chart.addSeries(category.getValue().getName(), treatmentManager.getCategoryProfitByMonths(category.getKey()));
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        cancellationReport.addActionListener(e -> {
            CancellationReportFrame cancellationReportFrame = new CancellationReportFrame(treatmentManager);
        });

        beauticianReport.addActionListener(e -> {
            BeauticianProfitFrame beauticianProfitFrame = new BeauticianProfitFrame(treatmentManager);
        });

        logout.addActionListener(e -> {
            authManager.logout();
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
