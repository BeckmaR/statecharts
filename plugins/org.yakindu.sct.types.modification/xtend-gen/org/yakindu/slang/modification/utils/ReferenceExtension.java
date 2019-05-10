package org.yakindu.slang.modification.utils;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.yakindu.base.expressions.expressions.Argument;
import org.yakindu.base.expressions.expressions.ArgumentExpression;
import org.yakindu.base.expressions.expressions.ElementReferenceExpression;
import org.yakindu.base.expressions.expressions.FeatureCall;
import org.yakindu.base.expressions.util.ExpressionBuilder;
import org.yakindu.base.types.Declaration;
import org.yakindu.base.types.Expression;

@SuppressWarnings("all")
public class ReferenceExtension {
  @Inject
  @Extension
  protected ExpressionBuilder _expressionBuilder;
  
  public List<EObject> references(final EObject target) {
    final Function1<EStructuralFeature.Setting, EObject> _function = (EStructuralFeature.Setting it) -> {
      return it.getEObject();
    };
    return IterableExtensions.<EObject>toList(IterableExtensions.<EStructuralFeature.Setting, EObject>map(EcoreUtil.UsageCrossReferencer.find(target, EcoreUtil2.getRootContainer(target)), _function));
  }
  
  protected void _retargetTo(final EObject e, final Declaration target) {
    throw new IllegalStateException(((("Could not retarget reference from " + e) + " to ") + target));
  }
  
  protected void _retargetTo(final ElementReferenceExpression ere, final Declaration target) {
    ere.setReference(target);
  }
  
  protected void _retargetTo(final FeatureCall fc, final Declaration target) {
    fc.setFeature(target);
  }
  
  protected Object _unqualify(final EObject e) {
    return null;
  }
  
  protected Object _unqualify(final FeatureCall fc) {
    final Function1<Argument, Expression> _function = (Argument it) -> {
      return it.getValue();
    };
    final ArgumentExpression newRef = this._expressionBuilder._with(this._expressionBuilder._ref(fc.getFeature()), ((Expression[])Conversions.unwrapArray(ListExtensions.<Argument, Expression>map(fc.getArguments(), _function), Expression.class)));
    EcoreUtil.replace(fc, newRef);
    return null;
  }
  
  protected Object _unqualify(final ElementReferenceExpression ref) {
    EObject _eContainer = ref.eContainer();
    if ((_eContainer instanceof FeatureCall)) {
      EObject _eContainer_1 = ref.eContainer();
      final FeatureCall fc = ((FeatureCall) _eContainer_1);
      final Function1<Argument, Expression> _function = (Argument it) -> {
        return it.getValue();
      };
      final ArgumentExpression newRef = this._expressionBuilder._with(this._expressionBuilder._ref(fc.getFeature()), ((Expression[])Conversions.unwrapArray(ListExtensions.<Argument, Expression>map(fc.getArguments(), _function), Expression.class)));
      EcoreUtil.replace(fc, newRef);
    }
    return null;
  }
  
  protected void _qualifyWith(final EObject oldRef, final Declaration owner) {
    throw new IllegalStateException(("Could not qualify reference " + oldRef));
  }
  
  /**
   * Transforms a reference expression b into a feature call owner.b
   */
  protected void _qualifyWith(final ElementReferenceExpression oldRef, final Declaration owner) {
    EObject _reference = oldRef.getReference();
    final Function1<Argument, Expression> _function = (Argument it) -> {
      return it.getValue();
    };
    final ArgumentExpression newRef = this._expressionBuilder._with(this._expressionBuilder._fc(this._expressionBuilder._ref(owner), ((Declaration) _reference)), ((Expression[])Conversions.unwrapArray(ListExtensions.<Argument, Expression>map(oldRef.getArguments(), _function), Expression.class)));
    EcoreUtil.replace(oldRef, newRef);
  }
  
  /**
   * Transforms a feature call, A.b into A.owner.b
   */
  protected void _qualifyWith(final FeatureCall oldFc, final Declaration owner) {
    final Function1<Argument, Expression> _function = (Argument it) -> {
      return it.getValue();
    };
    final ArgumentExpression newFc = this._expressionBuilder._with(this._expressionBuilder._fc(this._expressionBuilder._fc(oldFc.getOwner(), owner), oldFc.getFeature()), ((Expression[])Conversions.unwrapArray(ListExtensions.<Argument, Expression>map(oldFc.getArguments(), _function), Expression.class)));
    EcoreUtil.replace(oldFc, newFc);
  }
  
  public void retargetTo(final EObject ere, final Declaration target) {
    if (ere instanceof ElementReferenceExpression) {
      _retargetTo((ElementReferenceExpression)ere, target);
      return;
    } else if (ere instanceof FeatureCall) {
      _retargetTo((FeatureCall)ere, target);
      return;
    } else if (ere != null) {
      _retargetTo(ere, target);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ere, target).toString());
    }
  }
  
  public Object unqualify(final EObject ref) {
    if (ref instanceof ElementReferenceExpression) {
      return _unqualify((ElementReferenceExpression)ref);
    } else if (ref instanceof FeatureCall) {
      return _unqualify((FeatureCall)ref);
    } else if (ref != null) {
      return _unqualify(ref);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref).toString());
    }
  }
  
  public void qualifyWith(final EObject oldRef, final Declaration owner) {
    if (oldRef instanceof ElementReferenceExpression) {
      _qualifyWith((ElementReferenceExpression)oldRef, owner);
      return;
    } else if (oldRef instanceof FeatureCall) {
      _qualifyWith((FeatureCall)oldRef, owner);
      return;
    } else if (oldRef != null) {
      _qualifyWith(oldRef, owner);
      return;
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(oldRef, owner).toString());
    }
  }
}
