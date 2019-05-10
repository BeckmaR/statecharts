/**
 * Copyright (c) 2019 committers of YAKINDU and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * committers of YAKINDU - initial API and implementation
 */
package org.yakindu.slang.modification;

import com.google.inject.Inject;
import java.util.Collection;
import java.util.Set;
import org.eclipse.emf.transaction.RunnableWithResult;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.yakindu.slang.modification.IModification;

/**
 * The ModificationExecutor holds a set of {@link IModification} instances, obtained via injection
 * (see {@link org.yakindu.slang.SlangGeneratorModule}). It can be used to execute a number of
 * IModification instances on a {@link Collection&lt;{@link org.yakindu.base.types.Package}&gt;.
 * The collection can explicitly be changed during this process.
 * 
 * @author andreas muelder - Initial contribution and API
 */
@SuppressWarnings("all")
public class ModificationExecutor implements IModification {
  @Inject
  private Set<IModification> modifications;
  
  protected Collection<org.yakindu.base.types.Package> modifyInternal(final Collection<org.yakindu.base.types.Package> packages) {
    Collection<org.yakindu.base.types.Package> _xblockexpression = null;
    {
      Collection<org.yakindu.base.types.Package> result = packages;
      for (final IModification modification : this.modifications) {
        result = modification.modify(result);
      }
      _xblockexpression = packages;
    }
    return _xblockexpression;
  }
  
  @Override
  public Collection<org.yakindu.base.types.Package> modify(final Collection<org.yakindu.base.types.Package> packages) {
    try {
      final Function1<org.yakindu.base.types.Package, TransactionalEditingDomain> _function = (org.yakindu.base.types.Package it) -> {
        return TransactionUtil.getEditingDomain(it);
      };
      final TransactionalEditingDomain editingDomain = IterableExtensions.<TransactionalEditingDomain>head(IterableExtensions.<org.yakindu.base.types.Package, TransactionalEditingDomain>map(packages, _function));
      if ((editingDomain == null)) {
        return this.modifyInternal(packages);
      } else {
        final RunnableWithResult.Impl<Collection<org.yakindu.base.types.Package>> runnable = new RunnableWithResult.Impl<Collection<org.yakindu.base.types.Package>>() {
          @Override
          public void run() {
            this.setResult(ModificationExecutor.this.modifyInternal(packages));
          }
        };
        editingDomain.runExclusive(runnable);
        return runnable.getResult();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
