package model;

import dao.ClientDao;
import dao.PlataDao;
import dao.ProprietateDao;
import dao.RezervareDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookingGUI extends JFrame {

    private final BookingManager manager;
    private DefaultTableModel tableModel;
    private JTable table;

    private final ClientDao clientDao;
    private final ProprietateDao proprietateDao;
    private final RezervareDao rezervareDao;
    private final PlataDao plataDao;


    private JCheckBox clientVreaParcare;
    private JCheckBox clientVreaMicDejun;


    private JTextField txtNume, txtAdresa, txtPretNoapte, txtNrCamere, txtTaxaExtra;
    private JCheckBox chkBucatarie, chkParcare, chkMicDejun, chkPiscina;
    private JTextField txtPretParcare, txtPretMicDejun;
    private JComboBox<String> comboTip;

    private JTextField txtCheckIn, txtCheckOut;

    public BookingGUI(BookingManager manager,
                      ClientDao clientDao,
                      ProprietateDao proprietateDao,
                      RezervareDao rezervareDao,
                      PlataDao plataDao) {

        this.manager = manager;
        this.clientDao = clientDao;
        this.proprietateDao = proprietateDao;
        this.rezervareDao = rezervareDao;
        this.plataDao = plataDao;

        setTitle("Sistem Rezervări Cazare - Proiect POO");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Vizualizare & Rezervare Client", createViewPanel());
        tabs.addTab("Panou Administrare (Adăugare)", createAddPanel());

        add(tabs);
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout());


        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createTitledBorder("Căutare Disponibilitate"));

        txtCheckIn = new JTextField("2026-06-01", 10);
        txtCheckOut = new JTextField("2026-06-05", 10);
        JButton btnCauta = new JButton("Filtrează Disponibile");

        searchPanel.add(new JLabel("Check-in :"));
        searchPanel.add(txtCheckIn);
        searchPanel.add(new JLabel("Check-out :"));
        searchPanel.add(txtCheckOut);
        searchPanel.add(btnCauta);

        panel.add(searchPanel, BorderLayout.NORTH);


        String[] coloane = {"ID", "Tip", "Nume", "Adresa", "Camere", "Bucătărie", "Preț/Noapte", "Piscină", "Rating"};
        tableModel = new DefaultTableModel(coloane, 0);
        table = new JTable(tableModel);

        panel.add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel clientPanel = new JPanel(new FlowLayout());
        clientPanel.setBorder(BorderFactory.createTitledBorder("Opțiuni Rezervare"));


        clientVreaParcare = new JCheckBox("Vreau Parcare");
        clientVreaMicDejun = new JCheckBox("Vreau Mic Dejun");


        clientVreaParcare.setEnabled(false);
        clientVreaMicDejun.setEnabled(false);

        JButton btnRezerva = new JButton("Calculează Preț & Rezervă");
        btnRezerva.setBackground(new Color(39, 174, 96));
        btnRezerva.setForeground(Color.WHITE);
        btnRezerva.setFont(new Font("Arial", Font.BOLD, 13));

        clientPanel.add(clientVreaParcare);
        clientPanel.add(clientVreaMicDejun);
        clientPanel.add(btnRezerva);

        panel.add(clientPanel, BorderLayout.SOUTH);


        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            int row = table.getSelectedRow();
            if (row == -1) return;

            int idProp = (int) tableModel.getValueAt(row, 0);
            Proprietate pSel = manager.gasesteDupaId(idProp);
            if (pSel == null) return;

            boolean hasParking = pSel.isAreParcare();
            clientVreaParcare.setEnabled(hasParking);
            if (!hasParking) clientVreaParcare.setSelected(false);

            boolean hasBreakfast = pSel.isAreMicDejun();
            clientVreaMicDejun.setEnabled(hasBreakfast);
            if (!hasBreakfast) clientVreaMicDejun.setSelected(false);
        });


        btnCauta.addActionListener(e -> {
            try {
                String start = txtCheckIn.getText();
                String end = txtCheckOut.getText();

                tableModel.setRowCount(0);

                for (Proprietate p : manager.getInventar()) {
                    if (p.verificaDisponibilitate(start, end)) {
                        String infoPiscina = "Nu";
                        if (p instanceof Vila && ((Vila) p).isArePiscina()) infoPiscina = "Da";
                        else if (p instanceof Cabana && ((Cabana) p).isArePiscina()) infoPiscina = "Da";

                        String infoBuc = p.isAreBucatarie() ? "Da" : "Nu";

                        tableModel.addRow(new Object[]{
                                p.getId(),
                                p.getClass().getSimpleName(),
                                p.getNume(),
                                p.getAdresa(),
                                p.getNrCamere(),
                                infoBuc,
                                p.getPretNoapte(),
                                infoPiscina,
                                p.getRating()
                        });
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage());
            }
        });


        btnRezerva.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Selectați o proprietate din tabel!");
                return;
            }

            try {
                LocalDate d1 = LocalDate.parse(txtCheckIn.getText());
                LocalDate d2 = LocalDate.parse(txtCheckOut.getText());
                int nopti = (int) ChronoUnit.DAYS.between(d1, d2);

                if (nopti <= 0) {
                    JOptionPane.showMessageDialog(this, "Perioadă invalidă (minim o noapte)!");
                    return;
                }

                int idProp = (int) tableModel.getValueAt(selectedRow, 0);
                Proprietate pSel = manager.gasesteDupaId(idProp);
                if (pSel == null) return;

                double pretFinal = pSel.calculeazaPretTotal(
                        nopti,
                        clientVreaParcare.isSelected(),
                        clientVreaMicDejun.isSelected()
                );

                int confirm = JOptionPane.showConfirmDialog(this,
                        "Proprietate: " + pSel.getNume() + "\n" +
                                "Durata: " + nopti + " nopți\n" +
                                "Preț Total: " + pretFinal + " RON\n\nConfirmați?",
                        "Confirmare Plată",
                        JOptionPane.YES_NO_OPTION);

                if (confirm != JOptionPane.YES_OPTION) return;

                Client client = afiseazaDialogClient();
                if (client == null) return;

                // insert rezervare în DB
                int idRez = rezervareDao.insert(
                        pSel.getId(),
                        client.getId(),
                        d1,
                        d2,
                        pretFinal
                );


                plataDao.insert(idRez, pretFinal, LocalDate.now(), "Transfer bancar");

                Rezervare r = new Rezervare(client, pSel, d1, d2, pretFinal);
                pSel.adaugaRezervare(r);

                JOptionPane.showMessageDialog(this, "Rezervare finalizată!");
                btnCauta.doClick();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel createAddPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        txtNume = new JTextField();
        txtAdresa = new JTextField();
        txtPretNoapte = new JTextField();
        txtNrCamere = new JTextField();

        chkBucatarie = new JCheckBox("Are bucătărie utilată?");

        chkPiscina = new JCheckBox("Include Piscină?");
        txtTaxaExtra = new JTextField("0");
        txtTaxaExtra.setEnabled(false);
        chkPiscina.addActionListener(e -> txtTaxaExtra.setEnabled(chkPiscina.isSelected()));

        comboTip = new JComboBox<>(new String[]{"Apartament", "Vila", "Cabana"});
        comboTip.addActionListener(e -> {
            String tip = (String) comboTip.getSelectedItem();
            chkPiscina.setEnabled(!"Apartament".equals(tip));
            if ("Apartament".equals(tip)) chkPiscina.setSelected(false);
        });

        chkParcare = new JCheckBox("Oferă Parcare?");
        txtPretParcare = new JTextField("0");
        txtPretParcare.setEnabled(false);
        chkParcare.addActionListener(e -> txtPretParcare.setEnabled(chkParcare.isSelected()));

        chkMicDejun = new JCheckBox("Oferă Mic Dejun?");
        txtPretMicDejun = new JTextField("0");
        txtPretMicDejun.setEnabled(false);
        chkMicDejun.addActionListener(e -> txtPretMicDejun.setEnabled(chkMicDejun.isSelected()));

        JButton btnSalveaza = new JButton("Adaugă Proprietate");
        btnSalveaza.addActionListener(e -> {
            try {
                // id local (nu contează pt DB; DB returnează id real)
                int idLocal = manager.getInventar().size() + 1;

                String nume = txtNume.getText().trim();
                String adresa = txtAdresa.getText().trim();

                if (nume.isEmpty() || adresa.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Numele și adresa sunt obligatorii!");
                    return;
                }

                double pretN = Double.parseDouble(txtPretNoapte.getText().trim());
                int nrC = Integer.parseInt(txtNrCamere.getText().trim());

                boolean areB = chkBucatarie.isSelected();
                boolean arePisc = chkPiscina.isSelected();
                double extra = Double.parseDouble(txtTaxaExtra.getText().trim());

                boolean pExist = chkParcare.isSelected();
                double pretP = Double.parseDouble(txtPretParcare.getText().trim());

                boolean mdExist = chkMicDejun.isSelected();
                double pretMD = Double.parseDouble(txtPretMicDejun.getText().trim());

                String tip = (String) comboTip.getSelectedItem();

                Proprietate proprietateNoua;
                if ("Vila".equals(tip)) {
                    proprietateNoua = new Vila(idLocal, nume, adresa, pretN, 5.0,
                            pExist, mdExist, pretMD, pretP, nrC, areB, arePisc, extra);
                } else if ("Apartament".equals(tip)) {
                    proprietateNoua = new Apartament(idLocal, nume, adresa, pretN, 5.0,
                            pExist, mdExist, pretMD, pretP, nrC, areB, extra);
                } else { // Cabana
                    proprietateNoua = new Cabana(idLocal, nume, adresa, pretN, 5.0,
                            pExist, mdExist, pretMD, pretP, nrC, areB, arePisc, extra);
                }

                try {
                    int idDb = proprietateDao.insert(proprietateNoua);
                    proprietateNoua.setId(idDb);
                    manager.adauga(proprietateNoua);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Eroare DB: " + ex.getMessage());
                    return;
                }

                JOptionPane.showMessageDialog(this, "Proprietatea a fost adăugată cu succes!");
                updateTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valori numerice invalide!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Eroare: " + ex.getMessage());
            }
        });

        panel.add(new JLabel("Nume:"));
        panel.add(txtNume);

        panel.add(new JLabel("Adresă:"));
        panel.add(txtAdresa);

        panel.add(new JLabel("Tip:"));
        panel.add(comboTip);

        panel.add(new JLabel("Preț/Noapte:"));
        panel.add(txtPretNoapte);

        panel.add(new JLabel("Nr. Camere:"));
        panel.add(txtNrCamere);

        panel.add(chkPiscina);
        panel.add(new JLabel("Taxă Piscină/Mentenanță:"));
        panel.add(txtTaxaExtra);

        panel.add(chkBucatarie);
        panel.add(new JLabel(""));

        panel.add(chkParcare);
        panel.add(txtPretParcare);

        panel.add(chkMicDejun);
        panel.add(txtPretMicDejun);

        panel.add(new JLabel(""));
        panel.add(btnSalveaza);

        return panel;
    }

    private Client afiseazaDialogClient() {
        JTextField txtNume = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtTelefon = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nume complet:"));
        panel.add(txtNume);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Telefon:"));
        panel.add(txtTelefon);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Date Client",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) return null;

        String nume = txtNume.getText().trim();
        String email = txtEmail.getText().trim();
        String telefon = txtTelefon.getText().trim();

        if (nume.isEmpty() || email.isEmpty() || telefon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completează nume, email și telefon!");
            return null;
        }

        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Email invalid!");
            return null;
        }

        try {
            Client temp = new Client(0, nume, email, telefon);
            int idDb = clientDao.insert(temp);
            return new Client(idDb, nume, email, telefon);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Eroare DB client: " + ex.getMessage());
            return null;
        }
    }

    public void updateTable() {
        tableModel.setRowCount(0);

        for (Proprietate p : manager.getInventar()) {
            String infoPiscina = "Nu";
            if (p instanceof Vila && ((Vila) p).isArePiscina()) infoPiscina = "Da";
            else if (p instanceof Cabana && ((Cabana) p).isArePiscina()) infoPiscina = "Da";

            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getClass().getSimpleName(),
                    p.getNume(),
                    p.getAdresa(),
                    p.getNrCamere(),
                    (p.isAreBucatarie() ? "Da" : "Nu"),
                    p.getPretNoapte(),
                    infoPiscina,
                    p.getRating()
            });
        }
    }
}
