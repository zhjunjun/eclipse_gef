package tutogef.part.tree;

import java.beans.PropertyChangeEvent;
import java.util.List;

import tutogef.model.Entreprise;
import tutogef.model.Node;

public class EntrepriseTreeEditPart extends AppAbstractTreeEditPart {


	protected List<Node> getModelChildren() {
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
