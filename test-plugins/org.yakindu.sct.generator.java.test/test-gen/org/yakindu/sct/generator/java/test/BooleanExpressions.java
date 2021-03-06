/** Generated by YAKINDU Statechart Tools code generator. */

package org.yakindu.sct.generator.java.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.yakindu.scr.booleanexpressions.BooleanExpressionsStatemachine;
import org.yakindu.scr.booleanexpressions.BooleanExpressionsStatemachine.State;	
import org.yakindu.scr.VirtualTimer;
import org.yakindu.scr.VirtualTimer.VirtualTimeTask;
import org.yakindu.scr.VirtualTimer.CycleTimeEventTask;

/**
 * Unit TestCase for BooleanExpressions
 */
@SuppressWarnings("all")
public class BooleanExpressions {
	
	private BooleanExpressionsStatemachine statemachine;	
	private VirtualTimer timer;
	
	
	@Before
	public void booleanExpressions_setUp() {
		statemachine = new BooleanExpressionsStatemachine();
		timer = new VirtualTimer(200);
		timer.schedulePeriodicalTask(new CycleTimeEventTask(statemachine), 200, 200);
		
		statemachine.init();
		
	}

	@After
	public void booleanExpressions_tearDown() {
		statemachine = null;
		
		timer = null;
	}
	
	@Test
	public void booleanExpressions() {
		statemachine.enter();
		assertTrue(statemachine.isStateActive(State.main_region_StateA));
		assertTrue(statemachine.getMyBool1() == true);
		assertTrue(statemachine.getMyBool2() == false);
		statemachine.raiseE1();
		timer.cycleLeap(1l);
		assertTrue(statemachine.isStateActive(State.main_region_StateB));
		assertTrue(statemachine.getAnd() == false);
		assertTrue(statemachine.getOr() == true);
		assertTrue(statemachine.getNot() == false);
		assertTrue(statemachine.getEqual() == false);
		assertTrue(statemachine.getNotequal() == true);
	}
}
