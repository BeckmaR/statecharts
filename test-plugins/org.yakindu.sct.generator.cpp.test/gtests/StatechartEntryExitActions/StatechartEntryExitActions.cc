#include <string>
#include "gtest/gtest.h"
#include "StatechartEntryAndExitActions.h"
#include "sc_runner.h"
#include "sc_types.h"

namespace  {

statechartactions::StatechartEntryAndExitActions* statechart;



//! The timers are managed by a timer service. */
static SctUnitRunner * runner;

class StatechartEntryExitActions : public ::testing::Test{
	protected:
	virtual void SetUp() {
		statechart = new statechartactions::StatechartEntryAndExitActions();
		statechart->init();
		runner = new SctUnitRunner(
			statechart,
			false,
			200
		);
	}
	virtual void TearDown() {
		delete statechart;
		delete runner;
	}
};


TEST_F(StatechartEntryExitActions, entryActionsAreExecutedOnEnteringStatechart) {
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_x()== 0);
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_y()== 0);
	
	statechart->getDefaultSCI()->set_b(true);
	
	statechart->enter();
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_x()== 5);
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_y()== 3);
	
	
}
TEST_F(StatechartEntryExitActions, exitActionsAreExecutedOnEnteringStatechart) {
	
	statechart->enter();
	
	statechart->exit();
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_x()== 6);
	
	EXPECT_TRUE(statechart->getDefaultSCI()->get_y()== 2);
	
	
}

}

