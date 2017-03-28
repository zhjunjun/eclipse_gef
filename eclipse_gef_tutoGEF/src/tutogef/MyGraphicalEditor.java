package tutogef;

import java.util.ArrayList;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import sun.applet.Main;
import tutogef.actions.CopyNodeAction;
import tutogef.actions.PasteNodeAction;
import tutogef.actions.RenameAction;
import tutogef.figure.EmployeFigure;
import tutogef.figure.ServiceFigure;
import tutogef.model.Employe;
import tutogef.model.Entreprise;
import tutogef.model.Service;
import tutogef.part.AppEditPartFactory;
import tutogef.part.MyTemplateTransferDropTargetListener;
import tutogef.part.NodeCreationFactory;
import tutogef.part.tree.AppTreeEditPartFactory;

public class MyGraphicalEditor extends GraphicalEditorWithPalette {

	public static final String ID = "tutogef.mygraphicaleditor";

	private Entreprise model;
	private KeyHandler keyHandler;

	protected class OutlinePage extends ContentOutlinePage {

		private SashForm sash;

		private ScrollableThumbnail thumbnail;
		private DisposeListener disposeListener;

		public OutlinePage() {
			super(new TreeViewer());
		}

		public void createControl(Composite parent) {
			sash = new SashForm(parent, SWT.VERTICAL);

			getViewer().createControl(sash);

			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new AppTreeEditPartFactory());
			getViewer().setContents(model);

			getSelectionSynchronizer().addViewer(getViewer());
			
			// editor 是可以getSite()的
			IActionBars bars = getSite().getActionBars();
			ActionRegistry ar = getActionRegistry();
			bars.setGlobalActionHandler(ActionFactory.UNDO.getId(), ar.getAction(ActionFactory.UNDO.getId()));
			bars.setGlobalActionHandler(ActionFactory.REDO.getId(), ar.getAction(ActionFactory.REDO.getId()));
			bars.setGlobalActionHandler(ActionFactory.RENAME.getId(), ar.getAction(ActionFactory.RENAME.getId()));
			bars.setGlobalActionHandler(ActionFactory.DELETE.getId(), ar.getAction(ActionFactory.DELETE.getId()));
			bars.setGlobalActionHandler(ActionFactory.COPY.getId(), ar.getAction(ActionFactory.COPY.getId()));
			bars.setGlobalActionHandler(ActionFactory.PASTE.getId(), ar.getAction(ActionFactory.PASTE.getId()));

			// Creation de la miniature.
			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);

