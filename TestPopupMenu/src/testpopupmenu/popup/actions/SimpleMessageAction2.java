package testpopupmenu.popup.actions;

import java.util.ArrayList;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import lums.cbrs.completion.CodeCompletion;
import sampleview.popup.Mypopup;

import sampleview.views.SampleView;
import sampleview.views.SampleView.ViewContentProvider;
import testpopupmenu.Activator;
import testpopupmenu.popup.actions.SimpleMessageAction.ViewLabelProvider;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;



public class SimpleMessageAction2 implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public SimpleMessageAction2() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		/*MessageDialog.openInformation(
			shell,
			"TestPopupMenu",
			"SimpleMessageAction was executed.");*/
		try {
			//get editor
			IEditorPart editorPart = Activator.getDefault().getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getActiveEditor();
 
			if (editorPart instanceof AbstractTextEditor) {
				
				int offset = 0;
				int length = 0;
				String selectedText = null;
				IEditorSite iEditorSite = editorPart.getEditorSite();
				
				if (iEditorSite != null) {
					//get selection provider
					ISelectionProvider selectionProvider = iEditorSite
							.getSelectionProvider();
					
					if (selectionProvider != null) {
						ISelection iSelection = selectionProvider
								.getSelection();
						//offset
						offset = ((ITextSelection) iSelection).getOffset();
						
						if (!iSelection.isEmpty()) {
							selectedText = ((ITextSelection) iSelection)
									.getText();
							//length
							length = ((ITextSelection) iSelection).getLength();
							System.out.println("length: " + length);							
						
							
							/*MessageDialog.openInformation(
									shell,
									"Hellopopuptest",
									"shamsa testing.");*/
							
						
							SampleView.section.setText("Friend Recommendations");	
							SampleView.section.layout(true);
							
							SampleView.gd_tabFolder.verticalSpan = 2;
							SampleView.tabFolder.setLayoutData(SampleView.gd_tabFolder);
							SampleView.tabFolder.redraw();
							SampleView.tabFolder.layout(true);
							
							SampleView.searchText.setVisible(false);
							SampleView.searchText.getParent().layout();
							//SampleView.searchText.dispose();
							//SampleView.searchText.getParent().pack();
							
							SampleView.viewer2.getTable().setVisible(false);
							SampleView.viewer2.getTable().getParent().layout();
							//SampleView.viewer2.getTable().dispose();
							//SampleView.viewer2.getTable().getParent().pack();
							
							SampleView.lblFriendMethods.setVisible(false);
							//SampleView.lblFriendMethods.getParent().pack();
							SampleView.lblFriendMethods.getParent().layout();
							//SampleView.lblFriendMethods.dispose();
							
							
							
							
							SampleView.gd_table.grabExcessVerticalSpace = true;							
							SampleView.viewer.getTable().layout();
							
							SampleView.mode = "Friends";		
							SampleView.selMethodName = selectedText;
							SampleView.viewer.setContentProvider(new ViewContentProvider(selectedText));
							SampleView.viewer.setLabelProvider(new ViewLabelProvider());        
							SampleView.viewer.setInput(SampleView.obj);
							
							
							//SampleView.composite.setLayout(SampleView.gl_composite);
							//SampleView.composite.pack();
							//SampleView.composite.layout(true);
							
							//SampleView.viewer.setContentProvider(new ViewContentProvider(""));
							//SampleView.viewer.setLabelProvider(new ViewLabelProvider());        
							//SampleView.viewer.setInput(SampleView.obj);
							
							//Display.getCurrent().dispose();
							 //MessageDialog.openInformation(
							   //      shell,
							     //    "Do Something Menu",
							       //  "Length: " + length + "    Offset: " + offset);
						}
					}
				}
 
			}
		} catch (Exception e) {
			System.out.println("Exception in SimpleMessageAction2 run() "+ e.toString());
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
	class ViewContentProvider implements IStructuredContentProvider {
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
				return null;
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
