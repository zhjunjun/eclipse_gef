package tutogef.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertySource;
import tutogef.model.property.NodePropertySource;

public class Node implements IAdaptable {

	private String name;
	private Rectangle layout;
	private List<Node> children;
	private Node parent;
	private PropertyChangeSupport listeners;
	private IPropertySource propertySource;

	public static final String PROPERTY_LAYOUT = "NodeLayout";
	public static final String PROPERTY_ADD = "NodeAddChild";
	public static final String PROPERTY_REMOVE = "NodeRemoveChild";
	public static final String PROPERTY_RENAME = "NodeRename";

	// ------------------------------------------------------
	// 连接有关
	final public static String PROP_INPUTS = "INPUTS";
	final public static String PROP_OUTPUTS = "OUTPUTS";

	protected List outputs = new ArrayList(5);
	protected List inputs = new ArrayList(5);

	public void addInput(Connection connection) {
		this.inputs.add(connection);
		fireStructureChange(PROP_INPUTS, connection);
	}

	public void addOutput(Connection connection) {
		this.outputs.add(connection);
		fireStructureChange(PROP_OUTPUTS, connection);
	}

	public List getOutgoingConnections() {
		return outputs;
	}

	public List getIncomingConnections() {
		return inputs;
	}

	// 连接命令相关
	public void removeOutput(Connection connection) {
		this.outputs.remove(connection);
		fireStructureChange(PROP_OUTPUTS, connection);
	}

	public void removeInput(Connection connection) {
		this.inputs.remove(connection);
		fireStructureChange(PROP_INPUTS, connection);
	}

	// ------------------------------------------------------

	public Node() {
		this.name = "Unknown";
		this.layout = new Rectangle();
		this.children = new ArrayList<Node>();
		this.parent = null;
		this.listeners = new PropertyChangeSupport(this);
		this.propertySource = null;
	}

	public void setName(String name) {
		String oldName = this.name;
		this.name = name;
		getListeners().firePropertyChange(PROPERTY_RENAME, oldName, this.name);
	}

	public String getName() {
		return this.name;
	}

	public void setLayout(Rectangle newLayout) {
		Rectangle oldLayout = this.layout;
		this.layout = newLayout;
		getListeners().firePropertyChange(PROPERTY_LAYOUT, oldLayout, newLayout);
	}

	public Rectangle getLayout() {
		return this.layout;
	}

	public boolean addChild(Node child) {
		boolean b = this.children.add(child);
		if (b) {
			child.setParent(this);
			getListeners().firePropertyChange(PROPERTY_ADD, null, child);
		}
		return b;
	}

	public boolean removeChild(Node child) {
		boolean b = this.children.remove(child);
		if (b)
			getListeners().firePropertyChange(PROPERTY_REMOVE, child, null);
		return b;
	}

	public List<Node> getChildrenArray() {
		return this.children;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getParent() {
		return this.parent;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	public PropertyChangeSupport getListeners() {
		return listeners;
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	// @Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			if (propertySource == null)
				propertySource = new NodePropertySource(this);
			return propertySource;
		}
		return null;
	}

	public boolean contains(Node child) {
		return children.contains(child);
	}

	protected void fireStructureChange(String prop, Object child) {
		listeners.firePropertyChange(prop, null, child);
	}

}
