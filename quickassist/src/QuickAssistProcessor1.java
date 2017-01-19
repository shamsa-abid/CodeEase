import java.awt.List;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.jdt.ui.text.java.correction.ChangeCorrectionProposal;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.internal.ui.text.correction.AssistContext;
import org.eclipse.jdt.internal.ui.text.java.AbstractJavaCompletionProposal;
import org.eclipse.jdt.ui.actions.AddGetterSetterAction;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickAssistProcessor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import code_ease.handlers.Complete_methods_class;
import code_ease.handlers.Friend_methods_class;
import lums.cbrs.completion.CodeCompletion;
import sampleview.Activator;
import sampleview.popup.Mypopup;
import sampleview.views.SampleView;

public class QuickAssistProcessor1 implements IQuickAssistProcessor {

	@Override
	public boolean hasAssists(IInvocationContext context) throws CoreException {
		ASTNode coveredNode = context.getCoveredNode();

		return coveredNode.getNodeType() == ASTNode.TYPE_DECLARATION;
	}

	@Override
	public IJavaCompletionProposal[] getAssists(IInvocationContext context, IProblemLocation[] locations)
			throws CoreException {
		ArrayList<IJavaCompletionProposal> proposals = new ArrayList<IJavaCompletionProposal>();
		proposals.add(new AbstractJavaCompletionProposal() {
             public org.eclipse.jface.viewers.StyledString getStyledDisplayString() {
                     ICompilationUnit compilationUnit = context.getCompilationUnit();
                     return new StyledString(
                                     "Complete Method");
                     //+ compilationUnit.findPrimaryType().getElementName());
             }

             protected int getPatternMatchRule(String pattern, String string) {
                     // override the match rule since we do not work with a pattern, but just want to open the "Generate Getters and Setters..." dialog
                     return -1;
             };

             public void apply(org.eclipse.jface.text.ITextViewer viewer, char trigger, int stateMask, int offset) {
/*
                     if(context instanceof AssistContext) {
                             AssistContext assistContext = (AssistContext) context;
                             AddGetterSetterAction addGetterSetterAction = new AddGetterSetterAction((CompilationUnitEditor)assistContext.getEditor());

                             addGetterSetterAction.run();
                     }
                     
                     
                     
                     
*/
            	 String selectedText = null;
            	 
            	 try {
         			//get editor
         			IEditorPart editorPart = Activator.getDefault().getWorkbench()
         					.getActiveWorkbenchWindow().getActivePage()
         					.getActiveEditor();
          
         			if (editorPart instanceof AbstractTextEditor) {
         				
         				int offset1 = 0;
         				int length = 0;	
         				IEditorSite iEditorSite = editorPart.getEditorSite();
         				
         				if (iEditorSite != null) {
         					//get selection provider
         					ISelectionProvider selectionProvider = iEditorSite
         							.getSelectionProvider();
         					
         					if (selectionProvider != null) {
         						ISelection iSelection = selectionProvider
         								.getSelection();
         						//offset
         						offset1 = ((ITextSelection) iSelection).getOffset();
         						
         						if (!iSelection.isEmpty()) {
         							selectedText = ((ITextSelection) iSelection)
         									.getText();		
         						}
         					}
         				}
          
         			}
         		} catch (Exception e) {
         			System.out.println("Exception in SimpleMessageAction run()");
         		}
            	 
            	 
            	 
            	
            	 
            	 Complete_methods_class cm = new Complete_methods_class(selectedText);
             }
								
							public Image getImage()
							{
								ImageDescriptor descriptor = Activator.getImageDescriptor("platform:/plugin/org.eclipse.xtext.ui/icons/defaultoutlinenode.gif");
							    return descriptor.createImage();
								
							}

							public int getRelevance()
							{
								return 1;
							}
						
		});
		
		proposals.add(new AbstractJavaCompletionProposal() {
            public org.eclipse.jface.viewers.StyledString getStyledDisplayString() {
                ICompilationUnit compilationUnit = context.getCompilationUnit();
                return new StyledString(
                                "Find Friends");
                //+ compilationUnit.findPrimaryType().getElementName());
        }

        protected int getPatternMatchRule(String pattern, String string) {
                // override the match rule since we do not work with a pattern, but just want to open the "Generate Getters and Setters..." dialog
                return -1;
        };

        public void apply(org.eclipse.jface.text.ITextViewer viewer, char trigger, int stateMask, int offset) {
/*
                if(context instanceof AssistContext) {
                        AssistContext assistContext = (AssistContext) context;
                        AddGetterSetterAction addGetterSetterAction = new AddGetterSetterAction((CompilationUnitEditor)assistContext.getEditor());

                        addGetterSetterAction.run();
                }
*/
        	
        	
        	 String selectedText = null;
        	 
        	 try {
     			//get editor
     			IEditorPart editorPart = Activator.getDefault().getWorkbench()
     					.getActiveWorkbenchWindow().getActivePage()
     					.getActiveEditor();
      
     			if (editorPart instanceof AbstractTextEditor) {
     				
     				int offset1 = 0;
     				int length = 0;	
     				IEditorSite iEditorSite = editorPart.getEditorSite();
     				
     				if (iEditorSite != null) {
     					//get selection provider
     					ISelectionProvider selectionProvider = iEditorSite
     							.getSelectionProvider();
     					
     					if (selectionProvider != null) {
     						ISelection iSelection = selectionProvider
     								.getSelection();
     						//offset
     						offset1 = ((ITextSelection) iSelection).getOffset();
     						
     						if (!iSelection.isEmpty()) {
     							selectedText = ((ITextSelection) iSelection)
     									.getText();		
     						}
     					}
     				}
      
     			}
     		} catch (Exception e) {
     			System.out.println("Exception in SimpleMessageAction run()");
     		}
        	 
        	 
        	
        	
        	Friend_methods_class fmc = new Friend_methods_class(selectedText);
			
        }
							
						public Image getImage()
						{
							ImageDescriptor descriptor = Activator.getImageDescriptor("platform:/plugin/org.eclipse.xtext.ui/icons/defaultoutlinenode.gif");
						    return descriptor.createImage();
							
						}

						public int getRelevance()
						{
							return 2;
						}
					
	});
		
		return proposals.toArray(new IJavaCompletionProposal[proposals.size()]);
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
			if(methodName.equalsIgnoreCase("friends"))
			{
				return cc.getWriteFileMethodNames().toArray();
			}
			else
			{
			
			return cc.getReadFileMethodNames().toArray();
			}
		}
	}
	
}


	/*
return new IJavaCompletionProposal[] { new AbstractJavaCompletionProposal() {
             public org.eclipse.jface.viewers.StyledString getStyledDisplayString() {
                     ICompilationUnit compilationUnit = context.getCompilationUnit();
                     return new StyledString(
                                     "Complete Method");
                     //+ compilationUnit.findPrimaryType().getElementName());
             }

             protected int getPatternMatchRule(String pattern, String string) {
                     // override the match rule since we do not work with a pattern, but just want to open the "Generate Getters and Setters..." dialog
                     return -1;
             };

             public void apply(org.eclipse.jface.text.ITextViewer viewer, char trigger, int stateMask, int offset) {

                     if(context instanceof AssistContext) {
                             AssistContext assistContext = (AssistContext) context;
                             AddGetterSetterAction addGetterSetterAction = new AddGetterSetterAction((CompilationUnitEditor)assistContext.getEditor());

                             addGetterSetterAction.run();
                     }

             }
								
							public Image getImage()
							{
								ImageDescriptor descriptor = Activator.getImageDescriptor("platform:/plugin/org.eclipse.xtext.ui/icons/defaultoutlinenode.gif");
							    return descriptor.createImage();
								
							}

						
		}};
*/