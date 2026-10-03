import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public final class EventRegistrationGUI extends JFrame {
    private static final long serialVersionUID = 1L;

    // GUI Components
    private static final Color BACKGROUND = new Color(246, 249, 253);
    private static final Color WHITE = Color.WHITE;
    private static final Color PRIMARY = new Color(37, 99, 190);
    private static final Color PRIMARY_DARK = new Color(28, 78, 153);
    private static final Color ACCENT = new Color(231, 241, 255);
    private static final Color TEXT = new Color(34, 48, 68);
    private static final Color MUTED = new Color(105, 121, 143);
    private static final Color BORDER = new Color(226, 233, 242);
    private static final String[] TICKET_CATEGORIES = {
            "Regular", "VIP", "Student", "Premium"
    };
    private static final String[] TABLE_COLUMNS = {
            "Participant ID", "Name", "Email", "Ticket Type", "Quantity",
            "Ticket Price", "Total Amount", "Discount", "Final Amount"
    };

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private final JLabel pageSubtitle = new JLabel("Welcome to your event registration workspace.");

    private JTextField nameField;
    private JTextField idField;
    private JTextField emailField;
    private JComboBox<String> ticketCategory;
    private JSpinner quantitySpinner;
    private JTextField searchIdField;
    private JPanel feePreview;
    private JPanel searchResultPanel;
    private JTable participantsTable;
    private JTable sortedTable;
    private DefaultTableModel participantsModel;
    private DefaultTableModel sortedModel;
    private JPanel summaryDetails;
    private final String[] latestRegistration = new String[TABLE_COLUMNS.length];

    public EventRegistrationGUI() {
        super("Event Registration & Ticket Management System");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(1050, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BACKGROUND);
        setLayout(new BorderLayout());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                confirmExit();
            }
        });

        add(createSidebar(), BorderLayout.WEST);
        add(createMainArea(), BorderLayout.CENTER);
        refreshParticipantsTable();
        refreshDashboard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception exception) {
                System.err.println("Could not apply the system look and feel: "
                        + exception.getMessage());
            }
            new EventRegistrationGUI().setVisible(true);
        });
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(WHITE);
        sidebar.setPreferredSize(new Dimension(238, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));
        sidebar.setLayout(new BorderLayout());

        JPanel brand = new JPanel(new BorderLayout(0, 5));
        brand.setBackground(WHITE);
        brand.setBorder(BorderFactory.createEmptyBorder(26, 22, 24, 18));
        JLabel brandTitle = new JLabel("EVENT DESK");
        brandTitle.setFont(new Font("SansSerif", Font.BOLD, 17));
        brandTitle.setForeground(PRIMARY);
        JLabel brandSubtitle = new JLabel("Registration manager");
        brandSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        brandSubtitle.setForeground(MUTED);
        brand.add(brandTitle, BorderLayout.NORTH);
        brand.add(brandSubtitle, BorderLayout.CENTER);
        sidebar.add(brand, BorderLayout.NORTH);

        JPanel navigation = new JPanel();
        navigation.setBackground(WHITE);
        navigation.setBorder(BorderFactory.createEmptyBorder(4, 12, 12, 12));
        navigation.setLayout(new javax.swing.BoxLayout(navigation, javax.swing.BoxLayout.Y_AXIS));
        addNavigationButton(navigation, "Dashboard", "dashboard");
        addNavigationButton(navigation, "Register Participant", "register");
        addNavigationButton(navigation, "Registration Summary", "summary");
        addNavigationButton(navigation, "All Participants", "participants");
        addNavigationButton(navigation, "Search Participant", "search");
        addNavigationButton(navigation, "Sort Participants", "sort");

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 12, 18, 12));
        JButton exitButton = navigationButton("Exit");
        exitButton.addActionListener(event -> confirmExit());
        bottom.add(exitButton, BorderLayout.SOUTH);

        sidebar.add(navigation, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);
        return sidebar;
    }

    private void addNavigationButton(JPanel panel, String label, String cardName) {
        JButton button = navigationButton(label);
        button.addActionListener(event -> {
            cardLayout.show(contentPanel, cardName);
            pageSubtitle.setText(subtitleFor(cardName));
            if ("dashboard".equals(cardName)) {
                refreshDashboard();
            } else if ("summary".equals(cardName)) {
                refreshSummary();
            } else if ("participants".equals(cardName)) {
                refreshParticipantsTable();
            } else if ("sort".equals(cardName)) {
                refreshSortedTable();
            }
        });
        panel.add(button);
        panel.add(javax.swing.Box.createVerticalStrut(5));
    }

    private JButton navigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 13));
        button.setForeground(TEXT);
        button.setBackground(WHITE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 13, 12, 10));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(ACCENT);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(WHITE);
            }
        });
        return button;
    }

    private JPanel createMainArea() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                BorderFactory.createEmptyBorder(20, 28, 18, 28)));
        JLabel applicationTitle = new JLabel("Event Registration & Ticket Management System");
        applicationTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        applicationTitle.setForeground(TEXT);
        JPanel headerText = new JPanel(new BorderLayout(0, 5));
        headerText.setOpaque(false);
        pageSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        pageSubtitle.setForeground(MUTED);
        headerText.add(applicationTitle, BorderLayout.NORTH);
        headerText.add(pageSubtitle, BorderLayout.CENTER);
        header.add(headerText, BorderLayout.CENTER);
        main.add(header, BorderLayout.NORTH);

        contentPanel.setBackground(BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        contentPanel.add(createDashboardPage(), "dashboard");
        contentPanel.add(createRegistrationPage(), "register");
        contentPanel.add(createSummaryPage(), "summary");
        contentPanel.add(createParticipantsPage(), "participants");
        contentPanel.add(createSearchPage(), "search");
        contentPanel.add(createSortPage(), "sort");
        main.add(contentPanel, BorderLayout.CENTER);
        return main;
    }

    private JPanel createDashboardPage() {
        JPanel page = new JPanel(new BorderLayout(0, 22));
        page.setOpaque(false);

        JPanel welcome = new RoundedPanel(18, WHITE);
        welcome.setLayout(new BorderLayout(0, 8));
        welcome.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        JLabel welcomeTitle = new JLabel("Welcome to Event Desk");
        welcomeTitle.setFont(new Font("SansSerif", Font.BOLD, 21));
        welcomeTitle.setForeground(TEXT);
        JLabel welcomeText = new JLabel(
                "Manage participant registrations, ticket selections and event revenue in one place.");
        welcomeText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        welcomeText.setForeground(MUTED);
        welcome.add(welcomeTitle, BorderLayout.NORTH);
        welcome.add(welcomeText, BorderLayout.CENTER);

        JPanel cards = new JPanel(new java.awt.GridLayout(1, 4, 15, 0));
        cards.setOpaque(false);
        cards.add(createMetricCard("Total Participants", "participantsMetric", "Registered guests"));
        cards.add(createMetricCard("Total Tickets", "ticketsMetric", "Tickets booked"));
        cards.add(createMetricCard("Total Revenue", "revenueMetric", "After discounts"));
        cards.add(createMetricCard("Total Discounts", "discountMetric", "Savings provided"));

        JPanel dashboardContent = new JPanel(new BorderLayout(0, 20));
        dashboardContent.setOpaque(false);
        dashboardContent.add(welcome, BorderLayout.NORTH);
        dashboardContent.add(cards, BorderLayout.CENTER);
        page.add(dashboardContent, BorderLayout.NORTH);
        return page;
    }

    private JLabel participantsMetric;
    private JLabel ticketsMetric;
    private JLabel revenueMetric;
    private JLabel discountMetric;

    private JPanel createMetricCard(String title, String metricType, String detail) {
        JPanel card = new RoundedPanel(16, WHITE);
        card.setLayout(new BorderLayout(0, 8));
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 17, 18));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.PLAIN, 12));
        heading.setForeground(MUTED);
        JLabel value = new JLabel("0");
        value.setFont(new Font("SansSerif", Font.BOLD, 23));
        value.setForeground(TEXT);
        JLabel caption = new JLabel(detail);
        caption.setFont(new Font("SansSerif", Font.PLAIN, 11));
        caption.setForeground(MUTED);
        card.add(heading, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(caption, BorderLayout.SOUTH);

        if ("participantsMetric".equals(metricType)) {
            participantsMetric = value;
        } else if ("ticketsMetric".equals(metricType)) {
            ticketsMetric = value;
        } else if ("revenueMetric".equals(metricType)) {
            revenueMetric = value;
        } else {
            discountMetric = value;
        }
        return card;
    }

    private JPanel createRegistrationPage() {
        JPanel page = new JPanel(new BorderLayout(18, 0));
        page.setOpaque(false);

        JPanel formCard = new RoundedPanel(16, WHITE);
        formCard.setLayout(new BorderLayout(0, 18));
        formCard.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        JLabel formTitle = sectionTitle("Participant details");
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        nameField = new JTextField(22);
        idField = new JTextField(22);
        emailField = new JTextField(22);
        ticketCategory = new JComboBox<>(TICKET_CATEGORIES);
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        addFormRow(form, 0, "Participant Name", nameField);
        addFormRow(form, 1, "Participant ID", idField);
        addFormRow(form, 2, "Participant Email", emailField);
        addFormRow(form, 3, "Ticket Category", ticketCategory);
        addFormRow(form, 4, "Ticket Quantity", quantitySpinner);

        JPanel buttons = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 9, 0));
        buttons.setOpaque(false);
        JButton calculateButton = primaryButton("Calculate Fee");
        calculateButton.addActionListener(this::calculateFee);
        JButton registerButton = primaryButton("Register Participant");
        registerButton.addActionListener(this::registerParticipant);
        JButton clearButton = secondaryButton("Clear Form");
        clearButton.addActionListener(event -> clearRegistrationForm());
        buttons.add(calculateButton);
        buttons.add(registerButton);
        buttons.add(clearButton);

        JPanel formBody = new JPanel(new BorderLayout(0, 20));
        formBody.setOpaque(false);
        formBody.add(form, BorderLayout.NORTH);
        formBody.add(buttons, BorderLayout.SOUTH);
        formCard.add(formTitle, BorderLayout.NORTH);
        formCard.add(formBody, BorderLayout.CENTER);

        feePreview = new RoundedPanel(16, new Color(239, 246, 255));
        feePreview.setLayout(new BorderLayout());
        feePreview.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        showEmptyFeePreview();
        page.add(formCard, BorderLayout.CENTER);
        page.add(feePreview, BorderLayout.EAST);
        page.setPreferredSize(new Dimension(850, 500));
        return page;
    }

    private void addFormRow(JPanel form, int row, String label, Component input) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(8, 0, 8, 16);
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        fieldLabel.setForeground(TEXT);
        form.add(fieldLabel, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 0, 8, 0);
        if (input instanceof JTextField) {
            ((JTextField) input).setFont(new Font("SansSerif", Font.PLAIN, 13));
            ((JTextField) input).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(8, 9, 8, 9)));
        } else {
            input.setFont(new Font("SansSerif", Font.PLAIN, 13));
        }
        form.add(input, constraints);
    }

    private void calculateFee(ActionEvent event) {
        try {
            showFeePreview(calculateFeeDetails());
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Invalid ticket quantity", JOptionPane.ERROR_MESSAGE);
        }
    }

    private FeeDetails calculateFeeDetails() {
        try {
            quantitySpinner.commitEdit();
        } catch (java.text.ParseException exception) {
            throw new IllegalArgumentException(
                    "Ticket quantity must be a whole number greater than 0.");
        }
        int quantity = (Integer) quantitySpinner.getValue();
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ticket quantity must be greater than 0.");
        }
        double price = getTicketPrice((String) ticketCategory.getSelectedItem());
        double total = price * quantity;
        double discount;
        // Discount Calculation
        if (quantity >= 10) {
            discount = total * 0.15;
        } else if (quantity >= 5) {
            discount = total * 0.10;
        } else {
            discount = 0;
        }
        return new FeeDetails(price, quantity, total, discount, total - discount);
    }

    private void showFeePreview(FeeDetails fee) {
        feePreview.removeAll();
        JPanel details = new JPanel(new BorderLayout(0, 16));
        details.setOpaque(false);
        JLabel title = sectionTitle("Fee calculation");
        JPanel values = new JPanel(new GridBagLayout());
        values.setOpaque(false);
        addPreviewRow(values, 0, "Ticket Price", money(fee.price));
        addPreviewRow(values, 1, "Quantity", Integer.toString(fee.quantity));
        addPreviewRow(values, 2, "Total Amount", money(fee.total));
        addPreviewRow(values, 3, "Discount", money(fee.discount));
        addPreviewRow(values, 4, "Final Amount", money(fee.finalAmount));
        details.add(title, BorderLayout.NORTH);
        details.add(values, BorderLayout.CENTER);
        feePreview.add(details, BorderLayout.NORTH);
        feePreview.revalidate();
        feePreview.repaint();
    }

    private void showEmptyFeePreview() {
        feePreview.removeAll();
        JLabel message = new JLabel(
                "<html><b>Fee calculation</b><br><br>Select a ticket and quantity,<br>"
                        + "then click <b>Calculate Fee</b> to<br>preview the registration cost.</html>");
        message.setFont(new Font("SansSerif", Font.PLAIN, 13));
        message.setForeground(MUTED);
        feePreview.add(message, BorderLayout.NORTH);
    }

    private void addPreviewRow(JPanel panel, int row, String label, String value) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(9, 0, 9, 12);
        JLabel field = new JLabel(label);
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        field.setForeground(MUTED);
        panel.add(field, constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        JLabel amount = new JLabel(value);
        amount.setFont(new Font("SansSerif", Font.BOLD, 12));
        amount.setForeground(TEXT);
        panel.add(amount, constraints);
    }

    // Participant Registration
    private void registerParticipant(ActionEvent event) {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        String email = emailField.getText().trim();
        if (name.isEmpty() || id.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Participant name, ID and email are required.",
                    "Missing information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (EventRegistrationSystem.participantCount >= EventRegistrationSystem.participantNames.length) {
            JOptionPane.showMessageDialog(this, "Registration limit reached.",
                    "Registration unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int index = EventRegistrationSystem.participantCount;
        String category = (String) ticketCategory.getSelectedItem();
        FeeDetails fee;
        try {
            fee = calculateFeeDetails();
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(this, exception.getMessage(),
                    "Invalid ticket quantity", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ticket Selection
        EventRegistrationSystem.participantNames[index] = name;
        EventRegistrationSystem.participantIds[index] = id;
        EventRegistrationSystem.participantEmails[index] = email;
        EventRegistrationSystem.ticketTypes[index] = category;
        EventRegistrationSystem.ticketQuantities[index] = fee.quantity;
        EventRegistrationSystem.ticketPrices[index] = fee.price;

        // Fee Calculation
        EventRegistrationSystem.totalAmounts[index] = fee.total;
        EventRegistrationSystem.discounts[index] = fee.discount;
        EventRegistrationSystem.finalAmounts[index] = fee.finalAmount;
        EventRegistrationSystem.participantCount++;

        saveLatestRegistration(index);
        refreshDashboard();
        refreshParticipantsTable();
        refreshSummary();
        showFeePreview(fee);
        JOptionPane.showMessageDialog(this, "Participant registered successfully!",
                "Registration complete", JOptionPane.INFORMATION_MESSAGE);
        clearRegistrationFieldsOnly();
    }

    private void saveLatestRegistration(int index) {
        latestRegistration[0] = EventRegistrationSystem.participantIds[index];
        latestRegistration[1] = EventRegistrationSystem.participantNames[index];
        latestRegistration[2] = EventRegistrationSystem.participantEmails[index];
        latestRegistration[3] = EventRegistrationSystem.ticketTypes[index];
        latestRegistration[4] = Integer.toString(EventRegistrationSystem.ticketQuantities[index]);
        latestRegistration[5] = money(EventRegistrationSystem.ticketPrices[index]);
        latestRegistration[6] = money(EventRegistrationSystem.totalAmounts[index]);
        latestRegistration[7] = money(EventRegistrationSystem.discounts[index]);
        latestRegistration[8] = money(EventRegistrationSystem.finalAmounts[index]);
    }

    private void clearRegistrationForm() {
        clearRegistrationFieldsOnly();
        ticketCategory.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        showEmptyFeePreview();
    }

    private void clearRegistrationFieldsOnly() {
        nameField.setText("");
        idField.setText("");
        emailField.setText("");
        ticketCategory.setSelectedIndex(0);
        quantitySpinner.setValue(1);
    }

    private JPanel createSummaryPage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);
        JPanel card = new RoundedPanel(16, WHITE);
        card.setLayout(new BorderLayout(0, 18));
        card.setBorder(BorderFactory.createEmptyBorder(24, 26, 24, 26));
        card.add(sectionTitle("Latest registration"), BorderLayout.NORTH);
        summaryDetails = new JPanel(new GridBagLayout());
        summaryDetails.setOpaque(false);
        card.add(summaryDetails, BorderLayout.CENTER);
        page.add(card, BorderLayout.NORTH);
        return page;
    }

    private void refreshSummary() {
        if (summaryDetails == null) {
            return;
        }
        summaryDetails.removeAll();
        if (latestRegistration[0] == null) {
            JLabel empty = new JLabel("No registrations yet. Register a participant to see the summary.");
            empty.setFont(new Font("SansSerif", Font.PLAIN, 14));
            empty.setForeground(MUTED);
            summaryDetails.add(empty);
        } else {
            String[] labels = {
                    "Participant ID", "Name", "Email", "Ticket Type", "Ticket Price",
                    "Quantity", "Total Amount", "Discount", "Final Amount"
            };
            for (int row = 0; row < labels.length; row++) {
                addSummaryRow(summaryDetails, row, labels[row], latestRegistration[row]);
            }
        }
        summaryDetails.revalidate();
        summaryDetails.repaint();
    }

    private void addSummaryRow(JPanel panel, int row, String label, String value) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.35;
        constraints.insets = new Insets(8, 0, 8, 20);
        JLabel field = new JLabel(label);
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setForeground(MUTED);
        panel.add(field, constraints);
        constraints.gridx = 1;
        constraints.weightx = 0.65;
        constraints.insets = new Insets(8, 0, 8, 0);
        JLabel content = new JLabel(value);
        content.setFont(new Font("SansSerif", Font.BOLD, 13));
        content.setForeground(TEXT);
        panel.add(content, constraints);
    }

    private JPanel createParticipantsPage() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        JLabel description = new JLabel("Review all participant registrations.");
        description.setFont(new Font("SansSerif", Font.PLAIN, 13));
        description.setForeground(MUTED);
        JButton refreshButton = secondaryButton("Refresh");
        refreshButton.addActionListener(event -> refreshParticipantsTable());
        toolbar.add(description, BorderLayout.WEST);
        toolbar.add(refreshButton, BorderLayout.EAST);

        participantsModel = createTableModel();
        participantsTable = createTable(participantsModel);
        JPanel tableCard = tableCard(participantsTable);
        page.add(toolbar, BorderLayout.NORTH);
        page.add(tableCard, BorderLayout.CENTER);
        return page;
    }

    private JPanel createSearchPage() {
        JPanel page = new JPanel(new BorderLayout(0, 18));
        page.setOpaque(false);
        JPanel searchCard = new RoundedPanel(16, WHITE);
        searchCard.setLayout(new BorderLayout(0, 15));
        searchCard.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        searchCard.add(sectionTitle("Find a participant"), BorderLayout.NORTH);
        JPanel controls = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));
        controls.setOpaque(false);
        JLabel label = new JLabel("Enter Participant ID");
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(TEXT);
        searchIdField = new JTextField(22);
        searchIdField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchIdField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(8, 9, 8, 9)));
        JButton searchButton = primaryButton("Search");
        searchButton.addActionListener(event -> searchParticipant());
        controls.add(label);
        controls.add(searchIdField);
        controls.add(searchButton);
        searchCard.add(controls, BorderLayout.CENTER);

        searchResultPanel = new RoundedPanel(16, WHITE);
        searchResultPanel.setLayout(new BorderLayout());
        searchResultPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        JLabel initialMessage = new JLabel("Search results will appear here.");
        initialMessage.setForeground(MUTED);
        searchResultPanel.add(initialMessage, BorderLayout.NORTH);
        page.add(searchCard, BorderLayout.NORTH);
        page.add(searchResultPanel, BorderLayout.CENTER);
        return page;
    }

    // Participant Search
    private void searchParticipant() {
        String searchId = searchIdField.getText().trim();
        if (searchId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a participant ID to search.",
                    "Missing participant ID", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int foundIndex = -1;
        // Linear search
        for (int index = 0; index < EventRegistrationSystem.participantCount; index++) {
            if (EventRegistrationSystem.participantIds[index].equalsIgnoreCase(searchId)) {
                foundIndex = index;
                break;
            }
        }

        searchResultPanel.removeAll();
        if (foundIndex < 0) {
            JLabel notFound = new JLabel("Participant not found.");
            notFound.setFont(new Font("SansSerif", Font.BOLD, 15));
            notFound.setForeground(MUTED);
            searchResultPanel.add(notFound, BorderLayout.NORTH);
        } else {
            searchResultPanel.add(sectionTitle("Participant found"), BorderLayout.NORTH);
            JPanel details = new JPanel(new GridBagLayout());
            details.setOpaque(false);
            addSummaryRow(details, 0, "Participant ID",
                    EventRegistrationSystem.participantIds[foundIndex]);
            addSummaryRow(details, 1, "Name",
                    EventRegistrationSystem.participantNames[foundIndex]);
            addSummaryRow(details, 2, "Email",
                    EventRegistrationSystem.participantEmails[foundIndex]);
            addSummaryRow(details, 3, "Ticket Type",
                    EventRegistrationSystem.ticketTypes[foundIndex]);
            addSummaryRow(details, 4, "Quantity",
                    Integer.toString(EventRegistrationSystem.ticketQuantities[foundIndex]));
            addSummaryRow(details, 5, "Total Amount",
                    money(EventRegistrationSystem.totalAmounts[foundIndex]));
            addSummaryRow(details, 6, "Discount",
                    money(EventRegistrationSystem.discounts[foundIndex]));
            addSummaryRow(details, 7, "Final Amount",
                    money(EventRegistrationSystem.finalAmounts[foundIndex]));
            searchResultPanel.add(details, BorderLayout.CENTER);
        }
        searchResultPanel.revalidate();
        searchResultPanel.repaint();
    }

    private JPanel createSortPage() {
        JPanel page = new JPanel(new BorderLayout(0, 16));
        page.setOpaque(false);
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        JLabel explanation = new JLabel("Sort registered participants alphabetically by name.");
        explanation.setFont(new Font("SansSerif", Font.PLAIN, 13));
        explanation.setForeground(MUTED);
        JButton sortButton = primaryButton("Sort Participants by Name");
        sortButton.addActionListener(event -> sortParticipants());
        toolbar.add(explanation, BorderLayout.WEST);
        toolbar.add(sortButton, BorderLayout.EAST);

        sortedModel = createTableModel();
        sortedTable = createTable(sortedModel);
        page.add(toolbar, BorderLayout.NORTH);
        page.add(tableCard(sortedTable), BorderLayout.CENTER);
        return page;
    }

    // Participant Sorting
    private void sortParticipants() {
        if (EventRegistrationSystem.participantCount < 2) {
            JOptionPane.showMessageDialog(this, "Not enough participants to sort.",
                    "Sorting unavailable", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Bubble-sort participant names and move every related array value with each participant.
        for (int first = 0; first < EventRegistrationSystem.participantCount - 1; first++) {
            for (int second = first + 1; second < EventRegistrationSystem.participantCount; second++) {
                if (EventRegistrationSystem.participantNames[first]
                        .compareToIgnoreCase(EventRegistrationSystem.participantNames[second]) > 0) {
                    swap(EventRegistrationSystem.participantNames, first, second);
                    swap(EventRegistrationSystem.participantIds, first, second);
                    swap(EventRegistrationSystem.participantEmails, first, second);
                    swap(EventRegistrationSystem.ticketTypes, first, second);
                    swap(EventRegistrationSystem.ticketQuantities, first, second);
                    swap(EventRegistrationSystem.ticketPrices, first, second);
                    swap(EventRegistrationSystem.totalAmounts, first, second);
                    swap(EventRegistrationSystem.discounts, first, second);
                    swap(EventRegistrationSystem.finalAmounts, first, second);
                }
            }
        }
        refreshParticipantsTable();
        refreshSortedTable();
        JOptionPane.showMessageDialog(this, "Participants sorted by name successfully!",
                "Sorting complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void swap(String[] values, int first, int second) {
        String temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private void swap(int[] values, int first, int second) {
        int temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private void swap(double[] values, int first, int second) {
        double temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setRowHeight(34);
        table.setGridColor(BORDER);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setForeground(TEXT);
        header.setBackground(new Color(243, 247, 252));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 38));
        for (int column = 0; column < TABLE_COLUMNS.length; column++) {
            table.getColumnModel().getColumn(column).setPreferredWidth(
                    column == 2 ? 190 : (column == 1 ? 145 : 120));
        }
        return table;
    }

    private JPanel tableCard(JTable table) {
        JPanel card = new RoundedPanel(16, WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(WHITE);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    private void refreshParticipantsTable() {
        if (participantsModel == null) {
            return;
        }
        populateTable(participantsModel);
    }

    private void refreshSortedTable() {
        if (sortedModel == null) {
            return;
        }
        populateTable(sortedModel);
    }

    private void populateTable(DefaultTableModel model) {
        model.setRowCount(0);
        for (int index = 0; index < EventRegistrationSystem.participantCount; index++) {
            model.addRow(new Object[]{
                    EventRegistrationSystem.participantIds[index],
                    EventRegistrationSystem.participantNames[index],
                    EventRegistrationSystem.participantEmails[index],
                    EventRegistrationSystem.ticketTypes[index],
                    EventRegistrationSystem.ticketQuantities[index],
                    money(EventRegistrationSystem.ticketPrices[index]),
                    money(EventRegistrationSystem.totalAmounts[index]),
                    money(EventRegistrationSystem.discounts[index]),
                    money(EventRegistrationSystem.finalAmounts[index])
            });
        }
    }

    private void refreshDashboard() {
        if (participantsMetric == null) {
            return;
        }
        int ticketCount = 0;
        double revenue = 0;
        double discountTotal = 0;
        for (int index = 0; index < EventRegistrationSystem.participantCount; index++) {
            ticketCount += EventRegistrationSystem.ticketQuantities[index];
            revenue += EventRegistrationSystem.finalAmounts[index];
            discountTotal += EventRegistrationSystem.discounts[index];
        }
        participantsMetric.setText(Integer.toString(EventRegistrationSystem.participantCount));
        ticketsMetric.setText(Integer.toString(ticketCount));
        revenueMetric.setText(money(revenue));
        discountMetric.setText(money(discountTotal));
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?",
                "Confirm exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    private String subtitleFor(String cardName) {
        switch (cardName) {
            case "dashboard":
                return "Manage participants, tickets and registrations";
            case "register":
                return "Add a participant and calculate their ticket fee";
            case "summary":
                return "Review the most recent participant registration";
            case "participants":
                return "Browse all participant and ticket details";
            case "search":
                return "Find a registration using its participant ID";
            case "sort":
                return "Organize the participant list by name";
            default:
                return "";
        }
    }

    private JLabel sectionTitle(String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(TEXT);
        return label;
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new BasicButtonUI());
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(WHITE);
        button.setBackground(PRIMARY);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent event) {
                button.setBackground(PRIMARY_DARK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent event) {
                button.setBackground(PRIMARY);
            }
        });
        return button;
    }

    private JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setForeground(PRIMARY);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 213, 244)),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }

    private double getTicketPrice(String category) {
        // Ticket Selection
        switch (category) {
            case "Regular":
                return 500;
            case "VIP":
                return 1500;
            case "Student":
                return 300;
            case "Premium":
                return 2500;
            default:
                throw new IllegalArgumentException("Unknown ticket category: " + category);
        }
    }

    private String money(double amount) {
        return String.format("Rs. %,.2f", amount);
    }

    private static class FeeDetails {
        private final double price;
        private final int quantity;
        private final double total;
        private final double discount;
        private final double finalAmount;

        private FeeDetails(double price, int quantity, double total, double discount, double finalAmount) {
            this.price = price;
            this.quantity = quantity;
            this.total = total;
            this.discount = discount;
            this.finalAmount = finalAmount;
        }
    }

    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int cornerRadius;
        private final Color fillColor;

        private RoundedPanel(int cornerRadius, Color fillColor) {
            this.cornerRadius = cornerRadius;
            this.fillColor = fillColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(fillColor);
            graphics2D.fillRoundRect(0, 0, getWidth(), getHeight(),
                    cornerRadius, cornerRadius);
            graphics2D.dispose();
            super.paintComponent(graphics);
        }
    }
}
