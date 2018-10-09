/*
 * @brief This program is a starting point for the briefcase assignment for EN0572.
 *        The project must be inside the EN0572 workspace in order to build correctly.
 *        A basic framework for a uCOS-II program is provided. There are some task declarations
 *        and some use of devices. These are not meant to be definitive. You should modify the
 *				program structure to suit your requirements. Some tasks may not be required. Other tasks
 *				may need to be added. You will certainly need to add some concurrency control.
 */


#include <stdbool.h>
#include <ucos_ii.h>
#include <mbed.h>
#include <display.h>
#include <stdint.h>
#include <ucos_ii.h>
#include <MMA7455.h>
#include "buffer.h"

/*
*********************************************************************************************************
*                                            APPLICATION TASK PRIORITIES
*********************************************************************************************************
*/

typedef enum {
	APP_TASK_BUTTONS_PRIO = 4,
	APP_TASK_POT_PRIO,
	APP_TASK_ACC_PRIO,
	APP_TASK_COUNT_PRIO,
	APP_TASK_LED_PRIO,
	APP_TASK_LCD_PRIO
} taskPriorities_t;


/*
*********************************************************************************************************
*                                            APPLICATION TASK STACKS
*********************************************************************************************************
*/

#define  APP_TASK_BUTTONS_STK_SIZE           256
#define  APP_TASK_POT_STK_SIZE               256
#define  APP_TASK_LED_STK_SIZE               256
#define  APP_TASK_ACC_STK_SIZE               256
#define  APP_TASK_COUNT_STK_SIZE             256
#define  APP_TASK_LCD_STK_SIZE               256



static OS_STK appTaskButtonsStk[APP_TASK_BUTTONS_STK_SIZE];
static OS_STK appTaskPotStk[APP_TASK_POT_STK_SIZE];
static OS_STK appTaskLEDStk[APP_TASK_LED_STK_SIZE];
static OS_STK appTaskACCStk[APP_TASK_ACC_STK_SIZE];
static OS_STK appTaskCountStk[APP_TASK_COUNT_STK_SIZE];
static OS_STK appTaskLCDStk[APP_TASK_LCD_STK_SIZE];


/*
*********************************************************************************************************
*                                            APPLICATION FUNCTION PROTOTYPES
*********************************************************************************************************
*/

static void appTaskButtons(void *pdata);
static void appTaskPot(void *pdata);
static void appTaskLED(void *pdata);
static void appTaskAcc(void *pdata);
static void appTaskCount(void *pdata);
static void appTaskLCD(void *pdata);


/*
*********************************************************************************************************
*                                            GLOBAL TYPES AND VARIABLES
*********************************************************************************************************
*/

typedef enum {
	JOY = 0,
	ACC,
	COUNT,
	STATUS,
	POT
} deviceNames_t;

typedef enum {
	unarmed = 0,
	armed,
	pending,
	counting,
	alarmed
} alarm_t;

typedef enum {
	JLEFT = 0,
	JRIGHT,
	JUP,
	JDOWN,
	JCENTER
} buttonId_t;

enum {
	FLASH_MIN_DELAY     = 1,
	FLASH_INITIAL_DELAY = 500,
	FLASH_MAX_DELAY     = 1000,
	FLASH_DELAY_STEP    = 50
};

static bool buttonPressedAndReleased(buttonId_t button);


static DigitalOut led1(P1_18);
static DigitalOut led2(P0_13);
static DigitalIn buttons[] = {P5_0, P5_4, P5_2, P5_1, P5_3}; // LEFT, RIGHT, UP, DOWN, CENTER
static AnalogIn potentiometer(P0_23);
static Display *d = Display::theDisplay();

MMA7455 acc(P0_27, P0_28);
bool accInit(MMA7455& acc); //prototype of init routine
int32_t accVal[3];


static int32_t flashingDelay[2] = {FLASH_INITIAL_DELAY, FLASH_INITIAL_DELAY};

// my variables
static bool security = false;
static bool locked = false;
static bool alarm = false;
static int32_t code[4] = {0,0,0,0};
static int32_t password[4] = {1,2,3,4};
static int32_t position = 0;
static int32_t interval;
static alarm_t status = unarmed;

/*
*********************************************************************************************************
*                                            GLOBAL FUNCTION DEFINITIONS
*********************************************************************************************************
*/

