package vansh;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantManagementSystemTest {

  private RestaurantManagementSystem system;

  @BeforeEach
  public void setUp() {
    system = new RestaurantManagementSystem();
  }

  @Test
  public void testInitialState() {
    // Check initial values and component state
    assertEquals("Total: $0.00", system.totalLabel.getText());
    assertEquals("Status: Ready", system.statusLabel.getText());
    assertEquals("", system.tableNumberField.getText());
    assertFalse(system.cancelButton.isEnabled());
  }

  @Test
  public void testExcelFileCreation() {
    File excelFile = new File("restaurant_data.xlsx");
    assertTrue(excelFile.exists(), "Excel file should be created");

    // Verify the Excel file contains necessary sheets
    try (FileInputStream fis = new FileInputStream(excelFile);
        Workbook workbook = new XSSFWorkbook(fis)) {

      assertNotNull(workbook.getSheet("Menu"), "Menu sheet should exist");
      assertNotNull(workbook.getSheet("Orders"), "Orders sheet should exist");
      assertNotNull(workbook.getSheet("Order_Items"), "Order_Items sheet should exist");

    } catch (IOException e) {
      fail("IOException occurred while reading the Excel file");
    }
  }

  @Test
  public void testUpdateTotal() {
    // Add some test data to the table model
    DefaultTableModel model = (DefaultTableModel) system.menuTable.getModel();
    model.addRow(new Object[] { 1, "Dish A", 10.0, 2, "Increase", "Decrease" });
    model.addRow(new Object[] { 2, "Dish B", 5.0, 3, "Increase", "Decrease" });

    system.updateTotal();

    assertEquals("Total: $35.00", system.totalLabel.getText(), "Total should be calculated correctly");
  }

  @Test
  public void testPlaceOrder() {
    // Prepare some data in the menuTable
    DefaultTableModel model = (DefaultTableModel) system.menuTable.getModel();
    model.addRow(new Object[] { 1, "Dish A", 10.0, 2, "Increase", "Decrease" });

    // Set table number and place order
    system.tableNumberField.setText("5");
    system.confirmOrder();

    // Assuming placeOrder() updates the statusLabel
    assertEquals("Status: Order placed successfully", system.statusLabel.getText());
  }

  @Test
  public void testCancelOrder() {
    // Simulate order setup
    system.tableNumberField.setText("5");
    system.totalLabel.setText("Total: $20.00");

    // Cancel order
    system.cancelOrder();

    // Verify order is cancelled and reset
    assertEquals("Total: $0.00", system.totalLabel.getText());
    assertEquals("Status: Order canceled", system.statusLabel.getText());
    assertEquals("", system.tableNumberField.getText());
  }
}
