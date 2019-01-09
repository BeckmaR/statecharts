package org.yakindu.scr.raiseevent;


public class RaiseEventStatemachine implements IRaiseEventStatemachine {
	protected class SCInterfaceImpl implements SCInterface {
	
		private boolean e1;
		
		public boolean isRaisedE1() {
			return e1;
		}
		
		protected void raiseE1() {
			e1 = true;
		}
		
		private boolean e2;
		
		public void raiseE2() {
			e2 = true;
		}
		
		protected void clearEvents() {
			e2 = false;
		}
		protected void clearOutEvents() {
		
		e1 = false;
		}
		
	}
	
	protected SCInterfaceImpl sCInterface;
	
	private boolean initialized = false;
	
	public enum State {
		main_region_StateA,
		main_region_StateB,
		second_region_SateA,
		second_region_StateB,
		$NullState$
	};
	
	private final State[] stateVector = new State[2];
	
	private int nextStateIndex;
	
	
	public RaiseEventStatemachine() {
		sCInterface = new SCInterfaceImpl();
	}
	
	public void init() {
		this.initialized = true;
		for (int i = 0; i < 2; i++) {
			stateVector[i] = State.$NullState$;
		}
		clearEvents();
		clearOutEvents();
	}
	
	public void enter() {
		if (!initialized) {
			throw new IllegalStateException(
				"The state machine needs to be initialized first by calling the init() function."
			);
		}
		enterSequence_main_region_default();
		enterSequence_second_region_default();
	}
	
	public void runCycle() {
		if (!initialized)
			throw new IllegalStateException(
					"The state machine needs to be initialized first by calling the init() function.");
		clearOutEvents();
		for (nextStateIndex = 0; nextStateIndex < stateVector.length; nextStateIndex++) {
			switch (stateVector[nextStateIndex]) {
			case main_region_StateA:
				main_region_StateA_react(true);
				break;
			case main_region_StateB:
				main_region_StateB_react(true);
				break;
			case second_region_SateA:
				second_region_SateA_react(true);
				break;
			case second_region_StateB:
				second_region_StateB_react(true);
				break;
			default:
				// $NullState$
			}
		}
		clearEvents();
	}
	public void exit() {
		exitSequence_main_region();
		exitSequence_second_region();
	}
	
	/**
	 * @see IStatemachine#isActive()
	 */
	public boolean isActive() {
		return stateVector[0] != State.$NullState$||stateVector[1] != State.$NullState$;
	}
	
	/** 
	* Always returns 'false' since this state machine can never become final.
	*
	* @see IStatemachine#isFinal()
	*/
	public boolean isFinal() {
		return false;
	}
	/**
	* This method resets the incoming events (time events included).
	*/
	protected void clearEvents() {
		sCInterface.clearEvents();
	}
	
	/**
	* This method resets the outgoing events.
	*/
	protected void clearOutEvents() {
		sCInterface.clearOutEvents();
	}
	
	/**
	* Returns true if the given state is currently active otherwise false.
	*/
	public boolean isStateActive(State state) {
	
		switch (state) {
		case main_region_StateA:
			return stateVector[0] == State.main_region_StateA;
		case main_region_StateB:
			return stateVector[0] == State.main_region_StateB;
		case second_region_SateA:
			return stateVector[1] == State.second_region_SateA;
		case second_region_StateB:
			return stateVector[1] == State.second_region_StateB;
		default:
			return false;
		}
	}
	
	public SCInterface getSCInterface() {
		return sCInterface;
	}
	
	public boolean isRaisedE1() {
		return sCInterface.isRaisedE1();
	}
	
	public void raiseE2() {
		sCInterface.raiseE2();
	}
	
	/* Entry action for state 'StateB'. */
	private void entryAction_main_region_StateB() {
		sCInterface.raiseE1();
	}
	
	/* 'default' enter sequence for state StateA */
	private void enterSequence_main_region_StateA_default() {
		nextStateIndex = 0;
		stateVector[0] = State.main_region_StateA;
	}
	
	/* 'default' enter sequence for state StateB */
	private void enterSequence_main_region_StateB_default() {
		entryAction_main_region_StateB();
		nextStateIndex = 0;
		stateVector[0] = State.main_region_StateB;
	}
	
	/* 'default' enter sequence for state SateA */
	private void enterSequence_second_region_SateA_default() {
		nextStateIndex = 1;
		stateVector[1] = State.second_region_SateA;
	}
	
	/* 'default' enter sequence for state StateB */
	private void enterSequence_second_region_StateB_default() {
		nextStateIndex = 1;
		stateVector[1] = State.second_region_StateB;
	}
	
	/* 'default' enter sequence for region main region */
	private void enterSequence_main_region_default() {
		react_main_region__entry_Default();
	}
	
	/* 'default' enter sequence for region second region */
	private void enterSequence_second_region_default() {
		react_second_region__entry_Default();
	}
	
	/* Default exit sequence for state StateA */
	private void exitSequence_main_region_StateA() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state StateB */
	private void exitSequence_main_region_StateB() {
		nextStateIndex = 0;
		stateVector[0] = State.$NullState$;
	}
	
	/* Default exit sequence for state SateA */
	private void exitSequence_second_region_SateA() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}
	
	/* Default exit sequence for state StateB */
	private void exitSequence_second_region_StateB() {
		nextStateIndex = 1;
		stateVector[1] = State.$NullState$;
	}
	
	/* Default exit sequence for region main region */
	private void exitSequence_main_region() {
		switch (stateVector[0]) {
		case main_region_StateA:
			exitSequence_main_region_StateA();
			break;
		case main_region_StateB:
			exitSequence_main_region_StateB();
			break;
		default:
			break;
		}
	}
	
	/* Default exit sequence for region second region */
	private void exitSequence_second_region() {
		switch (stateVector[1]) {
		case second_region_SateA:
			exitSequence_second_region_SateA();
			break;
		case second_region_StateB:
			exitSequence_second_region_StateB();
			break;
		default:
			break;
		}
	}
	
	/* Default react sequence for initial entry  */
	private void react_main_region__entry_Default() {
		enterSequence_main_region_StateA_default();
	}
	
	/* Default react sequence for initial entry  */
	private void react_second_region__entry_Default() {
		enterSequence_second_region_SateA_default();
	}
	
	private boolean react() {
		return false;
	}
	
	private boolean main_region_StateA_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				if (sCInterface.e2) {
					exitSequence_main_region_StateA();
					enterSequence_main_region_StateB_default();
				} else {
					did_transition = false;
				}
			}
		}
		return did_transition;
	}
	
	private boolean main_region_StateB_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (react()==false) {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean second_region_SateA_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			if (sCInterface.e1) {
				exitSequence_second_region_SateA();
				enterSequence_second_region_StateB_default();
			} else {
				did_transition = false;
			}
		}
		return did_transition;
	}
	
	private boolean second_region_StateB_react(boolean try_transition) {
		boolean did_transition = try_transition;
		
		if (try_transition) {
			did_transition = false;
		}
		return did_transition;
	}
	
}
