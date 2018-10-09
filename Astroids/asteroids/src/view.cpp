/* Asteroids view
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

#include "asteroids.h"
#include "model.h"
#include "utils.h"



Display *graphics = Display::theDisplay();

const colour_t background = rgb(0,51,102); /* Midnight Blue */

/*bitmaps*/
// blarg 
const unsigned char bits[] = {0x13, 0x00, 0x15, 0x00, 0x93, 0xcd, 0x55, 0xa5, 0x93, 0xc5, 0x00, 0x80, 0x00, 0x60};

unsigned char flippedbits[] = {0x13, 0x00, 0x15, 0x00, 0x93, 0xcd, 0x55, 0xa5, 0x93, 0xc5, 0x00, 0x80, 0x00, 0x60};

extern "C" char fact(char);
void handleBitmap(void);

const coordinate_t shape[] = {
    {10,0}, {-5,5}, {-5,-5}
};

/* double buffering functions */
void init_DBuffer(void)
{   /* initialise the LCD driver to use second frame in buffer */
    uint16_t *bufferbase = graphics->getFb();
    uint16_t *nextbuffer = bufferbase+ (480*272);
    LPC_LCD->UPBASE = (uint32_t)nextbuffer;
}

void swap_DBuffer(void)
{   /* swaps frames used by the LCD driver and the graphics object */
    uint16_t *buffer = graphics->getFb();
    graphics->setFb( (uint16_t*) LPC_LCD->UPBASE);
    LPC_LCD->UPBASE = (uint32_t)buffer;
}

void updatePoints(void);
void drawBackground(void);
void drawShip();
void drawShield();
void drawMissiles(missile * head);
void drawAsteroids(rock * head);

void draw(void)
{
		if(gameover){
			drawBackground();
			graphics->setCursor(200,136);
			graphics->printf("Game over. Restart device.");	
			for(int i = 0; i < 14; i++){
					flippedbits[i] = fact(bits[i]);
				}
			graphics->drawBitmap(50, 50, flippedbits, 16, 7, WHITE);
		}else if(dead){
		  drawBackground();
			graphics->setCursor(200,136);
			graphics->printf("Not in play. Press to launch.");
		}else if(!dead){
			drawBackground();
			updatePoints();
			drawMissiles(shots);
			drawAsteroids(asteroids);
			drawShip();
			drawShield();
		}
    swap_DBuffer();
}

void drawBackground(void){
		graphics->fillScreen(background);
	  graphics->drawLine(400, 0, 400, 272, WHITE);
		graphics->setCursor(402,3);
    graphics->printf("Asteroids");
		graphics->setCursor(402,13);
		graphics->printf("Time: %.2f", elapsed_time);
		graphics->setCursor(402,23);
		graphics->printf("Score: %d", score);
		graphics->setCursor(402,33);
		graphics->printf("Lives: %d", lives);
		graphics->setCursor(402,43);
		graphics->printf("a.x: %.2f", player.a.x);
		graphics->setCursor(402,53);
		graphics->printf("a.y: %.2f", player.a.y);
		graphics->setCursor(402,63);
		graphics->printf("b.x: %.2f", player.b.x);
		graphics->setCursor(402,73);
		graphics->printf("b.y: %.2f", player.b.y);
		graphics->setCursor(402,83);
		graphics->printf("c.x: %.2f", player.c.x);
		graphics->setCursor(402,93);
		graphics->printf("c.y: %.2f", player.c.y);
		graphics->setCursor(402,103);
		graphics->printf("head: %.2f", player.heading);
		graphics->setCursor(402,113);
		graphics->printf("v.x:%.2f", player.v.x);
		graphics->setCursor(402,123);
		graphics->printf("v.y:%.2f", player.v.y);
		graphics->setCursor(402,133);
		graphics->printf("a.x:%.2f", player.accel.x);
		graphics->setCursor(402,143);
		graphics->printf("a.y:%.2f", player.accel.y);
		graphics->setCursor(402,153);
		graphics->printf("missiles:%d", player.missiles);
		graphics->setCursor(402,163);
		graphics->printf("tick:%d", asteroidTick);
		graphics->setCursor(402,173);
		graphics->printf("asteroids:%d", player.asteroids);
		graphics->setCursor(402,183);
		graphics->printf("shield:%d", player.shield);
}

void updatePoints(void){
		player.r.x = shape[0].x * cos(player.heading) - shape[0].y * sin(player.heading);
		player.r.y = shape[0].x * sin(player.heading) + shape[0].y * cos(player.heading);
		player.c.x = player.r.x + player.p.x;
		player.c.y = player.r.y + player.p.y;
		player.r.x = shape[1].x * cos(player.heading) - shape[1].y * sin(player.heading);
		player.r.y = shape[1].x * sin(player.heading) + shape[1].y * cos(player.heading);
		player.a.x = player.r.x + player.p.x;
		player.a.y = player.r.y + player.p.y;
		player.r.x = shape[2].x * cos(player.heading) - shape[2].y  * sin(player.heading);
		player.r.y = shape[2].x * sin(player.heading) + shape[2].y  * cos(player.heading);
		player.b.x = player.r.x + player.p.x;
		player.b.y = player.r.y + player.p.y;
}

void drawMissiles(missile * head){
		missile * current = head->next;
		while (current != NULL){
			graphics->drawPixel(current->p.x, current->p.y, WHITE);
			current = current->next;
		}
}

void drawAsteroids(rock * head){
		rock * current = head->next;
		while (current != NULL){
			graphics->drawRect(current->p.x, current->p.y, 20, 20, WHITE);
			current = current->next;
		}
}

void drawShip(void){
		graphics->drawTriangle(player.a.x, player.a.y, player.b.x, player.b.y, player.c.x, player.c.y, WHITE);
		graphics->fillTriangle(player.a.x, player.a.y, player.b.x, player.b.y, player.c.x, player.c.y, WHITE);
}

void drawShield(void){
	if(player.shield > 0){
		graphics->drawTriangle(player.a.x, player.a.y, player.b.x, player.b.y, player.c.x , player.c.y, RED);
	}
}
