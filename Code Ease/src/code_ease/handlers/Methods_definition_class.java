package code_ease.handlers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.eclipse.swt.events.SelectionAdapter;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import insertcode.CodeInsertion;

import javax.swing.ImageIcon;
import java.awt.Toolkit;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

public class Methods_definition_class {

	private JFrame frame;
	private String method;
	private RSyntaxTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//Frame_2 window = new Frame_2();
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
	public Methods_definition_class(ArrayList<String> def, Point p) {
		initialize(def,p);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ArrayList<String> def,Point p) {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Salman Javed\\Desktop\\C.PNG"));
		frame.setBounds(p.x+185, p.y, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setName("method definition");
		frame.setUndecorated(true);
	
		
		ComponentResizer cr = new ComponentResizer();
		cr.registerComponent(frame);										
		cr.setMaximumSize(new Dimension(1336,768));								// component resizer
		
		ComponentMover cm = new ComponentMover();
		cm.registerComponent(frame);
		cm.setDragInsets(cr.getDragInsets());
		
		JPanel panel_0 = new JPanel(new BorderLayout());						// parent panel
		panel_0.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		
		
		frame.getContentPane().add(panel_0,BorderLayout.CENTER);
		
		
		///////////////////////////////////////////////////////////////////////////////////////
		
		
		JPanel panel_1 = new JPanel(new BorderLayout());
		panel_1.setBounds(10, 11, 414, 239);
		textArea = new RSyntaxTextArea(20, 60);	
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);			// editor panel
		textArea.setCodeFoldingEnabled(true);
		
		// setting text
		method = "";
		
		for(int i = 0; i < def.size(); i++){
			String temp = def.get(i);
			method += temp + "\r\n";
		}
		textArea.setText(method);
		textArea.setEditable(false);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		panel_1.add(sp,BorderLayout.CENTER);										// text area
		panel_0.add(panel_1,BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel(new BorderLayout());
		JPanel panel_3 = new JPanel(new BorderLayout());							
		panel_2.add(panel_3,BorderLayout.EAST);
		panel_2.setBackground(new Color(250,250,204));
		
		
		
		/////////////////////////////////////////////////////////////////////////////////////
		
		JButton close_button = new JButton();
		close_button.setIcon(new ImageIcon("F:\\CodeEaseDatabase\\cross.png"));
		close_button.setPreferredSize(new Dimension(15,15));
		close_button.setBorderPainted(false);
		close_button.setBackground(new Color(250,250,204));
		panel_3.add(close_button,BorderLayout.EAST);
		
		close_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				frame.dispose();							// close button
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
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		
		JButton insert_button = new JButton();
		insert_button.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  System.out.println("Insert Code clicked");
				
				CodeInsertion ci = new CodeInsertion();
				ci.performInsert(textArea.getText());
				
		  }
		});
		
		insert_button.setIcon(new ImageIcon("F:\\CodeEaseDatabase\\external-browser.png"));
		insert_button.setPreferredSize(new Dimension(20,15));
		insert_button.setBackground(new Color(250,250,204));
		insert_button.setToolTipText("Insert Your Code");
		
		
		
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		panel_3.add(insert_button,BorderLayout.WEST);
		
		
		panel_0.add(panel_2,BorderLayout.NORTH);
			
		frame.pack();
		frame.setVisible(true);
		
		
	}
}


class ComponentResizer extends MouseAdapter
{
	private final static Dimension MINIMUM_SIZE = new Dimension(10, 10);
	private final static Dimension MAXIMUM_SIZE =
		new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

	private static Map<Integer, Integer> cursors = new HashMap<Integer, Integer>();
	{
		cursors.put(1, Cursor.N_RESIZE_CURSOR);
		cursors.put(2, Cursor.W_RESIZE_CURSOR);
		cursors.put(4, Cursor.S_RESIZE_CURSOR);
		cursors.put(8, Cursor.E_RESIZE_CURSOR);
		cursors.put(3, Cursor.NW_RESIZE_CURSOR);
		cursors.put(9, Cursor.NE_RESIZE_CURSOR);
		cursors.put(6, Cursor.SW_RESIZE_CURSOR);
		cursors.put(12, Cursor.SE_RESIZE_CURSOR);
	}

