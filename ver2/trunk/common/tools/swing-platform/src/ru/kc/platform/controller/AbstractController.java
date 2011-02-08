package ru.kc.platform.controller;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ru.kc.platform.action.MethodAction;
import ru.kc.platform.app.AppContext;
import ru.kc.platform.command.AbstractCommand;
import ru.kc.platform.data.Answer;
import ru.kc.platform.module.Module;
import ru.kc.platform.runtimestorage.RuntimeStorageService;

public abstract class AbstractController<T> {
	
	protected Log log = LogFactory.getLog(getClass());
	private List<MethodAction> methodActions;
	
	protected T ui;
	protected AppContext appContext;
	protected RuntimeStorageService runtimeStorage;
	
	void init(AppContext appContext, T ui){
		initContext(appContext);
		this.ui = ui;
		beforeInit();
		init();
	}
	
	private void initContext(AppContext appContext) {
		this.appContext = appContext;
		runtimeStorage = appContext.runtimeStorageService;
	}
	
	protected void beforeInit(){ /* override if need */ };
	
	protected abstract void init();
	

	void setMethodActions(List<MethodAction> actions){
		methodActions = Collections.unmodifiableList(actions);
	}

	public List<MethodAction> getMethodActions(){
		return methodActions;
	}


	
	protected List<MethodAction> getSubActionsRecursive(){
		ArrayList<MethodAction> out = new ArrayList<MethodAction>();
		
		LinkedList<Container> queue = new LinkedList<Container>();
		if(ui instanceof Container){
			queue.addLast((Container)ui);
		}
		while(queue.size() > 0){
			Container container = queue.removeFirst();
			Component[] children = container.getComponents();
			for (Component child : children) {
				if(child instanceof Module<?>){
					out.addAll(((Module<?>) child).getMethodActions());
				}
				if(child instanceof Container){
					queue.addLast((Container)child);
				}
			}
		}
		
		return out;
	}
	
	
	
	protected <N> N invoke(AbstractCommand<N> command) throws Exception {
		return (N) appContext.commandService.invoke(command, appContext);
	}
	
	protected <N> Answer<N> invokeSafe(AbstractCommand<N> command){
		try{
			N result = (N)invoke(command);
			return new Answer<N>(result, false, null);
		}catch (Exception e) {
			log.error("error while invoke "+command,e);
			return new Answer<N>(null, true, e);
		}
	}
	
	

}