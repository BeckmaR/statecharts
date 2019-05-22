package org.yakindu.sct.generator.java.test;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.Assert;
import org.yakindu.sct.model.sgen.GeneratorEntry;
import org.yakindu.sct.model.sgen.GeneratorModel;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.types.generator.statechart.java.GenmodelEntries;

public class TestModelGenerator {

	private static final String TEST_MODEL_BUNDLE = "org.yakindu.sct.test.models";
	private static final String GEN_JAVA_TEST_BUNDLE = "org.yakindu.sct.generator.java.test";

	private String statechartFile;
	private String genModelFile;

	public TestModelGenerator(String statechartFile, String genModelFile) {
		this.statechartFile = statechartFile;
		this.genModelFile = genModelFile;
	}

	public void generate() {
		initWorkspace();
		
		Statechart sct = loadModel(TEST_MODEL_BUNDLE, statechartFile);
		Assert.assertNotNull(sct);
		
		GeneratorModel gen = loadModel(GEN_JAVA_TEST_BUNDLE, genModelFile);
		Assert.assertNotNull(gen);
		
		gen.getEntries().get(0).setElementRef(sct);
		
//		cleanUpTarget(gen);
		
		execute(gen);
		
		performFullBuild();
	}

	protected void initWorkspace() {
		try {
			enableAutoBuild(false);
			importBundle(TEST_MODEL_BUNDLE);
			importBundle(GEN_JAVA_TEST_BUNDLE);
		} catch (CoreException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void cleanUpTarget(GeneratorModel gen) {
		GenmodelEntries genModelHelper = new GenmodelEntries();
		GeneratorEntry entry = gen.getEntries().get(0);
		
		// TODO: Naming not injected here, so this throws an NPE
		String targetPath = genModelHelper.getImplementationPackagePath(entry);
		
		IFile targetFolder = getFile(GEN_JAVA_TEST_BUNDLE, targetPath);
		if (targetFolder.exists()) {
			try {
				targetFolder.delete(true, null);
			} catch (CoreException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	protected void execute(GeneratorModel gen) {
		getGenModelExecutor().execute(gen);
	}

	// TODO: copied from TestModelGenerationRunner
	protected IProject importBundle(String bundleName) throws IOException, CoreException {
		URL projectDesc = Platform.getBundle(bundleName).getEntry(".project");
		Path pathToProjectFile = new Path(FileLocator.toFileURL(projectDesc).getPath());

		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(pathToProjectFile);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		if (!project.exists()) {
			project.create(description, null);
		}
		project.open(null);
		return project;
	}

	// TODO: copied from TestHelper
	protected static boolean enableAutoBuild(boolean enable) throws CoreException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription desc = workspace.getDescription();
		boolean isAutoBuilding = desc.isAutoBuilding();
		if (isAutoBuilding != enable) {
			desc.setAutoBuilding(enable);
			workspace.setDescription(desc);
		}
		return isAutoBuilding;
	}

	protected GenModelExecutor getGenModelExecutor() {
		return new GenModelExecutor();
	}

	protected IProject getProject(String projectName) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	@SuppressWarnings("unchecked")
	private <T> T loadModel(String projectName, String modelPath) {
		IFile modelFile = getFile(projectName, modelPath);
		Resource resource = loadResource(modelFile, new ResourceSetImpl());
		return (T) resource.getContents().get(0);
	}

	protected IFile getFile(String projectName, String filePath) {
		IProject project = getProject(projectName);
		IFile modelFile = project.getFile(filePath);
		return modelFile;
	}

	protected Resource loadResource(IFile file, ResourceSet set) {
		URI uri = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
		return set.getResource(uri, true);
	}
	
	protected void performFullBuild() {
		try {
			ResourcesPlugin.getWorkspace().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}
}
