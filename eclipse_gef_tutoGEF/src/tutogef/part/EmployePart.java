package tutogef.part;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;

import tutogef.editpolicies.AppDeletePolicy;
import tutogef.editpolicies.AppNodeEditPolicy;
import tutogef.figure.EmployeFigure;
import tutogef.model.Employe;
import tutogef.model.Node;

public class EmployePart extends AppAbstractEditPart{

	@Override
	protected IFigure createFigure() {
		IFigure figure = new EmployeFigure();
		return figure;
	}
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new AppDeletePolicy());
		//---------------------------连接相关-------------------------------
		// 注册 创建连接命令代理
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,new AppNodeEditPolicy());
	}
	
	protected void refreshVisuals(){
		
		EmployeFigure figure = (EmployeFigure)getFigure();
		Employe model = (Employe)getModel();
		figure.setName(model.getName());
		figure.setFirstName(model.getPrenom());
		figure.setLayout(model.getLayout());
	}

 	public List<Node> getModelChildren() {
 		return new ArrayList<Node>();
 	}

 	// 连接相关
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Node.PROPERTY_LAYOUT)){
			refreshVisuals();
		}else if(evt.getPropertyName().equals(Node.PROP_INPUTS)) {
			refreshTargetConnections();
		}else if(evt.getPropertyName().equals(Node.PROP_OUTPUTS)){
			refreshSourceConnections();
		}
	}
	
}
