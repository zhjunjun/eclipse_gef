package tutogef.part;

import org.eclipse.gef.requests.CreationFactory;
import java.lang.Class;
import tutogef.model.Service;
import tutogef.model.Employe;;
/**
 * 根据Class对象找到对应的Object对象
 * @author Administrator
 *
 */
public class NodeCreationFactory implements CreationFactory
{
	private Class<?> template;
	
	public NodeCreationFactory(Class<?> t) {
		this.template = t;
	}
	
	//@Override
	public Object getNewObject() {	
		if (template == null)
			return null;
		if (template == Service.class)
		{
			Service srv = new Service();
			srv.setEtage(42);
			srv.setName("Factorouf");
			return srv;
		}
		else if (template == Employe.class) {
			Employe emp = new Employe();
			emp.setPrenom("Halle");
			emp.setName("Berry");
			return emp;
		}
		return null;
	}

	//@Override
	public Object getObjectType() {
		return template;
	}
}