	private Insets dragInsets;
	private Dimension snapSize;

	private int direction;
	protected static final int NORTH = 1;
	protected static final int WEST = 2;
	protected static final int SOUTH = 4;
	protected static final int EAST = 8;

	private Cursor sourceCursor;
	private boolean resizing;
	private Rectangle bounds;
	private Point pressed;
	private boolean autoscrolls;

	private Dimension minimumSize = MINIMUM_SIZE;
	private Dimension maximumSize = MAXIMUM_SIZE;

	/**
	 *  Convenience contructor. All borders are resizable in increments of
	 *  a single pixel. Components must be registered separately.
	 */
	public ComponentResizer()
	{
		this(new Insets(5, 5, 5, 5), new Dimension(1, 1));
	}

	/**
	 *  Convenience contructor. All borders are resizable in increments of
	 *  a single pixel. Components can be registered when the class is created
	 *  or they can be registered separately afterwards.
	 *
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(Component... components)
	{
		this(new Insets(5, 5, 5, 5), new Dimension(1, 1), components);
	}

	/**
	 *  Convenience contructor. Eligible borders are resisable in increments of
	 *  a single pixel. Components can be registered when the class is created
	 *  or they can be registered separately afterwards.
	 *
	 *  @param dragInsets Insets specifying which borders are eligible to be
	 *                    resized.
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(Insets dragInsets, Component... components)
	{
		this(dragInsets, new Dimension(1, 1), components);
	}

	/**
	 *  Create a ComponentResizer.
	 *
	 *  @param dragInsets Insets specifying which borders are eligible to be
	 *                    resized.
	 *  @param snapSize Specify the dimension to which the border will snap to
	 *                  when being dragged. Snapping occurs at the halfway mark.
	 *  @param components components to be automatically registered
	 */
	public ComponentResizer(Insets dragInsets, Dimension snapSize, Component... components)
	{
		setDragInsets( dragInsets );
		setSnapSize( snapSize );
		registerComponent( components );
	}

	/**
	 *  Get the drag insets
	 *
	 *  @return  the drag insets
	 */
	public Insets getDragInsets()
	{
		return dragInsets;
	}

	/**
	 *  Set the drag dragInsets. The insets specify an area where mouseDragged
	 *  events are recognized from the edge of the border inwards. A value of
	 *  0 for any size will imply that the border is not resizable. Otherwise
	 *  the appropriate drag cursor will appear when the mouse is inside the
	 *  resizable border area.
	 *
	 *  @param  dragInsets Insets to control which borders are resizeable.
	 */
	public void setDragInsets(Insets dragInsets)
	{
		validateMinimumAndInsets(minimumSize, dragInsets);

		this.dragInsets = dragInsets;
	}

	/**
	 *  Get the components maximum size.
	 *
	 *  @return the maximum size
	 */
	public Dimension getMaximumSize()
	{
		return maximumSize;
	}

	/**
	 *  Specify the maximum size for the component. The component will still
	 *  be constrained by the size of its parent.
	 *
	 *  @param maximumSize the maximum size for a component.
	 */
	public void setMaximumSize(Dimension maximumSize)
	{
		this.maximumSize = maximumSize;
	}

	/**
	 *  Get the components minimum size.
	 *
	 *  @return the minimum size
	 */
	public Dimension getMinimumSize()
	{
		return minimumSize;
	}

	/**
	 *  Specify the minimum size for the component. The minimum size is
	 *  constrained by the drag insets.
	 *
	 *  @param minimumSize the minimum size for a component.
	 */
	public void setMinimumSize(Dimension minimumSize)
	{
		validateMinimumAndInsets(minimumSize, dragInsets);

		this.minimumSize = minimumSize;
	}

