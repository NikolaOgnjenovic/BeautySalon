package com.mrmi.beautysalon.main.view;

import com.mrmi.beautysalon.main.manager.AuthManager;
import com.mrmi.beautysalon.main.manager.SalonManager;
import com.mrmi.beautysalon.main.manager.TreatmentManager;
import com.mrmi.beautysalon.main.manager.UserManager;
import com.mrmi.beautysalon.main.entity.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RegisterFrame extends JFrame {
    private final SalonManager salonManager;
    private final TreatmentManager treatmentManager;
    private final UserManager userManager;
    private final AuthManager authManager;
    private final boolean canPickUserType;
    private final boolean isClient;
    private final User user;
    private JButton buttonRegister;
    private JButton buttonBack;
    private JTextField textUsername, textPassword, textName, textSurname, textGender, textPhone, textAddress, textSalary, textQualificationLevel, textExperience;

    private JComboBox<String> comboBoxUserType;
    private JLabel labelSalary, labelQualificationLevel, labelExperience;

    public RegisterFrame(SalonManager salonManager, TreatmentManager treatmentManager, UserManager userManager, AuthManager authManager, boolean canPickUserType, User user, boolean isClient) {
        this.salonManager = salonManager;
        this.treatmentManager = treatmentManager;
        this.userManager = userManager;
        this.authManager = authManager;
        this.canPickUserType = canPickUserType;
        this.isClient = isClient;
        this.user = user;

        initialiseViews();
        initialiseListeners();
    }

    private void initialiseViews() {
        this.setLayout(new MigLayout("wrap 2", "[center, grow, align right][center, grow, align left]", "[center, grow]"));
        this.setTitle("Beauty salon - Registration");
        this.setSize(1000, 1080);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/images/icon.png").getImage());

        this.add(new JLabel("Username"));
        textUsername = new JTextField(20);
        this.add(textUsername);

        this.add(new JLabel("Password"));
        textPassword = new JTextField(20);
        this.add(textPassword);

        this.add(new JLabel("Name"));
        textName = new JTextField(20);
        this.add(textName);

        this.add(new JLabel("Surname"));
        textSurname = new JTextField(20);
        this.add(textSurname);

        this.add(new JLabel("Gender"));
        textGender = new JTextField(20);
        this.add(textGender);

        this.add(new JLabel("Phone number"));

        textPhone = new JTextField(20);
        this.add(textPhone);

        this.add(new JLabel("Address"));
        textAddress = new JTextField(20);
        this.add(textAddress);

        if (canPickUserType) {
            comboBoxUserType = new JComboBox<>();
            comboBoxUserType.addItem("Client");
            comboBoxUserType.addItem("Beautician");
            comboBoxUserType.addItem("Receptionist");
            comboBoxUserType.addItem("Manager");

            this.add(comboBoxUserType, "span, center");

            labelExperience = new JLabel("Years of experience");
            this.add(labelExperience);

            textExperience = new JTextField(20);
            this.add(textExperience);

            labelQualificationLevel = new JLabel("Qualification level");
            this.add(labelQualificationLevel);

            textQualificationLevel = new JTextField(20);
            this.add(textQualificationLevel);

            labelSalary = new JLabel("Salary");
            this.add(labelSalary);

            textSalary = new JTextField(20);
            this.add(textSalary);

            toggleEmployeeFields(false);
            if (user != null && !(user instanceof Client)) {
                toggleEmployeeFields(true);

                if (user instanceof Beautician) {
                    comboBoxUserType.setSelectedIndex(1);
                } else if (user instanceof Receptionist) {
                    comboBoxUserType.setSelectedIndex(2);
                } else {
                    comboBoxUserType.setSelectedIndex(3);
                }
            }
        }

        if (user != null) {
            textUsername.setText(user.getUsername());
            textPassword.setText(user.getPassword());
            textName.setText(user.getName());
            textSurname.setText(user.getSurname());
            textGender.setText(user.getGender());
            textPhone.setText(user.getPhoneNumber());
            textAddress.setText(user.getAddress());

            if (user instanceof Employee) {
                textQualificationLevel.setText(String.valueOf(((Employee) user).getQualificationLevel()));
                textExperience.setText(String.valueOf(((Employee) user).getYearsOfExperience()));
                textSalary.setText(String.valueOf(((Employee) user).getMonthlySalary()));
            }
        }

        buttonRegister = new JButton((user == null) ? "Register" : "Save");
        this.add(buttonRegister, "span, center");

        buttonBack = new JButton("Back");
        this.add(buttonBack, "span, center");
    }

    private void initialiseListeners() {
        if (canPickUserType) {
            comboBoxUserType.addActionListener(e -> toggleEmployeeFields(comboBoxUserType.getSelectedIndex() > 0));
        }

        buttonRegister.addActionListener(e -> {
            String username, password, name, surname, gender, phone, address;
            username = textUsername.getText();
            if (username.length() == 0) {
                JOptionPane.showMessageDialog(null, "The username cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            password = textPassword.getText();
            if (password.length() == 0) {
                JOptionPane.showMessageDialog(null, "The password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            name = textName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "The name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            surname = textSurname.getText();
            if (surname.length() == 0) {
                JOptionPane.showMessageDialog(null, "The surname cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            gender = textGender.getText();
            if (gender.length() == 0) {
                JOptionPane.showMessageDialog(null, "The gender cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phone = textPhone.getText();
            if (phone.length() == 0) {
                JOptionPane.showMessageDialog(null, "The phone number cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            address = textAddress.getText();
            if (address.length() == 0) {
                JOptionPane.showMessageDialog(null, "The address number cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!canPickUserType) {
                Client client = new Client(username, password, name, surname, gender, phone, address);
                userManager.addUser(client);
                this.dispose();

                if (isClient) {
                    ClientFrame clientFrame = new ClientFrame(salonManager, treatmentManager, userManager, authManager, textUsername.getText(), client);
                    clientFrame.setVisible(true);
                }
                return;
            }
            User newUser = user;

            if (!textQualificationLevel.getText().matches("[1-8]")) {
                JOptionPane.showMessageDialog(null, "The qualification level has to be a number between 1 and 8", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte qualificationLevel = Byte.parseByte(textQualificationLevel.getText());

            if (!textExperience.getText().matches("[0-9]+")) {
                JOptionPane.showMessageDialog(null, "The years of experience have to be a number >= 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            byte experience = Byte.parseByte(textExperience.getText());

            if (!textSalary.getText().matches("[1-9][0-9]?")) {
                JOptionPane.showMessageDialog(null, "The salary has to be a positive number", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            float salary = Float.parseFloat(textSalary.getText());

            switch (comboBoxUserType.getSelectedIndex()) {
                case 0:
                    newUser = new Client(username, password, name, surname, gender, phone, address);
                    break;
                case 1:
                    newUser = new Beautician(username, password, name, surname, gender, phone, address, qualificationLevel, experience, salary);
                    break;
                case 2:
                    newUser = new Receptionist(username, password, name, surname, gender, phone, address, qualificationLevel, experience, salary);
                    break;
                case 3:
                    newUser = new Manager(username, password, name, surname, gender, phone, address, qualificationLevel, experience, salary);
                    break;
            }

            if (user == null) {
                userManager.addUser(newUser);
            } else {
                newUser.setId(user.getId());
                userManager.updateUser(newUser);
            }

            this.dispose();
        });

        buttonBack.addActionListener(e -> this.dispose());
    }

    private void toggleEmployeeFields(boolean value) {
        labelExperience.setVisible(value);
        textExperience.setVisible(value);
        labelQualificationLevel.setVisible(value);
        textQualificationLevel.setVisible(value);
        labelSalary.setVisible(value);
        textSalary.setVisible(value);
    }
}