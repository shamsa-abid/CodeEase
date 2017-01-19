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



public class SimpleMessageAction implements IObjectActionDelegate {

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public SimpleMessageAction() {
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
						
							SampleView.mode = "Completion";
							SampleView.section.setText("Method Completion Recommendations");
							//SampleView.section.layout(true);
							
							SampleView.gd_tabFolder.verticalSpan = 5;
							SampleView.tabFolder.setLayoutData(SampleView.gd_tabFolder);
							//SampleView.tabFolder.redraw();
							SampleView.tabFolder.layout(true);							
						
							
							SampleView.gd_table.grabExcessVerticalSpace = false;
							SampleView.viewer.refresh();
							SampleView.viewer.getTable().getParent().layout();
							
							SampleView.searchText.setVisible(true);
							SampleView.searchText.getParent().layout();
							//SampleView.searchText.dispose();
							//SampleView.searchText.getParent().pack();
							
							SampleView.viewer2.getTable().setVisible(true);
							SampleView.viewer2.getTable().getParent().layout();
							//SampleView.viewer2.getTable().dispose();
							//SampleView.viewer2.getTable().getParent().pack();
							
							SampleView.lblFriendMethods.setVisible(true);
							//SampleView.lblFriendMethods.getParent().pack();
							SampleView.lblFriendMethods.getParent().layout();
							//SampleView.lblFriendMethods.dispose();
							
							SampleView.selMethodName = selectedText;
							SampleView.mode = "Completion";
							
							SampleView.viewer.setContentProvider(new ViewContentProvider(selectedText));
							SampleView.viewer.setLabelProvider(new ViewLabelProvider());        
							SampleView.viewer.setInput(SampleView.obj);
							
							//make sure the viewer2 is refreshed
							SampleView.viewer2.setContentProvider(new ViewContentProvider("empty"));
							//SampleView.viewer2.setLabelProvider(new ViewLabelProvider());        
							SampleView.viewer2.setInput(null);
							SampleView.viewer2.refresh();
							SampleView.viewer2.getTable().getParent().layout();
							//SampleView.gl_composite.verticalSpacing = 2;
							//SampleView.composite.setLayout(SampleView.gl_composite);
							//SampleView.composite.layout(true);
							
							
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
			System.out.println("Exception in SimpleMessageAction run()");
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
		//private String selectedText;
		
		public ViewContentProvider(String data)
		{
			methodName = data;
		}
		
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		
		//add the logic to get teh appropriate recommendations 
		public Object[] getElements(Object parent) {
			CodeCompletion cc = new CodeCompletion();
			methodName = methodName.toLowerCase();
			if(methodName.contains("write"))
			//if(methodName.equalsIgnoreCase("friends"))
			{
				return cc.getWriteFileMethodNames().toArray();
			}
			else if(methodName.contains("read"))
			{
			
				return cc.getReadFileMethodNames().toArray();
			}
			else if(methodName.contains("extract"))
			{
			
				return cc.getExtractFileNameMethodNames().toArray();
			}
			else if(methodName.contains("min"))
			{
	
				return cc.getMinMethodNames().toArray();
			}
			else //if(methodName.contains("max"))
			{
			
				return cc.getMaxMethodNames().toArray();
			}
		}
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