			thumbnail = new ScrollableThumbnail((Viewport) ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getFigure());
			thumbnail.setSource(((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS));

			lws.setContents(thumbnail);

			disposeListener = new DisposeListener() {
				// @Override
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(disposeListener);

		}

		public Control getControl() {
			return sash;
		}

		public void dispose() {
			getSelectionSynchronizer().removeViewer(getViewer());
			if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed())
				getGraphicalViewer().getControl().removeDisposeListener(disposeListener);

			super.dispose();
		}
	}

	public MyGraphicalEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	public void doSave(IProgressMonitor monitor) {
	}

	public void doSaveAs() {
	}

	public boolean isDirty() {
		return false;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public Entreprise createEntreprise() {
		Entreprise psyEntreprise = new Entreprise();

		psyEntreprise.setName("Psykokwak Entreprise");
		psyEntreprise.setAddress("Quelque part sur terre");
		psyEntreprise.setCapital(100000);

		Service comptaService = new Service();
		comptaService.setName("Epitech");
		comptaService.setEtage(2);
		comptaService.setLayout(new Rectangle(30, 50, ServiceFigure.SERVICE_FIGURE_DEFWIDTH, ServiceFigure.SERVICE_FIGURE_DEFHEIGHT));

		Employe employeCat = new Employe();
		employeCat.setName("sd");
		employeCat.setPrenom("Jonatham");
		employeCat.setLayout(new Rectangle(25, 40, EmployeFigure.EMPLOYE_FIGURE_DEFWIDTH, EmployeFigure.EMPLOYE_FIGURE_DEFHEIGHT));
		comptaService.addChild(employeCat);

		Employe employeJyce = new Employe();
		employeJyce.setName("psykokwak");
		employeJyce.setPrenom("Jyce");
		employeJyce.setLayout(new Rectangle(100, 60, EmployeFigure.EMPLOYE_FIGURE_DEFWIDTH, EmployeFigure.EMPLOYE_FIGURE_DEFHEIGHT));
		comptaService.addChild(employeJyce);

		Employe employeEva = new Employe();
		employeEva.setName("tups");
		employeEva.setPrenom("Romain");
		employeEva.setLayout(new Rectangle(180, 90, EmployeFigure.EMPLOYE_FIGURE_DEFWIDTH, EmployeFigure.EMPLOYE_FIGURE_DEFHEIGHT));
		comptaService.addChild(employeEva);

		psyEntreprise.addChild(comptaService);

		Service rhService = new Service();
		rhService.setName("Ressources Humaines");
		rhService.setEtage(1);
		rhService.setLayout(new Rectangle(130, 220, ServiceFigure.SERVICE_FIGURE_DEFWIDTH, ServiceFigure.SERVICE_FIGURE_DEFHEIGHT));

		Employe employePaul = new Employe();
		employePaul.setName("Longoria");
		employePaul.setPrenom("Eva");
		employePaul.setLayout(new Rectangle(40, 70, EmployeFigure.EMPLOYE_FIGURE_DEFWIDTH, EmployeFigure.EMPLOYE_FIGURE_DEFHEIGHT));
		rhService.addChild(employePaul);

		Employe employeEric = new Employe();
		employeEric.setName("Parker");
		employeEric.setPrenom("Tony");
		employeEric.setLayout(new Rectangle(170, 100, EmployeFigure.EMPLOYE_FIGURE_DEFWIDTH, EmployeFigure.EMPLOYE_FIGURE_DEFHEIGHT));
		rhService.addChild(employeEric);

		psyEntreprise.addChild(rhService);

		return psyEntreprise;
	}

	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		model = createEntreprise();
		viewer.setContents(model);
		viewer.addDropTargetListener(new MyTemplateTransferDropTargetListener(viewer));
	}

	@Override
	protected void initializePaletteViewer() {
		super.initializePaletteViewer();
		getPaletteViewer().addDragSourceListener(new TemplateTransferDragSourceListener(getPaletteViewer()));
	}

	protected void configureGraphicalViewer() {
		double[] zoomLevels;
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new AppEditPartFactory());

		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);

		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));

		zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);
		ArrayList<String> zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(zoomContributions);

		keyHandler = new KeyHandler();

		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));

		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));

		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);

		viewer.setKeyHandler(keyHandler);

		ContextMenuProvider provider = new AppContextMenuProvider(viewer, getActionRegistry());
		viewer.setContextMenu(provider);

	}

	@SuppressWarnings("unchecked")
	public void createActions() {
		super.createActions();

		ActionRegistry registry = getActionRegistry();
		IAction action = new RenameAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new CopyNodeAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new PasteNodeAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	public Object getAdapter(Class type) {
		if (type == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		if (type == IContentOutlinePage.class) {
			return new OutlinePage();
		}
		return super.getAdapter(type);
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		
		// 返回你定制的Palette
		PaletteRoot root = new PaletteRoot();
		
		// 分组(工具栏)
		PaletteGroup manipGroup = new PaletteGroup("工具栏");
		root.add(manipGroup);
		
		// 工具条目
		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		manipGroup.add(selectionToolEntry);
		manipGroup.add(new MarqueeToolEntry());
		//     新添加连接组
		manipGroup.add(new ConnectionCreationToolEntry("连接","create an connection",null ,null ,null));
		// 分隔符
		PaletteSeparator sep2 = new PaletteSeparator();
		root.add(sep2);

		// 创建(模型组)
		PaletteGroup instGroup = new PaletteGroup("建模");
		root.add(instGroup);
		// 新建Service
		instGroup.add(new CombinedTemplateCreationEntry("Service", "Creation d'un service type", Service.class, new NodeCreationFactory(Service.class), AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, "icons/services-low.png"), AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/services-high.png")));

		// 新建Employe
		instGroup.add(new CombinedTemplateCreationEntry("Employe", "Creation d'un employe model", Employe.class, new NodeCreationFactory(Employe.class), AbstractUIPlugin.imageDescriptorFromPlugin(
				Activator.PLUGIN_ID, "icons/employe-low.png"), AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/employe-high.png")));

		root.setDefaultEntry(selectionToolEntry);
		return root;
	}
}
