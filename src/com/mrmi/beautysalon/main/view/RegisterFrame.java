package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final SalonManager salonManager;
    private final AuthManager authManager;
    private JButton registerButton;
    private JButton backButton;
    private JTextField usernameField, passwordField, nameField, surnameField, genderField, phoneField, addressField;
    private final boolean canPickUserType;
    private JComboBox<String> userTypeComboBox;
    private JLabel bonusLabel, salaryLabel, qualificationLabel, experienceLabel;
    private JTextField bonusField, salaryField, qualificationField, experienceField;
    public RegisterFrame(TreatmentManager treatmentManager, UserManager userManager, SalonManager salonManager, AuthManager authManager, boolean canPickUserType) {
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.salonManager = salonManager;
        this.authManager = authManager;
        this.canPickUserType = canPickUserType;

        initialiseViews();
        initialiseListeners();
    }
    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow, align right][center, grow, align left]", "[center, grow]"));
        this.setTitle("Beauty salon - Registration");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        JLabel usernameLabel = new JLabel("Username");
        this.add(usernameLabel);

        usernameField = new JTextField(20);
        this.add(usernameField);

        JLabel passwordLabel = new JLabel("Password");
        this.add(passwordLabel);

        passwordField = new JTextField(20);
        this.add(passwordField);

        JLabel nameLabel = new JLabel("Name");
        this.add(nameLabel);

        nameField = new JTextField(20);
        this.add(nameField);

        JLabel surnameLabel = new JLabel("Surname");
        this.add(surnameLabel);

        surnameField = new JTextField(20);
        this.add(surnameField);

        JLabel genderLabel = new JLabel("Gender");
        this.add(genderLabel);

        genderField = new JTextField(20);
        this.add(genderField);

        JLabel phoneLabel = new JLabel("Phone number");
        this.add(phoneLabel);

        phoneField = new JTextField(20);
        this.add(phoneField);

        JLabel addressLabel = new JLabel("Address");
        this.add(addressLabel);

        addressField = new JTextField(20);
        this.add(addressField);

        if (canPickUserType) {
            userTypeComboBox = new JComboBox<>();
            userTypeComboBox.addItem("Client");
            userTypeComboBox.addItem("Beautician");
            userTypeComboBox.addItem("Receptionist");
            userTypeComboBox.addItem("Manager");
            
            this.add(userTypeComboBox, "span");

            experienceLabel = new JLabel("Years of experience");
            
            this.add(experienceLabel);
            experienceLabel.setVisible(false);

            experienceField = new JTextField(20);
            
            this.add(experienceField);
            experienceField.setVisible(false);

            qualificationLabel = new JLabel("Qualification level");
            
            this.add(qualificationLabel);
            qualificationLabel.setVisible(false);

            qualificationField = new JTextField(20);
            
            this.add(qualificationField);
            qualificationField.setVisible(false);

            salaryLabel = new JLabel("Salary");
            
            this.add(salaryLabel);
            salaryLabel.setVisible(false);

            salaryField = new JTextField(20);
            
            this.add(salaryField);
            salaryField.setVisible(false);

            bonusLabel = new JLabel("Salary bonus");
            
            this.add(bonusLabel);
            bonusLabel.setVisible(false);

            bonusField = new JTextField(20);
            
            this.add(bonusField);
            bonusField.setVisible(false);
        }

        registerButton = new JButton("Register");
        this.add(registerButton, "span, center");

        backButton = new JButton("Back");
        this.add(backButton, "span, center");
    }

    private void initialiseListeners() {
        if (canPickUserType) {
            userTypeComboBox.addActionListener(e -> {
                if (userTypeComboBox.getSelectedIndex() > 0) {
                    experienceLabel.setVisible(true);
                    experienceField.setVisible(true);
                    qualificationLabel.setVisible(true);
                    qualificationField.setVisible(true);
                    salaryLabel.setVisible(true);
                    salaryField.setVisible(true);
                    bonusLabel.setVisible(true);
                    bonusField.setVisible(true);
                } else {
                    experienceLabel.setVisible(false);
                    experienceField.setVisible(false);
                    qualificationLabel.setVisible(false);
                    qualificationField.setVisible(false);
                    salaryLabel.setVisible(false);
                    salaryField.setVisible(false);
                    bonusLabel.setVisible(false);
                    bonusField.setVisible(false);
                }
            });
        }
        registerButton.addActionListener(e -> {
            if (!canPickUserType) {
                Client client = new Client(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText());
                ClientFrame clientFrame = new ClientFrame(client, usernameField.getText(), treatmentManager, userManager, salonManager, authManager);
            }
            switch (userTypeComboBox.getSelectedIndex()) {
                case 0:
                    Client client = new Client(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText());
                    ClientFrame clientFrame = new ClientFrame(client, usernameField.getText(), treatmentManager, userManager, salonManager, authManager);
                    break;
                case 1:
                    Beautician beautician = new Beautician(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userManager.addUser(usernameField.getText(), beautician);
                    this.dispose();
                    break;
                case 2:
                    Receptionist receptionist = new Receptionist(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userManager.addUser(usernameField.getText(), receptionist);
                    this.dispose();
                    break;
                case 3:
                    Manager manager = new Manager(passwordField.getText(), nameField.getText(), surnameField.getText(), genderField.getText(), phoneField.getText(), addressField.getText(), Byte.parseByte(qualificationField.getText()), Byte.parseByte(experienceField.getText()), Float.parseFloat(bonusField.getText()), Float.parseFloat(salaryField.getText()));
                    userManager.addUser(usernameField.getText(), manager);
                    this.dispose();
                    break;
            }
        });
        backButton.addActionListener(e -> this.dispose());
    }
}
