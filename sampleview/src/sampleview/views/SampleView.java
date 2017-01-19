package sampleview.views;


import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.*;

import insertcode.CodeInsertion;
import lums.cbrs.completion.CodeCompletion;
import sampleview.Activator;
import sampleview.popup.Myform;
import sampleview.popup.Mypopup;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.wb.swt.SWTResourceManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.osgi.framework.Bundle;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "sampleview.views.SampleView";

	private static SampleView view;
	public static TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Action doubleClickActionFriends;
	private static ArrayList<String> methodNames = new ArrayList<String>();
	private static ArrayList<RSyntaxTextArea> methodBodyTextAreasList = new ArrayList<RSyntaxTextArea>();
	public static CTabFolder tabFolder;
	public static Section section;
	FormToolkit toolkit;
	TabItem dummyTab;
	public static Object obj;
	public static TableViewer viewer2;
	public static Label lblFriendMethods;
	private MethodFilter filter;
	private MethodFilter filter2;
	public static Text searchText;
	public static Text methodSearchText;
	public static GridData gd_tabFolder;
	public static GridData gd_table;
	public static Composite composite;
	public static GridLayout gl_composite;
	public static String mode;
	public static String selMethodName;
	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	public ArrayList<String> getMethodNames() {
		return methodNames;
	}

	public void setMethodNames(ArrayList<String> methodNames) {
		this.methodNames = methodNames;
	}

	public class ViewContentProvider implements IStructuredContentProvider {
		private String methodName;
		public ViewContentProvider(String data)
		{
			methodName = data;
			
		}
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			//return new String[] { "One", "Two", "Three" };
			//return methodName;
			CodeCompletion cc = new CodeCompletion();
			methodName = methodName.toLowerCase();
			if(methodName.contains("write"))
			//if(methodName.equalsIgnoreCase("friends"))
			{
				return cc.getReadFileMethodNames().toArray();
			}
			else if(methodName.contains("read"))
			{
			
				return cc.getWriteFileMethodNames().toArray();
			}
			else if(methodName.contains("extract"))
			{
			
				return cc.getExtractFileNameFriendMethodNames().toArray();
			}
			else if(methodName.contains("min"))
			{
			
				return cc.getMinFriendMethodNames().toArray();
			}
			else if(methodName.contains("max"))
			{
			
				return cc.getMaxFriendMethodNames().toArray();
			}
			else
			{
				return null;
			}
			//return methodNames.toArray();
		}
	}
	
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			//platform:/plugin/org.eclipse.xtext.ui/icons/defaultoutlinenode.gif
			//return PlatformUI.getWorkbench().
			//		getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);		
			
			ImageDescriptor descriptor = Activator.getImageDescriptor("platform:/plugin/org.eclipse.xtext.ui/icons/defaultoutlinenode.gif");
	    return descriptor.createImage();
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SampleView() {
		view = this;
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	/*public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "sampleview.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}
*/
	public static ArrayList<String> fileRead(String pathName){
		File f;
		String s;
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> sl = new ArrayList<String>();
		try {
			//Class.getResource(fileName);
			f = new File(pathName);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			while ((s = br.readLine()) != null) {
				sl.add(s);
				System.out.println(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sl;
	}
	public void createPartControl(Composite parent) {
		
		try{
			toolkit = new FormToolkit(parent.getDisplay());      
			//Composite container = new Composite(parent, SWT.NONE);
			//Myform form = new Myform("1","TestForm");
			//container.setLayoutData(form);
			//container.setLayout(new FillLayout(SWT.VERTICAL));
			{
			section = toolkit.createSection(parent, Section.COMPACT );
			//section.setText("Method Completion Recommendations");
			//section.setDescription("3 results found");

			composite = new Composite(section, SWT.NONE);
			GridLayout gl_composite = new GridLayout(2, false);
			composite.setLayout(gl_composite);		 
			section.setClient(composite);
			
			
			methodSearchText = toolkit.createText(composite, "New Text", SWT.BORDER);
			GridData gd_methodSearchText = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_methodSearchText.minimumWidth = 180;
			gd_methodSearchText.widthHint = 210;
			methodSearchText.setLayoutData(gd_methodSearchText);
			methodSearchText.setBackground(SWTResourceManager.getColor(255, 255, 204));
			methodSearchText.setText("");
			methodSearchText.setMessage("Enter filter text here");
			//viewer.setContentProvider(new ViewContentProvider(""));
			//viewer.setLabelProvider(new ViewLabelProvider());        
			//viewer.setInput(getViewSite());
			
		

			tabFolder = new CTabFolder(composite, SWT.NONE);
			gd_tabFolder = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 5);
			gd_tabFolder.heightHint = 50;
			gd_tabFolder.minimumWidth = 100;
			gd_tabFolder.widthHint = 100;
			tabFolder.setLayoutData(gd_tabFolder);
			CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
			tabItem_1.setShowClose(true);
			//tabItem_1.setText("New Item");
			Composite composite_1 = new Composite(tabFolder, SWT.EMBEDDED | SWT.NO_BACKGROUND);
			tabItem_1.setControl(composite_1);
			toolkit.paintBordersFor(composite_1);
			composite_1.setLayout(new GridLayout(1, false));
			//Composite composite = new Composite(container, SWT.EMBEDDED | SWT.NO_BACKGROUND);
			Frame frame = SWT_AWT.new_Frame(composite_1);
			RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
			textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			textArea.setCodeFoldingEnabled(true);
			RTextScrollPane sp = new RTextScrollPane(textArea);
			frame.add(sp);
			toolkit.adapt(composite_1);
			toolkit.paintBordersFor(composite_1);
			
			//TextViewer textViewer = new TextViewer(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
			//StyledText styledText = textViewer.getTextWidget();
			//styledText.setEditable(false);
			//GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
			//gd_styledText.heightHint = 294;		 
			//styledText.setLayoutData(gd_styledText);
			methodBodyTextAreasList.add(textArea);			
			//toolkit.paintBordersFor(styledText);
			tabFolder.setSelection(tabItem_1);
			methodSearchText.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent ke) {
                        filter.setSearchText(methodSearchText.getText());
                        viewer.refresh();
                }

        });
			 //filter = new MethodFilter();
             //viewer.addFilter(filter);
             
			viewer = new TableViewer(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
			Table table = viewer.getTable();
			gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_table.widthHint = 200;
			gd_table.heightHint = 100;
			table.setLayoutData(gd_table);
			 
			 filter = new MethodFilter();
             viewer.addFilter(filter);
			
			lblFriendMethods = new Label(composite, SWT.WRAP);
			lblFriendMethods.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
			lblFriendMethods.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
			GridData gd_lblFriendMethods = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_lblFriendMethods.widthHint = 180;
			lblFriendMethods.setLayoutData(gd_lblFriendMethods);
			toolkit.adapt(lblFriendMethods, true, true);
			lblFriendMethods.setText("Friend Methods");
			lblFriendMethods.setVisible(false);
			
			searchText = toolkit.createText(composite, "New Text", SWT.BORDER);
			searchText.setBackground(SWTResourceManager.getColor(255, 255, 204));
			searchText.setToolTipText("Filter a specific method name");
			searchText.setText("");
			searchText.setMessage("Enter filter text here");
			GridData gd_searchText2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
			gd_searchText2.widthHint = 210;
			searchText.setLayoutData(gd_searchText2);
			
			 searchText.addKeyListener(new KeyAdapter() {
                 public void keyReleased(KeyEvent ke) {
                         filter2.setSearchText(searchText.getText());
                         viewer2.refresh();
                 }

         });

			viewer2 = new TableViewer(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
			Table table2 = viewer2.getTable();
			GridData gd_table2 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
			gd_table2.widthHint = 0;
			gd_table2.heightHint = 0;
			table2.setLayoutData(gd_table2);
			
			 filter2 = new MethodFilter();
             viewer2.addFilter(filter2);
             
			//viewer2.setContentProvider(new ViewContentProvider("friends"));
			//viewer2.setLabelProvider(new ViewLabelProvider());        
			//viewer2.setInput(getViewSite());
			obj = getViewSite();
			}
			
			Button btnInsertCode = new Button(section, SWT.NONE);
			btnInsertCode.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					System.out.println("Insert Code clicked");
					int tabIndex = tabFolder.getSelectionIndex();
					CodeInsertion ci = new CodeInsertion();
					ci.performInsert(methodBodyTextAreasList.get(tabIndex).getText());
					
					//Mypopup window = new Mypopup();		
					//window.setBlockOnOpen(true);
					//window.open();
				}
			});
			toolkit.adapt(btnInsertCode, true, true);
			//section.setTextClient(btnInsertCode);
			btnInsertCode.setText("Insert Code");
			//tbtmNewItem.setData(data);

			
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "sampleview.viewer");
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();

		}catch(Exception ex)
		{
			System.out.println("Exception in SampleView createPart");
		}
	
	}
	
	
	//end of createpartcontrol
	public TableViewer getViewer() {
		return viewer;
	}

	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SampleView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				//showMessage("Action 1 executed");
				System.out.println("Insert Code clicked");
				int tabIndex = tabFolder.getSelectionIndex();
				CodeInsertion ci = new CodeInsertion();
				ci.performInsert(methodBodyTextAreasList.get(tabIndex).getText());
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Insert Code");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJ_ADD));
		
		action2 = new Action() {
			public void run() {
				//showMessage("Action 2 executed");
				
			}
		};
		action2.setText("Undo Insert");
		action2.setToolTipText("Undo Insert");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		
		
		doubleClickAction = new Action() {
			public void run() {

				ISelection selection = viewer.getSelection();
				int selectedMethodNameIndex = viewer.getTable().getSelectionIndex();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String extractedMethodName = extractMethodName(obj.toString());
				//showMessage("Double-click detected on "+obj.toString());
				if(mode.equalsIgnoreCase("Completion"))
				{
					
								
				lblFriendMethods.setVisible(true);

				viewer2.setContentProvider(new ViewContentProvider(selMethodName));
				viewer2.setLabelProvider(new ViewLabelProvider());        
				viewer2.setInput(getViewSite());
				
				searchText.setVisible(true);
				searchText.getParent().layout();
				
				viewer2.getTable().setVisible(true);
				viewer2.getTable().getParent().layout();
				
				lblFriendMethods.setVisible(true);
				lblFriendMethods.getParent().layout();				
				}
				CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
				tabItem_1.setText(extractedMethodName);
				tabItem_1.setShowClose(true);

				Composite composite_1 = new Composite(tabFolder, SWT.EMBEDDED | SWT.NO_BACKGROUND);
				tabItem_1.setControl(composite_1);

				toolkit.paintBordersFor(composite_1);
				composite_1.setLayout(new GridLayout(1, false));
				/*

				TextViewer textViewer = new TextViewer(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
				StyledText styledText = textViewer.getTextWidget();
				styledText.setEditable(false);
				GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				gd_styledText.heightHint = 50;		 
				styledText.setLayoutData(gd_styledText);
				methodBodyTextAreasList.add(styledText);	

				styledText.setText(obj.toString());//read file 
				toolkit.paintBordersFor(styledText);
				tabFolder.setSelection(tabItem_1);
				*/
				Frame frame = SWT_AWT.new_Frame(composite_1);
				RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
				textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
				textArea.setCodeFoldingEnabled(true);
				RTextScrollPane sp = new RTextScrollPane(textArea);
				frame.add(sp);
				toolkit.adapt(composite_1);
				toolkit.paintBordersFor(composite_1);
				String methodBody = "";
				CodeCompletion cc = new CodeCompletion();
				//int tabIndex = tabFolder.getSelectionIndex();
				ArrayList<String> bodyList;
				
				bodyList = getAppropriateMethodBodyForViewer1();
				
				for (String temp : bodyList) {
					
					methodBody += temp + "\r\n";
				}
				
				textArea.setText(methodBody);
				methodBodyTextAreasList.add(textArea);	
				tabFolder.setSelection(tabItem_1);
			}

			private String extractMethodName(String methodSignature) {
				
				int openParanthesisIndex = methodSignature.indexOf("(", 0);
				String partialMethodSignature = methodSignature.substring(0, openParanthesisIndex-1);
				int partialMethodSignatureLastSpaceIndex = partialMethodSignature.lastIndexOf(" ");				
				return methodSignature.substring(partialMethodSignatureLastSpaceIndex+1,openParanthesisIndex);
			}
		};
		
		doubleClickActionFriends = new Action() {
			public void run() {

				ISelection selection = viewer2.getSelection();
				int selectedMethodNameIndex = viewer.getTable().getSelectionIndex();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String extractedMethodName = extractMethodName(obj.toString());
				
				
				
				//showMessage("Double-click detected on "+obj.toString());
				
				CTabItem tabItem_1 = new CTabItem(tabFolder, SWT.NONE);
				tabItem_1.setText(extractedMethodName);
				tabItem_1.setShowClose(true);

				Composite composite_1 = new Composite(tabFolder, SWT.EMBEDDED | SWT.NO_BACKGROUND);
				tabItem_1.setControl(composite_1);

				toolkit.paintBordersFor(composite_1);
				composite_1.setLayout(new GridLayout(1, false));
				/*

				TextViewer textViewer = new TextViewer(composite_1, SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
				StyledText styledText = textViewer.getTextWidget();
				styledText.setEditable(false);
				GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
				gd_styledText.heightHint = 50;		 
				styledText.setLayoutData(gd_styledText);
				methodBodyTextAreasList.add(styledText);	

				styledText.setText(obj.toString());//read file 
				toolkit.paintBordersFor(styledText);
				tabFolder.setSelection(tabItem_1);
				*/
				Frame frame = SWT_AWT.new_Frame(composite_1);
				RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
				textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
				textArea.setCodeFoldingEnabled(true);
				RTextScrollPane sp = new RTextScrollPane(textArea);
				frame.add(sp);
				toolkit.adapt(composite_1);
				toolkit.paintBordersFor(composite_1);
				String methodBody = "";
				
				//int tabIndex = tabFolder.getSelectionIndex();
				
				ArrayList<String> bodyList = getAppropriateMethodBodyForViewer2();
						//getFileWriteMethodBody(viewer2.getTable().getSelectionIndex()+1);
				for (String temp : bodyList) {
					methodBody += temp + "\r\n";
				}
				textArea.setText(methodBody);
				methodBodyTextAreasList.add(textArea);	
				tabFolder.setSelection(tabItem_1);
			}

			private String extractMethodName(String methodSignature) {
				int openParanthesisIndex = methodSignature.indexOf("(", 0);
				String partialMethodSignature = methodSignature.substring(0, openParanthesisIndex-1);
				int partialMethodSignatureLastSpaceIndex = partialMethodSignature.lastIndexOf(" ");				
				return methodSignature.substring(partialMethodSignatureLastSpaceIndex+1,openParanthesisIndex);
			}
		};
	}

	public ArrayList<String> getAppropriateMethodBodyForViewer1()
	{
		selMethodName = selMethodName.toLowerCase();
		CodeCompletion cc = new CodeCompletion();
		if(mode.equalsIgnoreCase("Friends"))
		{
			if(selMethodName.contains("write"))
			//if(methodName.equalsIgnoreCase("friends"))
			{
				return cc.getFileReadMethodBody(viewer.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("read"))
			{
			
				return cc.getFileWriteMethodBody(viewer.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("path"))
			{
			
				return cc.getExtractFileNameFriendMethodBody(viewer.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("min"))
			{
			
				return cc.getMinFriendMethodBody(viewer.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("max"))
			{
			
				return cc.getMaxFriendMethodBody(viewer.getTable().getSelectionIndex()+1);
			}
			else
				return null;
		}
		else
		{
			if(selMethodName.contains("write"))
				//if(methodName.equalsIgnoreCase("friends"))
				{
					return cc.getFileWriteMethodBody(viewer.getTable().getSelectionIndex()+1);
				}
				else if(selMethodName.contains("read"))
				{
				
					return cc.getFileReadMethodBody(viewer.getTable().getSelectionIndex()+1);
				}
				else if(selMethodName.contains("extract"))
				{
				
					return cc.getExtractFileNameMethodBody(viewer.getTable().getSelectionIndex()+1);
				}
				else if(selMethodName.contains("min"))
				{
				
					return cc.getMinMethodBody(viewer.getTable().getSelectionIndex()+1);
				}
				else if(selMethodName.contains("max"))
				{
				
					return cc.getMaxMethodBody(viewer.getTable().getSelectionIndex()+1);
				}
				else
					return null;
			
		}
	}
	
	public ArrayList<String> getAppropriateMethodBodyForViewer2()
	{
		CodeCompletion cc = new CodeCompletion();
		selMethodName = selMethodName.toLowerCase();
		if(mode.equalsIgnoreCase("Friends"))
		{
			if(selMethodName.contains("write"))
			//if(methodName.equalsIgnoreCase("friends"))
			{
				return cc.getFileReadMethodBody(viewer2.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("read"))
			{			
				return cc.getFileWriteMethodBody(viewer2.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("extract"))
			{
			
				return cc.getExtractFileNameFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("min"))
			{			
				return cc.getMinFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
			}
			else if(selMethodName.contains("max"))
			{			
				return cc.getMaxFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
			}
			else return null;
		}
		else
		{
			
				
					if(selMethodName.contains("write"))
					//if(methodName.equalsIgnoreCase("friends"))
					{
						return cc.getFileReadMethodBody(viewer2.getTable().getSelectionIndex()+1);
					}
					else if(selMethodName.contains("read"))
					{
					
						return cc.getFileWriteMethodBody(viewer2.getTable().getSelectionIndex()+1);
					}
					else if(selMethodName.contains("extract"))
					{
					
						return cc.getExtractFileNameFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
					}
					else if(selMethodName.contains("min"))
					{
					
						return cc.getMinFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
					}
					else if(selMethodName.contains("max"))
					{
					
						return cc.getMaxFriendMethodBody(viewer2.getTable().getSelectionIndex()+1);
					}
					else
						return null;
			
		}
	}
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
		viewer2.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickActionFriends.run();
			}
		});
	}
	private void showMessage(String message) {
		
		//methodBodyTextArea.setText(message);
	
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	
	
}