	/**
	 *  Remove listeners from the specified component
	 *
	 *  @param component  the component the listeners are removed from
	 */
	public void deregisterComponent(Component... components)
	{
		for (Component component : components)
		{
			component.removeMouseListener( this );
			component.removeMouseMotionListener( this );
		}
	}

	/**
	 *  Add the required listeners to the specified component
	 *
	 *  @param component  the component the listeners are added to
	 */
	public void registerComponent(Component... components)
	{
		for (Component component : components)
		{
			component.addMouseListener( this );
			component.addMouseMotionListener( this );
		}
	}

	/**
	 *	Get the snap size.
	 *
	 *  @return the snap size.
	 */
	public Dimension getSnapSize()
	{
		return snapSize;
	}

	/**
	 *  Control how many pixels a border must be dragged before the size of
	 *  the component is changed. The border will snap to the size once
	 *  dragging has passed the halfway mark.
	 *
	 *  @param snapSize Dimension object allows you to separately spcify a
	 *                  horizontal and vertical snap size.
	 */
	public void setSnapSize(Dimension snapSize)
	{
		this.snapSize = snapSize;
	}

	/**
	 *  When the components minimum size is less than the drag insets then
	 *	we can't determine which border should be resized so we need to
	 *  prevent this from happening.
	 */
	private void validateMinimumAndInsets(Dimension minimum, Insets drag)
	{
		int minimumWidth = drag.left + drag.right;
		int minimumHeight = drag.top + drag.bottom;

		if (minimum.width  < minimumWidth
		||  minimum.height < minimumHeight)
		{
			String message = "Minimum size cannot be less than drag insets";
			throw new IllegalArgumentException( message );
		}
	}

