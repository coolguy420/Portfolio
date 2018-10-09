/* Asteroids model */
#include <stdbool.h>
#include <stdint.h>
#include <stdlib.h>
#include <math.h>

#include "model.h"
#include "utils.h"
#include "asteroids.h"

void updateMissiles(missile * head);
void checkMissiles(missile * head);
void removeMissile(missile * head, missile * target);
void removeBackMissile(missile * head);
void checkWallCollisions();
void updateVelocity();
void updateAccel();	
void addAsteroid(rock * head);
void updateAsteroids(rock * head);
void checkAsteroids(rock * head, missile * cursor);
void removeAsteroid(rock * head, rock * target);
void removeBackAsteroid(rock * head);
void checkShip(rock * head);


void physics(void)
{
		if(!dead){
			addAsteroid(asteroids);
			updateAsteroids(asteroids);
			checkAsteroids(asteroids, shots);
			updateMissiles(shots);
			checkMissiles(shots);
			checkWallCollisions();
			updateVelocity();
			updateAccel();	
			checkShip(asteroids);
		}
		
}

void updateAsteroids(rock * head){
	rock * current = head;
	while (current != NULL){
		current->p.x += 50 * Dt * cos(current->heading);
		current->p.y += 50 * Dt * sin(current->heading);
		current = current->next;
	}
}

void checkAsteroids(rock * head, missile * cursor){
	rock * current = head;
	missile * header = cursor;
	if(current->next != NULL){
		current = current->next;
		while(current != NULL){
			//missile * 
			header = cursor;
			while(header != NULL){
				if(header->p.x < current->p.x + 20 && header->p.x > current->p.x && header->p.y <= current->p.y + 20 && header->p.y > current->p.y){
					removeMissile(shots, header);
					removeAsteroid(asteroids, current);	 
					score++;
				}
			header = header->next;
			}
			if(current->p.x > 400){
				current->p.x -= 399;
			}else if(current->p.x < 0){
				current->p.x += 399;
			}else if(current->p.y > 272){
				current->p.y -= 271;
			}else if(current->p.y < 0){
				current->p.y += 271;
			}
			current = current->next;
    }
	}
	
}


void checkShip(rock * head){
	rock * current = head;
	if(current->next != NULL){
		current = current->next;
		while(current != NULL){
			if((player.a.x < current->p.x + 20 && player.a.x > current->p.x && player.a.y <= current->p.y + 20 && player.a.y > current->p.y) ||
				(player.b.x < current->p.x + 20 && player.b.x > current->p.x && player.b.y <= current->p.y + 20 && player.b.y > current->p.y) ||
				(player.c.x < current->p.x + 20 && player.c.x > current->p.x && player.c.y <= current->p.y + 20 && player.c.y > current->p.y)){
				if(player.shield == 0){
					player.p.x = 200;
					player.p.y = 136;
					player.v.x = 0;
					player.v.y = 0;
					player.asteroids = 0;
					dead = true;
					lives--;
					}else{
					player.shield--;
					}
				}
			current = current->next;	
		}	
	}
}

void updateMissiles(missile * head){
	missile * current = head;
	while (current != NULL){
		current->p.x += 100 * Dt * cos(current->heading);
		current->p.y += 100 * Dt * sin(current->heading);
		current = current->next;
	}
}

void checkMissiles(missile * head){
	missile * current = head;
	if(head->next != NULL){
		current = current->next;
		while(current != NULL){
			if((current->p.x > 400) || (current->p.x < 0) || (current->p.y > 272) || (current->p.y < 0)){
				removeMissile(shots, current);
			}
			current = current->next;
    }
	}
	
}

void removeMissile(missile * head, missile * target){
	missile * current = head;
	missile * tmp = NULL;
	if(target->next == NULL){
		removeBackMissile(shots);
	}else{
		while(current->next != NULL){
			if(current->next == target){
				break;
			}
			current = current->next;
		}
		tmp = current->next;
		current->next = tmp->next;
		free(tmp);
	}
	player.missiles--;
}

void removeBackMissile(missile * head){
	missile * current = head;
	while(current->next->next != NULL){
		current = current->next;
	}
	free(current->next);
	current->next = NULL;
}

void removeAsteroid(rock * head, rock * target){
	rock * current = head;
	rock * tmp = NULL;
	if(target->next == NULL){
		removeBackAsteroid(asteroids);
	}else{
		while(current->next != NULL){
			if(current->next == target){
				break;
			}
			current = current->next;
		}
		tmp = current->next;
		current->next = tmp->next;
		free(tmp);
	}
	player.asteroids--;
}

void removeBackAsteroid(rock * head){
	rock * current = head;
	while(current->next->next != NULL){
		current = current->next;
	}
	free(current->next);
	current->next = NULL;
}

void checkWallCollisions(){
		// check for wall collisions and imploy wraparound if so
    if((player.p.x += player.v.x * Dt) > 400){
			player.p.x -= 399;
		}else if((player.p.x += player.v.x * Dt) < 0){
			player.p.x += 399;
		}else if((player.p.y += player.v.y * Dt) > 272){
			player.p.y -= 271;
		}else if((player.p.y += player.v.y * Dt) < 0){
			player.p.y += 271;
		}else{
			player.p.x += player.v.x * Dt;
			player.p.y += player.v.y * Dt;
		}
		
}

void updateVelocity(){
		player.v.x += player.accel.x;
		player.v.y += player.accel.y;
}
	
void updateAccel(){
		player.accel.x = player.thrust * Dt * cos(player.heading);
		player.accel.y = player.thrust * Dt * sin(player.heading);
}

void addAsteroid(rock * head){
	asteroidTick++;
	if(asteroidTick == 50){
		if(player.asteroids < 20){
		rock * current = head;
		while(current->next != NULL){
      current = current->next;
    }
		current->next = static_cast<rock*>(malloc(sizeof(rock)));	
		current->next->p.x = (rand()%(399-1))+ 1;
		current->next->p.y = (rand()%(271-1))+ 1;
		current->next->heading = (rand()%(10-1))+ 1;
		current->next->next = NULL;		
		player.asteroids++;
		}
		asteroidTick = 0;
	}
}
