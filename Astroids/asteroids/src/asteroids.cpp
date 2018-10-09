/* Asteroids
    Sample solution for assignment
    Semester 2 -- Small Embedded Systems
    Dr Alun Moon
*/

/* C libraries */
#include <stdlib.h>
#include <stdint.h>
#include <stdbool.h>
#include <math.h>
#include <string.h>

/* hardware platform libraries */
#include <display.h>
#include <mbed.h>

/* Main game elements */
#include "model.h"
#include "view.h"
#include "controller.h"

/* Game state */
float elapsed_time; 
int   score;
int		dead;
int   lives;
int gameover;
int asteroidTick;
struct ship player;
struct rock * asteroids = NULL;
struct missile * shots = NULL;

float Dt = 0.01f;

Ticker model, view, controller;

bool paused = true;
/* The single user button needs to have the PullUp resistor enabled */
DigitalIn userbutton(P2_10,PullUp);
int main()
{

    init_DBuffer();
    
		asteroidTick = 0;
    view.attach( draw, 0.025);
    model.attach( physics, Dt);
    controller.attach( controls, 0.1);
    player.p.x = 200;
		player.p.y = 136;
		player.thrust = 10;
		player.shield = 100;
    lives = 3;
		asteroids = static_cast<rock*>(malloc(sizeof(rock)));
		asteroids->next = NULL;
    shots = static_cast<missile*>(malloc(sizeof(missile)));
		shots->next = NULL;
    /* Pause to start */
    while( userbutton.read() ){ /* remember 1 is not pressed */
        paused=true;
				dead = true;
        wait_ms(100);
    }
    paused = false;
    dead = false;
    while(true) {
        while(lives>0){
            if(dead){
							if( userbutton.read() ){ /* remember 1 is not pressed */
								wait_ms(100);
							}else{
								player.shield = 100;
								dead = false;
							}
						}
        }
				gameover = true;
				// stuff
    }
}


