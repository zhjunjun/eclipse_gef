package tutogef.editpolicies;

import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import tutogef.commands.CreateConnectionCommand;
import tutogef.model.Node;

public class AppNodeEditPolicy extends  GraphicalNodeEditPolicy{

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest arg0) {
		// (C)
	    CreateConnectionCommand command = (CreateConnectionCommand) arg0.getStartCommand();	
	    // setup the target
	    command.setTarget((Node) getHost().getModel());
		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
	    CreateConnectionCommand command = new CreateConnectionCommand();// (A)
        command.setSource((Node) getHost().getModel());
        // 创建链接的命令被记录
        request.setStartCommand(command); // (B)
        return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
