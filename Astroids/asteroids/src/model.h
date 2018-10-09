/* Asteroids Model */
struct point {
    float x,y;
};
typedef struct point coordinate_t;
typedef struct point vector_t;

/* Some insitial struct types if you want to use them */
struct ship {
		int missiles;
		int asteroids;
		int shield;
		double heading;
		double thrust;
		double drag;
    coordinate_t p;
		coordinate_t r;
		coordinate_t a;
		coordinate_t b;
		coordinate_t c;
	  vector_t     v;
		vector_t accel;
};

/* initial structs for building linked lists */
struct rock {
    coordinate_t p;
		double heading;
    struct rock *next;
};

struct missile {
    coordinate_t p;
		double heading;		
    struct missile *next;
};

void physics(void);

