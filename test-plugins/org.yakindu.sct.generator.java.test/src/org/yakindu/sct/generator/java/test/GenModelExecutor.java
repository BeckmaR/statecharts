/**
 * Copyright (c) 2018 itemis AG - All rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 *
 * Contributors:
 * 	Thomas Kutz - itemis AG
 *
 */
package org.yakindu.sct.generator.java.test;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.yakindu.sct.generator.builder.EclipseContextGeneratorExecutorLookup;
import org.yakindu.sct.generator.core.console.IConsoleLogger;
import org.yakindu.sct.generator.core.console.IConsoleLogger.DefaultConsoleLogger;
import org.yakindu.sct.model.sgen.GeneratorModel;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.util.Modules;

public class GenModelExecutor {

	public IStatus execute(GeneratorModel model) {
		assertNotNull(model);
		return getExecutorLookup().execute(model);
	}

	private EclipseContextGeneratorExecutorLookup getExecutorLookup() {
		return new EclipseContextGeneratorExecutorLookup() {
			@Override
			protected Module getContextModule() {
				return Modules.override(super.getContextModule()).with(new Module() {
					@Override
					public void configure(Binder binder) {
						binder.bind(IConsoleLogger.class).to(DefaultConsoleLogger.class);
					}
				});
			};
		};
	}
}
