package vansh;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;

public class ButtonRenderer extends JButton implements TableCellRenderer {
  public ButtonRenderer(String text) {
    setText(text);
  }

  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    return this;
  }
}
