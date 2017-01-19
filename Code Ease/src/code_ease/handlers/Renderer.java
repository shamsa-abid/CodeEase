package code_ease.handlers;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;



public class Renderer extends DefaultListCellRenderer implements ListCellRenderer<Object>
{
 

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,boolean cellHasFocus) {
     
		ImgsNText it = (ImgsNText) value;
		setText(it.getName());
		setIcon(it.getImage());
		
		if(isSelected){
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		}
		else{
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		setEnabled(true);
		setFont(list.getFont());
		
		return this;

	}

}