	/**
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		Component source = e.getComponent();
		Point location = e.getPoint();
		direction = 0;

		if (location.x < dragInsets.left)
			direction += WEST;

		if (location.x > source.getWidth() - dragInsets.right - 1)
			direction += EAST;

		if (location.y < dragInsets.top)
			direction += NORTH;

		if (location.y > source.getHeight() - dragInsets.bottom - 1)
			direction += SOUTH;

		//  Mouse is no longer over a resizable border

		if (direction == 0)
		{
			source.setCursor( sourceCursor );
		}
		else  // use the appropriate resizable cursor
		{
			int cursorType = cursors.get( direction );
			Cursor cursor = Cursor.getPredefinedCursor( cursorType );
			source.setCursor( cursor );
		}
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		if (! resizing)
		{
			Component source = e.getComponent();
			sourceCursor = source.getCursor();
		}
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		if (! resizing)
		{
			Component source = e.getComponent();
			source.setCursor( sourceCursor );
		}
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		//	The mouseMoved event continually updates this variable

		if (direction == 0) return;

		//  Setup for resizing. All future dragging calculations are done based
		//  on the original bounds of the component and mouse pressed location.

		resizing = true;

		Component source = e.getComponent();
		pressed = e.getPoint();
		SwingUtilities.convertPointToScreen(pressed, source);
		bounds = source.getBounds();

		//  Making sure autoscrolls is false will allow for smoother resizing
		//  of components

		if (source instanceof JComponent)
		{
			JComponent jc = (JComponent)source;
			autoscrolls = jc.getAutoscrolls();
			jc.setAutoscrolls( false );
		}
	}

	/**
	 *  Restore the original state of the Component
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		resizing = false;

		Component source = e.getComponent();
		source.setCursor( sourceCursor );

		if (source instanceof JComponent)
		{
			((JComponent)source).setAutoscrolls( autoscrolls );
		}
	}

	/**
	 *  Resize the component ensuring location and size is within the bounds
	 *  of the parent container and that the size is within the minimum and
	 *  maximum constraints.
	 *
	 *  All calculations are done using the bounds of the component when the
	 *  resizing started.
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		if (resizing == false) return;

		Component source = e.getComponent();
		Point dragged = e.getPoint();
		SwingUtilities.convertPointToScreen(dragged, source);

		changeBounds(source, direction, bounds, pressed, dragged);
	}

	protected void changeBounds(Component source, int direction, Rectangle bounds, Point pressed, Point current)
	{
		//  Start with original locaton and size

		int x = bounds.x;
		int y = bounds.y;
		int width = bounds.width;
		int height = bounds.height;

		//  Resizing the West or North border affects the size and location

		if (WEST == (direction & WEST))
		{
			int drag = getDragDistance(pressed.x, current.x, snapSize.width);
			int maximum = Math.min(width + x, maximumSize.width);
			drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);

			x -= drag;
			width += drag;
		}

		if (NORTH == (direction & NORTH))
		{
			int drag = getDragDistance(pressed.y, current.y, snapSize.height);
			int maximum = Math.min(height + y, maximumSize.height);
			drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);

			y -= drag;
			height += drag;
		}

		//  Resizing the East or South border only affects the size

		if (EAST == (direction & EAST))
		{
			int drag = getDragDistance(current.x, pressed.x, snapSize.width);
			Dimension boundingSize = getBoundingSize( source );
			int maximum = Math.min(boundingSize.width - x, maximumSize.width);
			drag = getDragBounded(drag, snapSize.width, width, minimumSize.width, maximum);
			width += drag;
		}

		if (SOUTH == (direction & SOUTH))
		{
			int drag = getDragDistance(current.y, pressed.y, snapSize.height);
			Dimension boundingSize = getBoundingSize( source );
			int maximum = Math.min(boundingSize.height - y, maximumSize.height);
			drag = getDragBounded(drag, snapSize.height, height, minimumSize.height, maximum);
			height += drag;
		}

		source.setBounds(x, y, width, height);
		source.validate();
	}

	/*
	 *  Determine how far the mouse has moved from where dragging started
	 */
	private int getDragDistance(int larger, int smaller, int snapSize)
	{
		int halfway = snapSize / 2;
		int drag = larger - smaller;
		drag += (drag < 0) ? -halfway : halfway;
		drag = (drag / snapSize) * snapSize;

		return drag;
	}

	/*
	 *  Adjust the drag value to be within the minimum and maximum range.
	 */
	private int getDragBounded(int drag, int snapSize, int dimension, int minimum, int maximum)
	{
		while (dimension + drag < minimum)
			drag += snapSize;

		while (dimension + drag > maximum)
			drag -= snapSize;


		return drag;
	}

	/*
	 *  Keep the size of the component within the bounds of its parent.
	 */
	private Dimension getBoundingSize(Component source)
	{
		if (source instanceof Window)
		{
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Rectangle bounds = env.getMaximumWindowBounds();
			return new Dimension(bounds.width, bounds.height);
		}
		else
		{
			return source.getParent().getSize();
		}
	}
}


class ComponentMover extends MouseAdapter
{
	private Insets dragInsets = new Insets(0, 0, 0, 0);
	private Dimension snapSize = new Dimension(1, 1);
	private Insets edgeInsets = new Insets(0, 0, 0, 0);
	private boolean changeCursor = true;
	private boolean autoLayout = false;

	private Class destinationClass;
	private Component destinationComponent;
	private Component destination;
	private Component source;

	private Point pressed;
	private Point location;

	private Cursor originalCursor;
	private boolean autoscrolls;
	private boolean potentialDrag;


	/**
	 *  Constructor for moving individual components. The components must be
	 *  regisetered using the registerComponent() method.
	 */
	public ComponentMover()
	{
	}

