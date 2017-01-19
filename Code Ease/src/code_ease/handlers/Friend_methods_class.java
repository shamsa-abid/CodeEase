package code_ease.handlers;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;

import lums.cbrs.completion.CodeCompletion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;

import javax.swing.JPanel;
import java.awt.event.FocusAdapter;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;

public class Friend_methods_class implements IObjectActionDelegate {

	private JFrame frame;
	public JLabel label;
	private String selectedText; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Friend_methods_class window = new Friend_methods_class();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Friend_methods_class(String s) {
		initialize(s);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String s) {
		frame = new JFrame();	
		selectedText = s;
		
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 11));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Salman Javed\\Desktop\\C.PNG"));
		
		frame.setBounds(500,200, 184, 253);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setUndecorated(true);
		frame.getContentPane().setLayout(null);
	
		FrameDragListener frameDragListener = new FrameDragListener(frame);
        frame.addMouseListener(frameDragListener);
        frame.addMouseMotionListener(frameDragListener);

    	
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 184, 253);
		panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panel.setBackground(new Color(250,250,204));							// outer panel
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
			
		label = new JLabel("  Friend Methods Found");
		label.setForeground(new Color(102, 102, 204));
		label.setBounds(5, 5, 130, 20);
		panel.add(label);
		label.setFont(new Font("Segoe UI", Font.BOLD, 12));
		label.setOpaque(true);													// friend method label
		label.setBackground(new Color(250,250,204));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 163, 206);
		panel.add(scrollPane);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
	
		selectedText = selectedText.toLowerCase();
	
		
		CodeCompletion cc = new CodeCompletion();								// calling class of code completion
		ArrayList<String> array = null;
		
		if(selectedText.contains("read")){
			array = cc.getWriteFileMethodNames();
		}
		else if(selectedText.contains("write")){
			array = cc.getReadFileMethodNames();
		}
		else if(selectedText.contains("min")){
			array = cc.getMinFriendMethodNames();
		}
		else if(selectedText.contains("max")){
			array = cc.getMaxFriendMethodNames();
		}
		else if(selectedText.contains("extract")){
			array = cc.getExtractFileNameFriendMethodNames();
		}
		else{
			JOptionPane.showMessageDialog(frame, "No recommendations found!");
		}
		
		String[] methods = new String[array.size()];
		
		DefaultListModel dlm = new DefaultListModel();
		
		for(int i = 0; i < array.size(); i++){
			methods[i] = array.get(i);													// list
			dlm.addElement(new ImgsNText(methods[i], new ImageIcon("F:\\CodeEaseDatabase\\green.gif")));
		}
		
		JList list = new JList();
		list.setForeground(new Color(0, 0, 0));
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		list.setCellRenderer(new Renderer());
		list.setModel(dlm);
		list.setBackground(new Color(250,250,204));
		
		scrollPane.setViewportView(list);
		
		JButton close_button = new JButton("");																	// close button
		close_button.setIcon(new ImageIcon("F:\\CodeEaseDatabase\\cross.png"));	
		close_button.setBounds(167, 2, 15, 15);
		close_button.setBorderPainted(false);
		close_button.setBackground(new Color(250,250,204));
		
		close_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();		
				
				Frame [] frames = JFrame.getFrames();
				for(int i = 0; i < frames.length; i++){
					frames[i].dispose();
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				close_button.setBackground(Color.ORANGE);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				close_button.setBackground(new Color(250,250,204));
				}
		});
		
		panel.add(close_button);
		
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				int i = list.getSelectedIndex();

				
				ArrayList<String> definition = null;
				
				if(selectedText.contains("read")){
					definition = cc.getFileWriteMethodBody(i+1);
				}
				else if(selectedText.contains("write")){
					definition = cc.getFileReadMethodBody(i+1);
				}
				else if(selectedText.contains("min")){
					definition = cc.getMinFriendMethodBody(i+1);
				}
				else if(selectedText.contains("max")){
					definition = cc.getMaxFriendMethodBody(i+1);
				}
				else if(selectedText.contains("extract")){
					definition = cc.getExtractFileNameFriendMethodBody(i+1);
				}
				
				
				
				// only one frame i.e only one should appear code missing
				
				Frame [] frames = Frame.getFrames();
				Point p = frame.getLocation();
				
				if(frames.length > 1){
					
					for(int j = 0; j < frames.length; j++){
						if(frames[j].getName() == "method definition"){
							frames[j].dispose();
						}
					}
					Methods_definition_class second_frame = new Methods_definition_class(definition,p);
				
				}
				else{
					Methods_definition_class second_frame = new Methods_definition_class(definition,p);
					
				}
			}
		});
		
		
		   
		frame.setResizable(false);
		frame.setVisible(true);
		
	}

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub
		
	}
}





class FrameDragListener extends MouseAdapter {

    private final JFrame frame;
    private Point mouseDownCompCoords = null;

    public FrameDragListener(JFrame frame) {
        this.frame = frame;
    }

    public void mouseReleased(MouseEvent e) {
        mouseDownCompCoords = null;
    }

    public void mousePressed(MouseEvent e) {
        mouseDownCompCoords = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
        Point currCoords = e.getLocationOnScreen();
        frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
	
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}









////////////////////////////////////////////////



class ListEntry
{
   private String value;
   private ImageIcon icon;
  
   public ListEntry(String value, ImageIcon icon) {
      this.value = value;
      this.icon = icon;
   }
  
   public String getValue() {
      return value;
   }
  
   public ImageIcon getIcon() {
      return icon;
   }
  
   public String toString() {
      return value;
   }
}
  
class ListEntryCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>
{
   private JLabel label;
  
   public Component getListCellRendererComponent(JList list, Object value,
                                                 int index, boolean isSelected,
                                                 boolean cellHasFocus) {
      ListEntry entry = (ListEntry) value;
  
      setText(value.toString());
      setIcon(entry.getIcon());
   
      if (isSelected) {
         setBackground(list.getSelectionBackground());
         setForeground(list.getSelectionForeground());
      }
      else {
         setBackground(list.getBackground());
         setForeground(list.getForeground());
      }
  
      setEnabled(list.isEnabled());
      setFont(list.getFont());
      setOpaque(true);
  
      return this;
   }
}