int main() {

  /* Initialise the OS */
  OSInit();

  /* Create the tasks */
  OSTaskCreate(appTaskButtons,
               (void *)0,
               (OS_STK *)&appTaskButtonsStk[APP_TASK_BUTTONS_STK_SIZE - 1],
               APP_TASK_BUTTONS_PRIO);

  OSTaskCreate(appTaskPot,
               (void *)0,
               (OS_STK *)&appTaskPotStk[APP_TASK_POT_STK_SIZE - 1],
               APP_TASK_POT_PRIO);

	OSTaskCreate(appTaskLED,
               (void *)0,
               (OS_STK *)&appTaskLEDStk[APP_TASK_LED_STK_SIZE - 1],
               APP_TASK_LED_PRIO);

	OSTaskCreate(appTaskAcc,
               (void *)0,
               (OS_STK *)&appTaskACCStk[APP_TASK_ACC_STK_SIZE - 1],
               APP_TASK_ACC_PRIO);
							 
	OSTaskCreate(appTaskCount,
               (void *)0,
               (OS_STK *)&appTaskCountStk[APP_TASK_COUNT_STK_SIZE - 1],
               APP_TASK_COUNT_PRIO);

	OSTaskCreate(appTaskLCD,
               (void *)0,
               (OS_STK *)&appTaskLCDStk[APP_TASK_LCD_STK_SIZE - 1],
               APP_TASK_LCD_PRIO);

  /* Initialise buffer semaphores */
	safeBufferInit();

  /* Start the OS */
  OSStart();

  /* Should never arrive here */
  return 0;
}

/*
*********************************************************************************************************
*                                            APPLICATION TASK DEFINITIONS
*********************************************************************************************************
*/

static void appTaskButtons(void *pdata) {
  /* Start the OS ticker -- must be done in the highest priority task */
  SysTick_Config(SystemCoreClock / OS_TICKS_PER_SEC);
  message_t msg;
  /* Task main loop */
  while (true) {
		//security disabled logic
		if(!security){
			if(buttonPressedAndReleased(JUP)){
				locked = true;
			}
			else if (buttonPressedAndReleased(JDOWN)) {
				locked = false;
			}
			else if (buttonPressedAndReleased(JLEFT) && locked) {
				security = true;
				status = armed;
			}
		} // end of security disabled logic

		//security enabled joystick logic
	  if(security){
			if (buttonPressedAndReleased(JRIGHT)) {
				if((position + 1) < 4){
					position++;
				}
			}
			else if(buttonPressedAndReleased(JLEFT)){
				if((position - 1) > -1){
					position--;
				}
			}
			else if(buttonPressedAndReleased(JUP)){
				if((code[position] + 1) < 10){
					code[position]++;
				}
			}
			else if(buttonPressedAndReleased(JDOWN)){
				if((code[position] - 1) > -1){
					code[position]--;
				}
			}
			else if(buttonPressedAndReleased(JCENTER)){
				if(code[0] == password[0]){
					if(code[1] == password[1]){
						if(code[2] == password[2]){
							if(code[3] == password[3]){
								security = false;
								alarm = false;
								status = unarmed;
								locked = false;
							}
						}
					}
				}
			}
		} //end of security enabled logic

		//msg assignment and buffer sending
		msg.id = JOY;
		msg.data[0] = code[0];
		msg.data[1] = code[1];
		msg.data[2] = code[2];
		msg.data[3] = code[3];
		msg.data[4] = locked;
		msg.data[5] = security;
		msg.data[6] = position;
		safeBufferPut(&msg);
		
		msg.id = STATUS;
		msg.data[1] = status;
		safeBufferPut(&msg);

    OSTimeDlyHMSM(0,0,0,100);
  }
}

static void appTaskPot(void *pdata) {
	message_t msg;
  while (true) {
		if(!security){
			interval = int(120 - 110 * potentiometer.read());
			
		}
		msg.id = POT;
		msg.data[0] = interval;
		safeBufferPut(&msg);
    OSTimeDlyHMSM(0,0,0,200);
  }
}

static void appTaskCount(void *pdata){
	message_t msg;
	while(true){
		if(status == pending && (interval !=0)){
			interval--;
		
		}
		if (interval == 0){
					status = alarmed;
					msg.id = STATUS;
					msg.data[2] = status;
					safeBufferPut(&msg);
					OSTimeDlyHMSM(0,0,1,0);

			}
					msg.id = COUNT;
					msg.data[0] = interval;
					safeBufferPut(&msg);
					OSTimeDlyHMSM(0,0,1,0);
	}
}