	/**
	 *  Constructor to specify a Class of Component that will be moved when
	 *  drag events are generated on a registered child component. The events
	 *  will be passed to the first ancestor of this specified class.
	 *
	 *  @param destinationClass  the Class of the ancestor component
	 *  @param component         the Components to be registered for forwarding
	 *                           drag events to the ancestor Component.
	 */
	public ComponentMover(Class destinationClass, Component... components)
	{
		this.destinationClass = destinationClass;
		registerComponent( components );
	}

	/**
	 *  Constructor to specify a parent component that will be moved when drag
	 *  events are generated on a registered child component.
	 *
	 *  @param destinationComponent  the component drage events should be forwareded to
	 *  @param components    the Components to be registered for forwarding drag
	 *                       events to the parent component to be moved
	 */
	public ComponentMover(Component destinationComponent, Component... components)
	{
		this.destinationComponent = destinationComponent;
		registerComponent( components );
	}

	/**
	 *  Get the auto layout property
	 *
	 *  @return  the auto layout property
	 */
	public boolean isAutoLayout()
	{
		return autoLayout;
	}

	/**
	 *  Set the auto layout property
	 *
	 *  @param  autoLayout when true layout will be invoked on the parent container
	 */
	public void setAutoLayout(boolean autoLayout)
	{
		this.autoLayout = autoLayout;
	}

	/**
	 *  Get the change cursor property
	 *
	 *  @return  the change cursor property
	 */
	public boolean isChangeCursor()
	{
		return changeCursor;
	}

	/**
	 *  Set the change cursor property
	 *
	 *  @param  changeCursor when true the cursor will be changed to the
	 *                       Cursor.MOVE_CURSOR while the mouse is pressed
	 */
	public void setChangeCursor(boolean changeCursor)
	{
		this.changeCursor = changeCursor;
	}

	/**
	 *  Get the drag insets
	 *
	 *  @return  the drag insets
	 */
	public Insets getDragInsets()
	{
		return dragInsets;
	}

	/**
	 *  Set the drag insets. The insets specify an area where mouseDragged
	 *  events should be ignored and therefore the component will not be moved.
	 *  This will prevent these events from being confused with a
	 *  MouseMotionListener that supports component resizing.
	 *
	 *  @param  dragInsets
	 */
	public void setDragInsets(Insets dragInsets)
	{
		this.dragInsets = dragInsets;
	}

	/**
	 *  Get the bounds insets
	 *
	 *  @return  the bounds insets
	 */
	public Insets getEdgeInsets()
	{
		return edgeInsets;
	}

	/**
	 *  Set the edge insets. The insets specify how close to each edge of the parent
	 *  component that the child component can be moved. Positive values means the
	 *  component must be contained within the parent. Negative values means the
	 *  component can be moved outside the parent.
	 *
	 *  @param  edgeInsets
	 */
	public void setEdgeInsets(Insets edgeInsets)
	{
		this.edgeInsets = edgeInsets;
	}

	/**
	 *  Remove listeners from the specified component
	 *
	 *  @param component  the component the listeners are removed from
	 */
	public void deregisterComponent(Component... components)
	{
		for (Component component : components)
			component.removeMouseListener( this );
	}

	/**
	 *  Add the required listeners to the specified component
	 *
	 *  @param component  the component the listeners are added to
	 */
	public void registerComponent(Component... components)
	{
		for (Component component : components)
			component.addMouseListener( this );
	}

	/**
	 *	Get the snap size
	 *
	 *  @return the snap size
	 */
	public Dimension getSnapSize()
	{
		return snapSize;
	}

	/**
	 *  Set the snap size. Forces the component to be snapped to
	 *  the closest grid position. Snapping will occur when the mouse is
	 *  dragged half way.
	 */
	public void setSnapSize(Dimension snapSize)
	{
		if (snapSize.width < 1
		||  snapSize.height < 1)
			throw new IllegalArgumentException("Snap sizes must be greater than 0");

		this.snapSize = snapSize;
	}

