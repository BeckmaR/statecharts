package org.yakindu.sct.model.sequencer.cyclebased

import com.google.inject.Inject
import java.util.Collection
import org.yakindu.base.expressions.util.ExpressionBuilder
import org.yakindu.base.types.ComplexType
import org.yakindu.base.types.Package
import org.yakindu.base.types.TypeBuilder
import org.yakindu.base.types.typesystem.ITypeSystem
import org.yakindu.sct.model.sequencer.types.StatemachineMethods
import org.yakindu.slang.modification.IModification
import org.yakindu.slang.modification.utils.PackageNavigationExtensions

class CycleBasedModification implements IModification {
	
	@Inject extension CycleBasedAnnotation
	@Inject extension ICycleBased
	@Inject extension StatemachineMethods
	@Inject extension PackageNavigationExtensions
	@Inject extension TypeBuilder 
	@Inject extension ExpressionBuilder 
	
	
	override modify(Collection<Package> pkgs) {
		pkgs.cycleBasedMachines.forEach[sm |
			sm.containingPackage.member += cycleBasedInterfaceType
			
			sm._extends(cycleBasedInterfaceType)
			
			sm.features += sm.runCycleMethod
		]
		
		pkgs
	}
	
	def protected getCycleBasedMachines(Collection<Package> pkgs) {
		pkgs.map[member].flatten.filter(ComplexType).filter[isCycleBased].toList
	}
	
	def protected runCycleMethod(ComplexType sm) {
		_op("runCycle", ITypeSystem.VOID) => [
			body = _block(
				sm.clearOutEventsMethod._call,
				sm.rtcMethod._call,
				sm.clearEventsMethod._call
			)
			
		]
	}
	
	
}
