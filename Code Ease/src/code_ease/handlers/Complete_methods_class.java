package code_ease.handlers;

import java.awt.BorderLayout;
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
import java.awt.Point;

import javax.swing.JPanel;
import java.awt.event.FocusAdapter;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;

// implement it with IObjectActionDelegate

public class Complete_methods_class{

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
					//Complete_methods_class window = new Complete_methods_class();
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
	public Complete_methods_class(String s) {
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
		
			
		label = new JLabel(" Complete Methods");
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
		
		JButton close_button = new JButton();																		// close button
		close_button.setIcon(new ImageIcon("F:\\CodeEaseDatabase\\cross.png"));
		close_button.setBounds(167, 2, 15, 15);
		close_button.setBorderPainted(false);
		close_button.setBackground(new Color(250,250,204));
		panel.add(close_button,BorderLayout.EAST);
		
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
		
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		
		CodeCompletion cc = new CodeCompletion();								// calling class of code completion
		ArrayList<String> array = null;
		
		selectedText = selectedText.toLowerCase();
		
		if(selectedText.contains("read")){
			array = cc.getReadFileMethodNames();
		}
		else if(selectedText.contains("write")){
			array = cc.getWriteFileMethodNames();
		}
		else if(selectedText.contains("min")){
			array = cc.getMinMethodNames();
		}
		else if(selectedText.contains("max")){
			array = cc.getMaxMethodNames();
		}
		else if(selectedText.contains("extract")){
			array = cc.getExtractFileNameMethodNames();
		}
		else{
			JOptionPane.showMessageDialog(frame, "No recommendations found!");
		}
		
		String [] methods = new String[array.size()];
		for(int i = 0; i < array.size(); i++){
			methods[i] = array.get(i);											// list
		}

		DefaultListModel dlm = new DefaultListModel();
		for(int i = 0; i < methods.length; i++){
			dlm.addElement(new ListEntry(methods[i],new ImageIcon("F:\\CodeEaseDatabase\\green.gif")));
		}
		
		JList list = new JList(dlm);
		list.setForeground(new Color(0, 0, 0));
		list.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		list.setCellRenderer(new ListEntryCellRenderer());
		list.setBackground(new Color(250,250,204));
		
		scrollPane.setViewportView(list);
		
		
	
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				int i = list.getSelectedIndex();
				ArrayList<String> definition = null;
				
				if(selectedText.contains("read")){
					definition = cc.getFileReadMethodBody(i+1);
				}
				else if(selectedText.contains("write")){
					definition = cc.getFileWriteMethodBody(i+1);
				}
				else if(selectedText.contains("min")){
					definition = cc.getMinMethodBody(i+1);
				}
				else if(selectedText.contains("max")){
					definition = cc.getMaxMethodBody(i+1);
				}
				else if(selectedText.contains("extract")){
					definition = cc.getExtractFileNameMethodBody(i+1);
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
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
	
}