	/**
	 *  Setup the variables used to control the moving of the component:
	 *
	 *  source - the source component of the mouse event
	 *  destination - the component that will ultimately be moved
	 *  pressed - the Point where the mouse was pressed in the destination
	 *      component coordinates.
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		source = e.getComponent();
		int width  = source.getSize().width  - dragInsets.left - dragInsets.right;
		int height = source.getSize().height - dragInsets.top - dragInsets.bottom;
		Rectangle r = new Rectangle(dragInsets.left, dragInsets.top, width, height);

		if (r.contains(e.getPoint()))
			setupForDragging(e);
	}

	private void setupForDragging(MouseEvent e)
	{
		source.addMouseMotionListener( this );
		potentialDrag = true;

		//  Determine the component that will ultimately be moved

		if (destinationComponent != null)
		{
			destination = destinationComponent;
		}
		else if (destinationClass == null)
		{
			destination = source;
		}
		else  //  forward events to destination component
		{
			destination = SwingUtilities.getAncestorOfClass(destinationClass, source);
		}

		pressed = e.getLocationOnScreen();
		location = destination.getLocation();

		if (changeCursor)
		{
			originalCursor = source.getCursor();
			source.setCursor( Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR) );
		}

		//  Making sure autoscrolls is false will allow for smoother dragging of
		//  individual components

		if (destination instanceof JComponent)
		{
			JComponent jc = (JComponent)destination;
			autoscrolls = jc.getAutoscrolls();
			jc.setAutoscrolls( false );
		}
	}

	/**
	 *  Move the component to its new location. The dragged Point must be in
	 *  the destination coordinates.
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		Point dragged = e.getLocationOnScreen();
		int dragX = getDragDistance(dragged.x, pressed.x, snapSize.width);
		int dragY = getDragDistance(dragged.y, pressed.y, snapSize.height);

		int locationX = location.x + dragX;
		int locationY = location.y + dragY;

		//  Mouse dragged events are not generated for every pixel the mouse
		//  is moved. Adjust the location to make sure we are still on a
		//  snap value.

		while (locationX < edgeInsets.left)
			locationX += snapSize.width;

		while (locationY < edgeInsets.top)
			locationY += snapSize.height;

		Dimension d = getBoundingSize( destination );

		while (locationX + destination.getSize().width + edgeInsets.right > d.width)
			locationX -= snapSize.width;

		while (locationY + destination.getSize().height + edgeInsets.bottom > d.height)
			locationY -= snapSize.height;

		//  Adjustments are finished, move the component

		destination.setLocation(locationX, locationY);
	}

	/*
	 *  Determine how far the mouse has moved from where dragging started
	 *  (Assume drag direction is down and right for positive drag distance)
	 */
	private int getDragDistance(int larger, int smaller, int snapSize)
	{
		int halfway = snapSize / 2;
		int drag = larger - smaller;
		drag += (drag < 0) ? -halfway : halfway;
		drag = (drag / snapSize) * snapSize;

		return drag;
	}

	/*
	 *  Get the bounds of the parent of the dragged component.
	 */
	private Dimension getBoundingSize(Component source)
	{
		if (source instanceof Window)
		{
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			Rectangle bounds = env.getMaximumWindowBounds();
			return new Dimension(bounds.width, bounds.height);
		}
		else
		{
			return source.getParent().getSize();
		}
	}

	/**
	 *  Restore the original state of the Component
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if (!potentialDrag) return;

		source.removeMouseMotionListener( this );
		potentialDrag = false;

		if (changeCursor)
			source.setCursor( originalCursor );

		if (destination instanceof JComponent)
		{
			((JComponent)destination).setAutoscrolls( autoscrolls );
		}

		//  Layout the components on the parent container

		if (autoLayout)
		{
			if (destination instanceof JComponent)
			{
				((JComponent)destination).revalidate();
			}
			else
			{
				destination.validate();
			}
		}
	}
}
