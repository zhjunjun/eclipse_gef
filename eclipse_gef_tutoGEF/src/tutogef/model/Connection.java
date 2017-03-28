package tutogef.model;

public class Connection extends Node{

	private Node source;

	private Node target;

	public void setSource(Node source) {
		this.source = source;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public Node getTarget() {
		return this.target;
	}

	public Node getSource() {
		return this.source;
	}

	/**
	 * @param source
	 * @param target
	 */
	public Connection(Node source, Node target) {
		super();
		this.source = source;
		this.target = target;
		source.addOutput(this);
		target.addInput(this);
	}
}