static void appTaskAcc(void *pdata) {
	message_t msg;

	d->setCursor(2,82);
  if (accInit(acc)) {
		d->printf("Accelerometer initialised");
	} else {
    d->printf("Could not initialise accelerometer");
  }
	
  while (true) {
		acc.read(accVal[0], accVal[1], accVal[2]);
		if(((accVal[0] > 50)||
			  (accVal[1] > 50)||
			  (accVal[2] > 50))&& security){
					alarm = true;
					status = pending;
		}
		msg.id = ACC;
		msg.data[0] = accVal[0];
		msg.data[1] = accVal[1];
		msg.data[2] = accVal[2];
		msg.data[3] = alarm;
		safeBufferPut(&msg);
		
		msg.id = STATUS;
		msg.data[1] = status;
		safeBufferPut(&msg);
		OSTimeDlyHMSM(0,0,0,100);
  }
}

static void appTaskLED(void *pdata) {
  while (true) {
		if(alarm){
			if(interval == 0){
				led1 = !led1;
				led2 = !led2;
			}
		}
	OSTimeDly(flashingDelay[0]);

  }
}

static void appTaskLCD(void *pdata) {
	message_t msg;

	/* Initialise the display */
	d->fillScreen(WHITE);
	d->setTextColor(BLACK, WHITE);
	d->setTextSize(2);
  d->setCursor(2, 2);
	d->printf("EN0572 Assignment");

  while (true) {
		safeBufferGet(&msg);
		switch(msg.id){
			case JOY:{
				d->setCursor(2,22);
				d->printf("BRIEFCASE LOCK : %s", msg.data[4] ? " ON" : "OFF");
				d->setCursor(2,42);
				d->printf("SECURITY: %s", msg.data[5] ? " ON" : "OFF");
				
				//checking which position to wrap with brackets
				switch(msg.data[6]){
					case 0:{
						d->setCursor(2,102);
						d->printf("PASSWORD : [%d], %d, %d, %d", msg.data[0], msg.data[1], msg.data[2], msg.data[3]);
						break;
					}
					case 1:{
						d->setCursor(2,102);
						d->printf("PASSWORD : %d, [%d], %d, %d", msg.data[0], msg.data[1], msg.data[2], msg.data[3]);
						break;
					}
					case 2:{
						d->setCursor(2,102);
						d->printf("PASSWORD : %d, %d, [%d], %d", msg.data[0], msg.data[1], msg.data[2], msg.data[3]);
						break;
					}
					case 3:{
						d->setCursor(2,102);
						d->printf("PASSWORD : %d, %d, %d, [%d]", msg.data[0], msg.data[1], msg.data[2], msg.data[3]);
						break;
					}
					default:{
						break;
					}
				}
				break;
			}
			case POT:{
				d->setCursor(2,82);
				d->printf("INTERVAL TIMER: %03d", msg.data[0]);
				break;
			}
			case ACC:{
				d->setCursor(2,62);
				d->printf("ALARM: %s", msg.data[3] ? " ON" : "OFF");
				d->setCursor(2,162);
				d->printf("Accelerometer: [%05d] [%05d] [%05d]",
				msg.data[0], msg.data[1], msg.data[2]);
				break;
			}
			case STATUS:{			
			switch(msg.data[1]){
					case unarmed:{
						d->setCursor(162,62);
						d->printf("UNARMED   ", msg.data[1]);
						break;
					}
					case armed:{
						d->setCursor(162,62);
						d->printf("ARMED    ", msg.data[1]);
						break;
					}
					case pending:{
						d->setCursor(162,62);
						d->printf("PENDING ", msg.data[1]);
						break;
					}
					case alarmed:{
						d->setCursor(162,62);
						d->printf("ALARMED", msg.data[2]);
						break;
					}
					
					}
				break;
			}
		}
  }
}




/*
 * @brief buttonPressedAndReleased(button) tests to see if the button has
 *        been pressed then released.
 *
 * @param button - the name of the button
 * @result - true if button pressed then released, otherwise false
 *
 * If the value of the button's pin is 0 then the button is being pressed,
 * just remember this in savedState.
 * If the value of the button's pin is 1 then the button is released, so
 * if the savedState of the button is 0, then the result is true, otherwise
 * the result is false.
 */
bool buttonPressedAndReleased(buttonId_t b) {
	bool result = false;
	uint32_t state;
	static uint32_t savedState[5] = {1,1,1,1,1};

	state = buttons[b].read();
  if ((savedState[b] == 0) && (state == 1)) {
		result = true;
	}
	savedState[b] = state;
	return result;
}



bool accInit(MMA7455& acc) {
  bool result = true;
  if (!acc.setMode(MMA7455::ModeMeasurement)) {
    // screen->printf("Unable to set mode for MMA7455!\n");
    result = false;
  }
  if (!acc.calibrate()) {
    // screen->printf("Failed to calibrate MMA7455!\n");
    result = false;
  }
  // screen->printf("MMA7455 initialised\n");
  return result;
}
