package tutogef.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import tutogef.editpolicies.AppNodeEditPolicy;
import tutogef.model.Node;

public abstract class AppAbstractEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener,NodeEditPart {
	
	public void activate() {
		super.activate();
		((Node) getModel()).addPropertyChangeListener(this);
	}

	public void deactivate() {
		super.deactivate();
		((Node) getModel()).removePropertyChangeListener(this);
	}
	
	@Override
	public void performRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			try {
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				page.showView(IPageLayout.ID_PROP_SHEET);
			}
			catch (PartInitException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public List getSourceConnections() {
		return super.getSourceConnections();
	}
	
	//------------------------------------------------------------------------
    // 连接有关
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }
    
    protected List getModelSourceConnections() {
		return ((Node) this.getModel()).getOutgoingConnections();
	}

	protected List getModelTargetConnections() {
		return ((Node) this.getModel()).getIncomingConnections();
	}
	
}
