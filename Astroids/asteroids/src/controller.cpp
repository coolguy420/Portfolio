/* Controller */

/* C libraries */
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <math.h>

/* hardware platform libraries */
#include <mbed.h>

/* asteroids */
#include "model.h"
#include "asteroids.h"

/* Joystick 5-way switch
*/
enum position { left,down,right,up,centre };
DigitalIn joystick[] = {P5_0, P5_1, P5_4, P5_2, P5_3};

void fire(missile * head, float x, float y, double heading);

void controls(void)
{
	if(!dead){
		if(!joystick[0]){
			player.heading += 0.3;
		}else if(!joystick[2]){
			player.heading -= 0.3;
		}else if(!joystick[3]){
			player.thrust = 10;
		}else if(!joystick[1]){
			fire(shots, player.p.x, player.p.y, player.heading);
		}else{
			player.thrust = 0;
		}
	}
	
}

void fire(missile * head, float x, float y, double heading){
	missile * current = head;
	while(current->next != NULL){
        current = current->next;
    }
	
  /* now we can add a new missile */
	current->next = static_cast<missile*>(malloc(sizeof(missile)));
  current->next->p.x = x;
  current->next->p.y = y;
	current->next->heading = heading;			
	current->next->next = NULL;			
	player.missiles++;
}
