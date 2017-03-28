package tutogef.part;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import tutogef.editpolicies.AppEditLayoutPolicy;
import tutogef.figure.EntrepriseFigure;
import tutogef.model.Entreprise;
import tutogef.model.Node;

public class EntreprisePart extends AppAbstractEditPart{

	// 返回对应的Figure
	@Override
	protected IFigure createFigure() {
		IFigure figure = new EntrepriseFigure();
		return figure;
	}

	// 注册commond 代理
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new AppEditLayoutPolicy());
	}

	// 刷新figure 和 model 
	protected void refreshVisuals(){ 
		EntrepriseFigure figure = (EntrepriseFigure)getFigure();
		Entreprise model = (Entreprise)getModel();

		figure.setName(model.getName());
		figure.setAddress(model.getAddress());
		figure.setCapital(model.getCapital());
	}
	
 	public List<Node> getModelChildren() {
 		return ((Entreprise)getModel()).getChildrenArray();
 	}

	//@Override
	public void propertyChange(PropertyChangeEvent evt) {
	    if (evt.getPropertyName().equals(Node.PROPERTY_ADD)) refreshChildren();
	    if (evt.getPropertyName().equals(Node.PROPERTY_REMOVE)) refreshChildren();
	    if (evt.getPropertyName().equals(Node.PROPERTY_RENAME)) refreshVisuals();
	    if (evt.getPropertyName().equals(Entreprise.PROPERTY_CAPITAL)) refreshVisuals();
	}
}
