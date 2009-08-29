package ru.dolganov.tool.knowledge.collector.tree;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import model.knowledge.NodeMeta;
import ru.chapaj.util.swing.tree.ExtendTree;
import ru.chapaj.util.swing.tree.ExtendTree.SelectModel;
import ru.dolganov.tool.knowledge.collector.Controller;
import ru.dolganov.tool.knowledge.collector.dialog.NewDirDialog;
import ru.dolganov.tool.knowledge.collector.main.MainWindow;
import ru.dolganov.tool.knowledge.collector.tree.cell.HasCellConst;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellEditor;
import ru.dolganov.tool.knowledge.collector.tree.cell.MainCellRender;

public class TreeController extends Controller<MainWindow> implements HasCellConst{

	ExtendTree tree;
	DefaultMutableTreeNode treeRoot;
	
	
	@Override
	public void init(final MainWindow ui) {
		tree = ui.tree;
		tree.init(ExtendTree.createTreeModel(null), true, new MainCellRender(), SelectModel.SINGLE);
		treeRoot = tree.getRootNode();
		treeRoot.setUserObject("root");
		tree.setRootVisible(false);
		
		tree.setCellEditor(new MainCellEditor());
		tree.setEditable(true);
		
		ui.dirB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				NewDirDialog newDir = new NewDirDialog();
				newDir.init(mainUI);
				newDir.setVisible(true);
				System.out.println("done!");
				newDir.remove();
			}
			
		});
		
		
		
		
		fillTree();
	}


	static class QS {
		List<NodeMeta> list;
		DefaultMutableTreeNode node;
		public QS(List<NodeMeta> list, DefaultMutableTreeNode node) {
			super();
			this.list = list;
			this.node = node;
		}
	}
	
	private void fillTree() {
		treeRoot.removeAllChildren();
		long time = System.currentTimeMillis();
		
		LinkedList<QS> q = new LinkedList<QS>();
		q.addLast(new QS(dao.getRoot().getNodes(),treeRoot));
		while(!q.isEmpty()){
			QS s = q.removeFirst();
			s.node.add(new DefaultMutableTreeNode(Cell.BUTTONS));
			for(NodeMeta meta : s.list){
				DefaultMutableTreeNode chNode = new DefaultMutableTreeNode(meta);
				s.node.add(chNode);
				q.addLast(new QS(dao.getChildren(meta),chNode));
			}
		}
		System.out.println("tree filled after "
				+ ((System.currentTimeMillis() - time) / 1000.) + " sec");
		
		tree.expandPath(treeRoot);
		tree.updateUI();
		
	}

	
}