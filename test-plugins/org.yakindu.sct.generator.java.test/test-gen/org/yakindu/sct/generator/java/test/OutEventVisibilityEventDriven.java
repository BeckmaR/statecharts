/** Generated by YAKINDU Statechart Tools code generator. */

package org.yakindu.sct.generator.java.test;

import org.junit.*;
import static org.junit.Assert.*;
import org.yakindu.scr.outeventvisibilityeventdriven.OutEventVisibilityEventDrivenStatemachine;
import org.yakindu.scr.outeventvisibilityeventdriven.OutEventVisibilityEventDrivenStatemachine.State;	
import org.yakindu.scr.VirtualTimer;

/**
 * Unit TestCase for OutEventVisibilityEventDriven
 */
@SuppressWarnings("all")
public class OutEventVisibilityEventDriven {
	
	private OutEventVisibilityEventDrivenStatemachine statemachine;	
	
	
	@Before
	public void outEventVisibilityEventDriven_setUp() {
		statemachine = new OutEventVisibilityEventDrivenStatemachine();
		
		statemachine.init();
		
	}

	@After
	public void outEventVisibilityEventDriven_tearDown() {
		statemachine = null;
		
	}
	
	@Test
	public void out_events_must_be_collected_during_local_event_processing_steps() {
		statemachine.enter();
		statemachine.getSCInterface().raiseI(1l);
		assertTrue(statemachine.isRaisedO1());
		assertTrue(statemachine.isRaisedO2());
		statemachine.exit();
	}
	
	@Test
	public void all_out_events_that_are_raised_within_a_single_step_must_be_visible() {
		statemachine.enter();
		assertTrue(!statemachine.isRaisedO1());
		assertTrue(!statemachine.isRaisedO2());
		statemachine.getSCInterface().raiseI(2l);
		assertTrue(statemachine.isRaisedO1());
		assertTrue(!statemachine.isRaisedO2());
		statemachine.getSCInterface().raiseI(2l);
		assertTrue(!statemachine.isRaisedO1());
		assertTrue(statemachine.isRaisedO2());
		statemachine.getSCInterface().raiseI(2l);
		assertTrue(statemachine.isRaisedO1());
		assertTrue(statemachine.isRaisedO2());
		statemachine.exit();
	}
}