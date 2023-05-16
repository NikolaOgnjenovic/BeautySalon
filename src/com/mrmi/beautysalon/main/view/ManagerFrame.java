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
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private JButton buttonEditUsers;
    private JTextField textLoyaltyThreshold;
    private JButton buttonEditTreatmentTypeCategories;
    private JButton buttonEditTreatmentTypes;
    private JButton buttonEditTreatments;
    private JButton buttonTreatmentStatusGraph;
    private JButton buttonTreatmentCategoryProfit;
    private JButton buttonBeauticianStats;

    private JButton buttonCancellationReport;
    private JButton beauticianReport;
    private JButton buttonLogout;
    private JTextField textSalonName;
    private JTextField textOpeningHour;
    private JTextField textClosingHour;

    public ManagerFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow, align right][center, grow, align left]", "[center, grow]40"));
        this.setTitle("Beauty salon - Manager");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new JLabel("Loyalty threshold"));
        textLoyaltyThreshold = new JTextField(String.valueOf(salonManager.getLoyaltyThreshold()), 7);
        this.add(textLoyaltyThreshold);

        this.add(new JLabel("Salon name"));
        textSalonName = new JTextField(salonManager.getName(), 10);
        this.add(textSalonName);

        this.add(new JLabel("Opening hour"));
        textOpeningHour = new JTextField(String.valueOf(salonManager.getOpeningHour()), 2);
        this.add(textOpeningHour);

        this.add(new JLabel("Closing hour"));
        textClosingHour = new JTextField(String.valueOf(salonManager.getClosingHour()), 2);
        this.add(textClosingHour);

        buttonEditUsers = new JButton("Users");
        this.add(buttonEditUsers, "span, center");

        buttonEditTreatmentTypeCategories = new JButton("Treatment type categories");
        this.add(buttonEditTreatmentTypeCategories);

        buttonEditTreatmentTypes = new JButton("Treatment types");
        this.add(buttonEditTreatmentTypes);

        buttonEditTreatments = new JButton("Treatments");
        this.add(buttonEditTreatments);

        buttonTreatmentStatusGraph = new JButton("Treatment status chart");
        this.add(buttonTreatmentStatusGraph);

        buttonBeauticianStats = new JButton("Finished treatments chart");
        this.add(buttonBeauticianStats);

        beauticianReport = new JButton("Beautician profit report");
        this.add(beauticianReport);

        buttonTreatmentCategoryProfit = new JButton("Treatment category profit graph");
        this.add(buttonTreatmentCategoryProfit);

        buttonCancellationReport = new JButton("Cancellation report");
        this.add(buttonCancellationReport);

        // TODO:
        //  Menadžer ima uvid o prihodioma i rashodima za odredjeni vremenski period

        // TODO:
        //  Bonus se dodaje na osnovu pravila koje će zadati menadžer (npr. broju
        //  izvedenih kozmetičkih tretmana ili ostvarenom prihodu u jednoj nedelji, mesecu itd.).
        buttonLogout = new JButton("Logout");
        this.add(buttonLogout, "span, center");
    }

    private void initialiseListeners() {
        buttonEditUsers.addActionListener(e -> {
            HashMap<Integer, User> users = userManager.getUsers();
            UsersFrame usersFrame = new UsersFrame(salonManager, treatmentManager, userManager, authManager, users);
            usersFrame.setVisible(true);
        });

        textLoyaltyThreshold.addActionListener(e -> salonManager.setLoyaltyThreshold(Double.parseDouble(textLoyaltyThreshold.getText())));
        textSalonName.addActionListener(e -> salonManager.setName(textSalonName.getText()));
        textOpeningHour.addActionListener(e -> salonManager.setOpeningHour(Byte.parseByte(textOpeningHour.getText())));
        textClosingHour.addActionListener(e -> salonManager.setClosingHour(Byte.parseByte(textClosingHour.getText())));

        buttonEditTreatmentTypeCategories.addActionListener(e -> {
            TreatmentTypeCategoriesFrame treatmentTypeCategoriesFrame = new TreatmentTypeCategoriesFrame(treatmentManager, treatmentManager.getTreatmentTypeCategories());
            treatmentTypeCategoriesFrame.setVisible(true);
        });

        buttonEditTreatmentTypes.addActionListener(e -> {
            TreatmentTypesFrame treatmentTypesFrame = new TreatmentTypesFrame(treatmentManager, treatmentManager.getTreatmentTypes());
            treatmentTypesFrame.setVisible(true);
        });

        buttonEditTreatments.addActionListener(e -> {
            HashMap<Integer, Treatment> treatments = treatmentManager.getTreatments();
            TreatmentsFrame treatmentsFrame = new TreatmentsFrame(treatmentManager, userManager, treatments, true, false);
            treatmentsFrame.setVisible(true);
        });

        HashMap<Treatment.Status, Integer> statusCount = treatmentManager.getStatusCountMap();
        buttonTreatmentStatusGraph.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Treatments by cancellation reason").build();
            chart.getStyler().setSeriesColors(getChartColors(statusCount.size()));
            for (Map.Entry<Treatment.Status, Integer> entry : statusCount.entrySet()) {
                chart.addSeries(String.valueOf(entry.getKey()), entry.getValue());
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        ArrayList<Beautician> beauticians = userManager.getBeauticians();
        buttonBeauticianStats.addActionListener(e -> {
            PieChart chart = new PieChartBuilder().width(800).height(600).title("Beauticians by number of finished treatments").build();
            chart.getStyler().setSeriesColors(getChartColors(beauticians.size()));
            for (Beautician beautician : beauticians) {
                chart.addSeries("ID: " + beautician.getId() + "- " + beautician.getName(), userManager.getFinishedTreatments(beautician.getUsername()));
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        HashMap<Integer, TreatmentTypeCategory> treatmentTypeCategories = treatmentManager.getTreatmentTypeCategories();
        buttonTreatmentCategoryProfit.addActionListener(e -> {
            XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Profit").yAxisTitle("Time").build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setSeriesColors(getChartColors(treatmentTypeCategories.size()));
            chart.setXAxisTitle("Month");
            chart.setYAxisTitle("Profit");
            chart.setTitle("Profit by treatment type category per month");
            for (Map.Entry<Integer, TreatmentTypeCategory> category : treatmentTypeCategories.entrySet()) {
                chart.addSeries("ID: " + category.getValue().getId() + "- " + category.getValue().getName(), treatmentManager.getCategoryProfitByMonths(category.getKey()));
            }
            Thread t = new Thread(() -> new SwingWrapper<>(chart).displayChart().setDefaultCloseOperation(DISPOSE_ON_CLOSE));
            t.start();
        });

        buttonCancellationReport.addActionListener(e -> {
            CancellationReportFrame cancellationReportFrame = new CancellationReportFrame(treatmentManager);
            cancellationReportFrame.setVisible(true);
        });

        beauticianReport.addActionListener(e -> {
            BeauticianProfitFrame beauticianProfitFrame = new BeauticianProfitFrame(treatmentManager);
            beauticianProfitFrame.setVisible(true);
        });

        buttonLogout.addActionListener(e -> {
            authManager.logout();
            this.dispose();
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
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
