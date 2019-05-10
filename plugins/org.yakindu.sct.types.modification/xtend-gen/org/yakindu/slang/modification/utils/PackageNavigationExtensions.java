/**
 * Copyright (c) 2019 itemis AG - All rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * 
 * Contributors:
 * 	itemis AG
 */
package org.yakindu.slang.modification.utils;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.yakindu.base.expressions.expressions.FeatureCall;
import org.yakindu.base.types.Annotation;
import org.yakindu.base.types.ComplexType;
import org.yakindu.base.types.Operation;
import org.yakindu.base.types.Property;
import org.yakindu.base.types.Visibility;

@SuppressWarnings("all")
public class PackageNavigationExtensions {
  public List<ComplexType> allTopLevelComplexTypes(final org.yakindu.base.types.Package p) {
    return IterableExtensions.<ComplexType>toList(Iterables.<ComplexType>filter(p.getMember(), ComplexType.class));
  }
  
  public List<Property> allTopLevelPublicProperties(final org.yakindu.base.types.Package p) {
    final Function1<Property, Boolean> _function = (Property it) -> {
      Visibility _visibility = it.getVisibility();
      return Boolean.valueOf(Objects.equal(_visibility, Visibility.PUBLIC));
    };
    return IterableExtensions.<Property>toList(IterableExtensions.<Property>filter(Iterables.<Property>filter(p.getMember(), Property.class), _function));
  }
  
  public List<ComplexType> allComplexTypes(final org.yakindu.base.types.Package p) {
    return IteratorExtensions.<ComplexType>toList(Iterators.<ComplexType>filter(p.eAllContents(), ComplexType.class));
  }
  
  public List<Operation> allOperations(final org.yakindu.base.types.Package p) {
    return IteratorExtensions.<Operation>toList(Iterators.<Operation>filter(p.eAllContents(), Operation.class));
  }
  
  public List<FeatureCall> allFeatureCalls(final org.yakindu.base.types.Package p) {
    return IteratorExtensions.<FeatureCall>toList(Iterators.<FeatureCall>filter(p.eAllContents(), FeatureCall.class));
  }
  
  public org.yakindu.base.types.Package getContainingPackage(final EObject e) {
    return EcoreUtil2.<org.yakindu.base.types.Package>getContainerOfType(e, org.yakindu.base.types.Package.class);
  }
  
  public Iterable<ComplexType> complexTypesOfAnnotation(final org.yakindu.base.types.Package p, final String annotation) {
    final Function1<ComplexType, Boolean> _function = (ComplexType it) -> {
      Annotation _annotationOfType = it.getAnnotationOfType(annotation);
      return Boolean.valueOf((_annotationOfType != null));
    };
    return IterableExtensions.<ComplexType>filter(this.allTopLevelComplexTypes(p), _function);
  }
}
