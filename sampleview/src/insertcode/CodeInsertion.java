package insertcode;

import org.eclipse.jdt.internal.ui.text.template.contentassist.TemplateProposal;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.templates.DocumentTemplateContext;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateContext;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import sampleview.Activator;



public class CodeInsertion extends AbstractTextEditor{

	public void performInsert(){
		
		AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) Activator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();

		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		// You can generate template dynamically here!
		Template template = new Template("sample", 
		        "sample description", 
		        "no-context", 
		        "private void name(){\r\n" + 
		        "\tSystem.out.println(\"name\");\r\n"
		        + "}\r\n", true);

		IRegion region = new Region(range.x, range.y);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext ctx =
		    new DocumentTemplateContext(contextType, 
		        sourceViewer.getDocument(), 
		        range.x, 
		        range.y);

		TemplateProposal proposal 
		    = new TemplateProposal(template, ctx, region, null);

		proposal.apply(sourceViewer, (char) 0, 0, 0);
	}
	
public void performInsert(String methodBody){
		
		/*AbstractTextEditor activeEditor = 
		        (AbstractTextEditor) Activator.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();*/
		
		Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		    	AbstractTextEditor activeEditor = 
				        (AbstractTextEditor) Activator.getDefault().getWorkbench()
						.getActiveWorkbenchWindow().getActivePage()
						.getActiveEditor();
		    

		ISourceViewer sourceViewer = 
		        (ISourceViewer) activeEditor.getAdapter(ITextOperationTarget.class);

		Point range = sourceViewer.getSelectedRange();

		// You can generate template dynamically here!
		Template template = new Template("sample", 
		        "sample description", 
		        "no-context", 
		       methodBody, true);

		IRegion region = new Region(range.x, range.y);
		TemplateContextType contextType = new TemplateContextType("test");
		TemplateContext ctx =
		    new DocumentTemplateContext(contextType, 
		        sourceViewer.getDocument(), 
		        range.x, 
		        range.y);

		TemplateProposal proposal 
		    = new TemplateProposal(template, ctx, region, null);

		proposal.apply(sourceViewer, (char) 0, 0, 0);
		
		    }
		});
	}
}
