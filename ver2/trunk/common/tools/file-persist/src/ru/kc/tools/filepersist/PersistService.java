package ru.kc.tools.filepersist;

import java.io.File;
import java.io.IOException;

import ru.kc.exception.BaseException;
import ru.kc.tools.filepersist.command.Command;
import ru.kc.tools.filepersist.command.CreateOrLoadData;
import ru.kc.tools.filepersist.model.DataFactory;
import ru.kc.tools.filepersist.persist.EntityManager;

public class PersistService {
	
	Context context;
	
	public PersistService() {
	}
	
	public void init(String rootDirPath) throws Exception{
		initContext(rootDirPath);
		invoke(new CreateOrLoadData());
	}

	private void initContext(String rootDirPath) throws Exception {
		File root = createRootDir(rootDirPath);
		
		EntityManager dataManager = new EntityManager();
		dataManager.init(root);
		
		DataFactory dataFactory = new DataFactory();
		
		context = new Context(root,
				dataManager,
				dataFactory);
	}

	private File createRootDir(String rootDirPath) throws BaseException {
		File root = new File(rootDirPath);
		root.mkdirs();
		root.mkdir();
		if(!root.isDirectory()){
			throw new BaseException("!root.isDirectory() with path "+rootDirPath);
		}
		return root;
	}
	
	private <O> O invoke(Command<O> command) throws Exception{
		return context.invoke(command);
	}

}