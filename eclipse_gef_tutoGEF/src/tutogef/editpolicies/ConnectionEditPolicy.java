package tutogef.editpolicies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import tutogef.commands.DeleteCommand;
import tutogef.commands.DeleteConnectionCommand;
import tutogef.model.Connection;

public class ConnectionEditPolicy extends ComponentEditPolicy {
	
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		// 获取到要删除的连接对象
		Connection comm = (Connection)getHost().getModel();
		// 新建一个删除命令
        DeleteConnectionCommand cmd=new DeleteConnectionCommand();
        // 设置 命令的 一些参数 :　source 和 target 和 connection
        cmd.setTarget(comm.getTarget());
        cmd.setSource(comm.getSource());
        cmd.setConnection(comm);
        // 将命令返回 
        return cmd;
	}
}
