package tutogef.commands;

import org.eclipse.gef.commands.Command;

import tutogef.model.Connection;
import tutogef.model.Node;

public class DeleteConnectionCommand extends Command {

	Node source;

    Node target;

    Connection connection;

    //Setters
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public void setTarget(Node target) {
        this.target = target;
    }
	@Override
	public void execute() {
		source.removeOutput(connection);
        target.removeInput(connection);
        connection.setSource(null);
        connection.setTarget(null);
	}
}
