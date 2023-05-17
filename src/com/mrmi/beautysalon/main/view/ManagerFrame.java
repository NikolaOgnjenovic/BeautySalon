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
    private JButton buttonEditSalon;
    private JButton buttonEditTreatmentTypeCategories;
    private JButton buttonEditTreatmentTypes;
    private JButton buttonEditTreatments;
    private JButton buttonTreatmentStatusGraph;
    private JButton buttonTreatmentCategoryProfit;
    private JButton buttonBeauticianStats;

    private JButton buttonCancellationReport;
    private JButton beauticianReport;
    private JButton buttonLogout;
    private JButton buttonRevenueExpense;

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
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());

        buttonEditSalon = new JButton("Edit salon properties");
        add(buttonEditSalon);

        buttonEditUsers = new JButton("Users");
        this.add(buttonEditUsers);

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

        buttonRevenueExpense = new JButton("Salon revenue & expenses");
        this.add(buttonRevenueExpense, "span, center");

        buttonLogout = new JButton("Logout");
        this.add(buttonLogout, "span, center");
    }

    private void initialiseListeners() {
        buttonEditSalon.addActionListener(e -> {
            SalonFrame salonFrame = new SalonFrame(salonManager);
            salonFrame.setVisible(true);
        });
        buttonEditUsers.addActionListener(e -> {
            HashMap<Integer, User> users = userManager.getUsers();
            UsersFrame usersFrame = new UsersFrame(salonManager, treatmentManager, userManager, authManager, users);
            usersFrame.setVisible(true);
        });

        buttonEditTreatmentTypeCategories.addActionListener(e -> {
            TreatmentTypeCategoriesFrame typeCategoriesFrame = new TreatmentTypeCategoriesFrame(treatmentManager, treatmentManager.getTreatmentTypeCategories());
            typeCategoriesFrame.setVisible(true);
        });

        buttonEditTreatmentTypes.addActionListener(e -> {
            TreatmentTypesFrame typesFrame = new TreatmentTypesFrame(treatmentManager, treatmentManager.getTreatmentTypes());
            typesFrame.setVisible(true);
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

        HashMap<Integer, TreatmentTypeCategory> categories = treatmentManager.getTreatmentTypeCategories();
        buttonTreatmentCategoryProfit.addActionListener(e -> {
            XYChart chart = new XYChartBuilder().width(800).height(600).xAxisTitle("Profit").yAxisTitle("Time").build();
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
            chart.getStyler().setSeriesColors(getChartColors(categories.size()));
            chart.setXAxisTitle("Month");
            chart.setYAxisTitle("Profit");
            chart.setTitle("Profit by treatment type category per month");
            for (Map.Entry<Integer, TreatmentTypeCategory> category : categories.entrySet()) {
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

        buttonRevenueExpense.addActionListener(e -> {
            RevenueExpenseFrame revenueExpenseFrame = new RevenueExpenseFrame(treatmentManager, userManager);
            revenueExpenseFrame.setVisible(true);
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
