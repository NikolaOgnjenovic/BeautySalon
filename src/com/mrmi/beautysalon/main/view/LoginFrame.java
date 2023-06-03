package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private JTextField textUsername;
    private JTextField textPassword;
    private JButton buttonLogin;
    private JButton buttonBack;

    public LoginFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[grow, align right][grow, align left]", "[grow]40[grow]40[grow]10[grow]"));
        this.setTitle("Beauty salon - Login");
        this.setSize(800, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());
        this.setLocationRelativeTo(null);

        this.add(new JLabel("Login to your account"), "span, align center");
        this.add(new JLabel("Username"));
        textUsername = new JTextField(18);
        textUsername.setPreferredSize(new Dimension(250, 40));
        this.add(textUsername);

        this.add(new JLabel("Password"));
        textPassword = new JPasswordField(18);
        textPassword.setPreferredSize(new Dimension(250, 40));
        this.add(textPassword);

        buttonLogin = new JButton("Login");
        this.add(buttonLogin, "span, align center");

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span, align center");

        setVisible(true);
    }

    private void initialiseListeners() {
        buttonLogin.addActionListener(e -> {
            if (!authManager.login(textUsername.getText(), String.valueOf(textPassword.getText()))) {
                JOptionPane.showMessageDialog(null, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (authManager.getCurrentUser().getClass().equals(Client.class)) {
                ClientFrame clientFrame = new ClientFrame(salonManager, treatmentManager, userManager, authManager, authManager.getCurrentUsername(), (Client) authManager.getCurrentUser());
                clientFrame.setVisible(true);
                this.dispose();
            } else if (authManager.getCurrentUser().getClass().equals(Beautician.class)) {
                BeauticianFrame beauticianFrame = new BeauticianFrame(treatmentManager, userManager, authManager.getCurrentUsername());
                beauticianFrame.setVisible(true);
                this.dispose();
            } else if (authManager.getCurrentUser().getClass().equals(Receptionist.class)) {
                ReceptionistFrame receptionistFrame = new ReceptionistFrame(salonManager, treatmentManager, userManager, authManager);
                receptionistFrame.setVisible(true);
                this.dispose();
            } else if (authManager.getCurrentUser().getClass().equals(Manager.class)) {
                ManagerFrame managerFrame = new ManagerFrame(salonManager, treatmentManager, userManager, authManager);
                managerFrame.setVisible(true);
                this.dispose();
            }
        });

        buttonBack.addActionListener(e -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
            this.dispose();
        });
    }
}
