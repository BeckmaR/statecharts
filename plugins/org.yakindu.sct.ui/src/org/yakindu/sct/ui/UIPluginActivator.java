/** 
 * Copyright (c) 2015 committers of YAKINDU and others. 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * Contributors:
 * committers of YAKINDU - initial API and implementation
 *
*/
package org.yakindu.sct.ui;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.yakindu.sct.commons.DeadlockDetector;

/**
 * The activator class controls the plug-in life cycle
 */
public class UIPluginActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.yakindu.sct.ui"; //$NON-NLS-1$

	// The shared instance
	private static UIPluginActivator plugin;
	
	/**
	 * The constructor
	 */
	public UIPluginActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		DeadlockDetector detector = DeadlockDetector.INSTANCE;
		Job.getJobManager().addJobChangeListener(new IJobChangeListener() {
			
			@Override
			public void sleeping(IJobChangeEvent event) {
				
			}
			
			@Override
			public void scheduled(IJobChangeEvent event) {
				detector.wait(Job.class.getName());
			}
			
			@Override
			public void running(IJobChangeEvent event) {
				detector.own(Job.class.getName());
			}
			
			@Override
			public void done(IJobChangeEvent event) {
				detector.release(Job.class.getName());
			}
			
			@Override
			public void awake(IJobChangeEvent event) {
				
			}
			
			@Override
			public void aboutToRun(IJobChangeEvent event) {
				
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UIPluginActivator getDefault() {
		return plugin;
	}

}
