package vansh;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends DefaultCellEditor {
  private JButton button;
  private int value;
  private RestaurantManagementSystem system;

  public ButtonEditor(String text, RestaurantManagementSystem system, int increment) {
    super(new JTextField());
    this.button = new JButton(text);
    this.system = system;
    this.value = increment;

    this.button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        system.updateTotal();
      }
    });
  }

  @Override
  public Object getCellEditorValue() {
    return value;
  }
}
