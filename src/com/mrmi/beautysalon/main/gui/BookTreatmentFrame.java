package com.mrmi.beautysalon.main.gui;

import com.mrmi.beautysalon.main.objects.*;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;

public class BookTreatmentFrame extends JFrame {
    private final Database database;
    private byte treatmentTypeId;
    private double treatmentPrice;
    private String beauticianUsername;
    BeauticianTableModel beauticianTableModel;
    private HashMap<String, Beautician> beauticians;
    public BookTreatmentFrame(Database database, String clientUsername) {
        this.database = database;
        this.setTitle("Book treatment");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(800, 800);
        this.setVisible(true);
        this.getContentPane().setBackground(new Color(235, 235, 235));
        this.setLayout(new FlowLayout());

        TreatmentTypeTableModel treatmentTableModel = new TreatmentTypeTableModel(database, database.getTreatmentTypes(), false);
        JTable treatmentTypeTable = new JTable(treatmentTableModel);
        this.add(new JScrollPane(treatmentTypeTable));
        treatmentTypeTable.setAutoCreateRowSorter(true);

        TableRowSorter<TableModel> treatmentTableSorter = new TableRowSorter<>(treatmentTypeTable.getModel());
        treatmentTypeTable.setRowSorter(treatmentTableSorter);
        JTextField filterText = new JTextField("Search", 20);
        filterText.addActionListener(e -> filter(filterText.getText(), treatmentTableSorter));
        this.add(filterText);

        treatmentTypeTable.getSelectionModel().addListSelectionListener(e -> {
            if (treatmentTypeTable.getSelectedRow() != -1) {
                treatmentTypeId = Byte.parseByte(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 6).toString());
                treatmentPrice = Double.parseDouble(treatmentTypeTable.getValueAt(treatmentTypeTable.getSelectedRow(), 2).toString());
                displayAvailableBeauticians(treatmentTypeId);
            }
        });

        Date selectedDate = new Date();

        beauticians = database.getBeauticians();
        beauticianTableModel = new BeauticianTableModel(beauticians);
        JTable beauticianTable = new JTable(beauticianTableModel);
        this.add(new JScrollPane(beauticianTable));
        beauticianTable.setAutoCreateRowSorter(true);

        TableRowSorter<TableModel> beauticianTableSorter = new TableRowSorter<>(beauticianTable.getModel());
        beauticianTable.setRowSorter(beauticianTableSorter);
        JTextField beauticianFilterText = new JTextField("Search", 20);
        beauticianFilterText.addActionListener(e -> filter(beauticianFilterText.getText(), beauticianTableSorter));
        this.add(beauticianFilterText);

        beauticianTable.getSelectionModel().addListSelectionListener(e -> {
            beauticianUsername = beauticianTable.getValueAt(beauticianTable.getSelectedRow(), 0).toString();
        });
        /*
        TODO: date & time picker
        Zatim korisnik bira termin – datum i vreme (od dostupnih termina kada je dostupan
        odabrani kozmetičar, u toku radnog vremena kozmetičkog salona).
        Zbog pojednostavljivanja, smatrati da tretmani počinju uvek na pun sat.

            System.out.println("Enter date in dd.MM.yyyy format");
            Date scheduledDate;
            try {
                scheduledDate = sdf.parse(scanner.nextLine());
            } catch (ParseException e) {
                System.out.println("Invalid date");
                return null;
            }

            return new Treatment(scheduledDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentType.getPrice());
        }
        */
        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e -> database.bookTreatment(new Treatment(selectedDate, false, clientUsername, beauticianUsername, treatmentTypeId, treatmentPrice), database.getNextTreatmentId()));
        this.add(bookButton);
    }

    private void displayAvailableBeauticians(byte treatmentTypeId) {
        beauticians = database.getBeauticiansByTreatmentType(treatmentTypeId);
        if (beauticians.size() < 1) {
            return;
        }
        beauticianTableModel.fireTableDataChanged();
    }

    private void filter(String text, TableRowSorter<TableModel> tableSorter) {
        RowFilter<TableModel, Object> rf;
        //If current expression doesn't parse, don't update.
        try {
            rf = RowFilter.regexFilter(text);
        } catch (java.util.regex.PatternSyntaxException e) {
            return;
        }
        tableSorter.setRowFilter(rf);
    }
}
