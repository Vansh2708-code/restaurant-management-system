package vansh;

package vansh;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class RestaurantManagementSystem extends JFrame {
  private JTextField tableNumberField;
  private JTable menuTable;
  private DefaultTableModel tableModel;
  private JButton orderButton, cancelButton, viewHistoryButton, editMenuButton;
  private JLabel totalLabel, statusLabel;
  private double totalPrice = 0.0;
  private File excelFile = new File("restaurant_data.xlsx");
  private File errorLogFile = new File("error_log.txt");

  public RestaurantManagementSystem() {
    setTitle("Restaurant Management System");
    setSize(800, 700);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    initComponents();
    checkAndCreateExcelFile();
    loadMenuData();
  }

  private void initComponents() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(new java.awt.Color(230, 240, 250));

    // Top Panel - Table Number Input and Buttons
    JPanel topPanel = new JPanel(new FlowLayout());
    topPanel.setBackground(new java.awt.Color(110, 160, 220));

    JLabel tableNumberLabel = new JLabel("Table Number:");
    tableNumberLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    tableNumberField = new JTextField(5);

    orderButton = new JButton("Place Order");
    orderButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    orderButton.setBackground(new java.awt.Color(30, 130, 180));
    orderButton.setForeground(Color.WHITE);
    orderButton.addActionListener(e -> confirmOrder());

    cancelButton = new JButton("Cancel Order");
    cancelButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    cancelButton.setBackground(new java.awt.Color(220, 20, 60));
    cancelButton.setForeground(Color.WHITE);
    cancelButton.setEnabled(false);
    cancelButton.addActionListener(e -> cancelOrder());

    viewHistoryButton = new JButton("View Order History");
    viewHistoryButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    viewHistoryButton.setBackground(new java.awt.Color(70, 130, 180));
    viewHistoryButton.setForeground(Color.WHITE);
    viewHistoryButton.addActionListener(e -> viewOrderHistory());

    editMenuButton = new JButton("Edit Menu");
    editMenuButton.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
    editMenuButton.setBackground(new java.awt.Color(100, 149, 237));
    editMenuButton.setForeground(Color.WHITE);
    editMenuButton.addActionListener(e -> editMenu());

    topPanel.add(tableNumberLabel);
    topPanel.add(tableNumberField);
    topPanel.add(orderButton);
    topPanel.add(cancelButton);
    topPanel.add(viewHistoryButton);
    topPanel.add(editMenuButton);

    panel.add(topPanel, BorderLayout.NORTH);

    // Menu Table
    String[] columnNames = { "Dish ID", "Dish Name", "Price", "Quantity", "Increase", "Decrease" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 3; // Only quantity column is editable
      }
    };
    menuTable = new JTable(tableModel);
    menuTable.setRowHeight(30);
    menuTable.getColumn("Increase").setCellRenderer(new ButtonRenderer("Increase"));
    menuTable.getColumn("Decrease").setCellRenderer(new ButtonRenderer("Decrease"));
    menuTable.getColumn("Increase").setCellEditor(new ButtonEditor("Increase", this, 1));
    menuTable.getColumn("Decrease").setCellEditor(new ButtonEditor("Decrease", this, -1));

    JScrollPane scrollPane = new JScrollPane(menuTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    // Bottom Panel - Total Price Display and Status Label
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.setBackground(new java.awt.Color(120, 170, 220));

    totalLabel = new JLabel("Total: $0.00");
    totalLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
    totalLabel.setForeground(new java.awt.Color(0, 100, 0));

    statusLabel = new JLabel("Status: Ready");
    statusLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
    statusLabel.setForeground(new java.awt.Color(0, 0, 128));

    bottomPanel.add(totalLabel);
    bottomPanel.add(statusLabel);
    panel.add(bottomPanel, BorderLayout.SOUTH);

    add(panel);
  }

  private void checkAndCreateExcelFile() {
    try {
      if (!excelFile.exists()) {
        Workbook workbook = new XSSFWorkbook();
        createDefaultSheets(workbook);
        try (FileOutputStream fos = new FileOutputStream(excelFile)) {
          workbook.write(fos);
        }
        workbook.close();
        JOptionPane.showMessageDialog(this, "Excel file created successfully.");
      } else {
        verifySheetStructure();
      }
    } catch (IOException e) {
      logError("Error creating Excel file", e);
      JOptionPane.showMessageDialog(this, "Error creating Excel file. See error log for details.");
    }
  }

  private void createDefaultSheets(Workbook workbook) {
    Sheet menuSheet = workbook.createSheet("Menu");
    Row headerRow = menuSheet.createRow(0);
    headerRow.createCell(0).setCellValue("Dish ID");
    headerRow.createCell(1).setCellValue("Dish Name");
    headerRow.createCell(2).setCellValue("Price");

    Sheet ordersSheet = workbook.createSheet("Orders");
    Row ordersHeader = ordersSheet.createRow(0);
    ordersHeader.createCell(0).setCellValue("Order ID");
    ordersHeader.createCell(1).setCellValue("Table Number");
    ordersHeader.createCell(2).setCellValue("Total Price");

    Sheet orderItemsSheet = workbook.createSheet("Order_Items");
    Row itemsHeader = orderItemsSheet.createRow(0);
    itemsHeader.createCell(0).setCellValue("Order ID");
    itemsHeader.createCell(1).setCellValue("Dish ID");
    itemsHeader.createCell(2).setCellValue("Quantity");
  }

  private void verifySheetStructure() throws IOException {
    try (FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis)) {

      if (workbook.getSheet("Menu") == null || workbook.getSheet("Orders") == null
          || workbook.getSheet("Order_Items") == null) {
        JOptionPane.showMessageDialog(this, "One or more required sheets are missing. Recreating the file.");
        createDefaultSheets(workbook);
      }
    }
  }

  private void loadMenuData() {
    try (FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis)) {

      Sheet menuSheet = workbook.getSheet("Menu");
      if (menuSheet == null) {
        JOptionPane.showMessageDialog(this, "No menu data available.");
        return;
      }

      Iterator<Row> iterator = menuSheet.iterator();
      iterator.next(); // Skip header row

      while (iterator.hasNext()) {
        Row row = iterator.next();
        int dishId = (int) row.getCell(0).getNumericCellValue();
        String dishName = row.getCell(1).getStringCellValue();
        double price = row.getCell(2).getNumericCellValue();

        tableModel.addRow(new Object[] { dishId, dishName, price, 1, "Increase", "Decrease" });
      }
    } catch (IOException e) {
      logError("Failed to load menu data", e);
    }
  }

  private void confirmOrder() {
    StringBuilder orderSummary = new StringBuilder("Order Summary:\n");
    totalPrice = 0.0;

    for (int row = 0; row < menuTable.getRowCount(); row++) {
      String dishName = (String) menuTable.getValueAt(row, 1);
      double price = (double) menuTable.getValueAt(row, 2);
      int quantity = (int) menuTable.getValueAt(row, 3);

      orderSummary.append(dishName).append(" x").append(quantity).append(" - $").append(price * quantity).append("\n");
      totalPrice += price * quantity;
    }

    orderSummary.append("\nTotal: $").append(String.format("%.2f", totalPrice));

    int confirm = JOptionPane.showConfirmDialog(this, orderSummary.toString(), "Confirm Order",
        JOptionPane.YES_NO_CANCEL_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      placeOrder();
    } else if (confirm == JOptionPane.NO_OPTION) {
      statusLabel.setText("Status: Order canceled");
    }
  }

  private void placeOrder() {
    JOptionPane.showMessageDialog(this, "Order placed successfully!", "Order", JOptionPane.INFORMATION_MESSAGE);
    totalLabel.setText("Total: $0.00");
    statusLabel.setText("Status: Order placed successfully");
    tableNumberField.setText("");
  }

  private void viewOrderHistory() {
    // Open a new window to display order history from the Excel file
  }

  private void editMenu() {
    // Open a new window to allow editing the menu in the Excel file
  }

  private void cancelOrder() {
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this order?", "Cancel Order",
        JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      totalPrice = 0;
      totalLabel.setText("Total: $0.00");
      statusLabel.setText("Status: Order canceled");
      tableNumberField.setText("");
    }
  }

  private void logError(String message, Exception e) {
    try (FileWriter fw = new FileWriter(errorLogFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw)) {
      pw.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - " + message);
      e.printStackTrace(pw);
    } catch (IOException ex) {
      System.err.println("Error writing to log file: " + ex.getMessage());
    }
  }

  public void updateTotal() {
    totalPrice = 0.0;
    for (int row = 0; row < menuTable.getRowCount(); row++) {
      double price = (double) menuTable.getValueAt(row, 2);
      int quantity = (int) menuTable.getValueAt(row, 3);
      totalPrice += price * quantity;
    }
    totalLabel.setText("Total: $" + String.format("%.2f", totalPrice));
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      RestaurantManagementSystem app = new RestaurantManagementSystem();
      app.setVisible(true);
    });
  }
}
