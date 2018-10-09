/* Game state */

extern float elapsed_time; /* time this ship has been active */
extern int   score;        /* total score so far */
extern int   lives;        /* lives remaining */
extern int   dead;        /* ship dead or not */
extern int   gameover;        /* game over or not */
extern int 		asteroidTick;   /* asteroid ticker */

extern struct ship player;

extern struct rock * asteroids; /* array of rocks / pointer to linked-list */
extern struct missile * shots;  /* array of missiles / pointer to linked-list */

extern const float Dt; /* Time step for physics, needed for consistent motion */
