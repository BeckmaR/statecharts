/* Generated by YAKINDU Statechart Tools code generator. */

#include "gtest/gtest.h"
#include "BooleanExpressions.h"

#include "sc_timer_service.h"



//! The timers are managed by a timer service. */
static sc_unit_timer_service_t timer_service;

static BooleanExpressions statechart;

class BooleanExpressionsTest : public ::testing::Test{
	protected:
	virtual void SetUp() {
		booleanExpressions_init(&statechart);
		sc_timer_service_init(
			&timer_service,
			0,
			(sc_run_cycle_fp) &booleanExpressions_runCycle,
			false,
			200,
			&statechart
		);
	}
};


TEST_F(BooleanExpressionsTest, booleanExpressions) {
	booleanExpressions_enter(&statechart);
	EXPECT_TRUE(booleanExpressions_isStateActive(&statechart, BooleanExpressions_main_region_StateA));
	EXPECT_TRUE(booleanExpressionsIface_get_myBool1(&statechart)== true);
	EXPECT_TRUE(booleanExpressionsIface_get_myBool2(&statechart)== false);
	booleanExpressionsIface_raise_e1(&statechart);
	sc_timer_service_proceed_cycles(&timer_service, 1);
	EXPECT_TRUE(booleanExpressions_isStateActive(&statechart, BooleanExpressions_main_region_StateB));
	EXPECT_TRUE(booleanExpressionsIface_get_and(&statechart)== false);
	EXPECT_TRUE(booleanExpressionsIface_get_or(&statechart)== true);
	EXPECT_TRUE(booleanExpressionsIface_get_not(&statechart)== false);
	EXPECT_TRUE(booleanExpressionsIface_get_equal(&statechart)== false);
	EXPECT_TRUE(booleanExpressionsIface_get_notequal(&statechart)== true);
}


