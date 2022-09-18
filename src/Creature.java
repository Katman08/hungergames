import processing.core.PApplet;
import processing.core.PImage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Creature {
    public int species;
    public double health;
    public double energy;
    public PImage image;

    public double speed;

    public double strength;
    public int id;

    public int x;
    public int y;
    public PApplet pApplet;

    public int[] fears;
    public int[] foods;

    public int priority;
    public double maxHealth;

    public ArrayList<Creature> targets;

    public ArrayList<Creature> family;

    private final Random rand;

    //DIRECTIONS
    public static final String[] DIRECTIONS = {"UP", "UP_RIGHT", "RIGHT", "DOWN_RIGHT", "DOWN", "DOWN_LEFT", "LEFT", "UP_LEFT"};
    public final int UP = 0;
    public final int UP_RIGHT = 1;
    public final int RIGHT = 2;
    public final int DOWN_RIGHT = 3;
    public final int DOWN = 4;
    public final int DOWN_LEFT = 5;
    public final int LEFT = 6;
    public final int UP_LEFT = 7;

    //SPECIES
    public static final String[] SPECIES = {"DOG", "CAT", "MOUSE", "LION", "BURGER"};
    public static final int DOG = 0;
    public static final int CAT = 1;
    public static final int MOUSE = 2;
    public static final int LION = 3;
    public static final int BURGER = 4;

    //PRIORITIES
    public static final String[] PRIORITIES = {"RUN", "EAT", "FUCK", "WANDER", "FOLLOW"};
    public static final int RUN = 0;
    public static final int EAT = 1;
    public static final int FUCK = 2;
    public static final int WANDER = 3;
    public static final int FOLLOW = 4;
    public static final int HELP = 5;

    public Personality personality;

    private int direction;
    public boolean selected = false;
    public String displayText;
    public boolean alive = true;

    public Creature(int in_species,
                    double in_health,
                    PImage img,
                    PApplet in_pApplet,
                    double in_speed,
                    double in_strength,
                    int[] in_fears,
                    int[] in_foods,
                    Personality in_personality,
                    int spawnX,
                    int spawnY) {

        species = in_species;
        health = in_health;
        energy = 3;
        image = img;
        speed = in_speed;
        strength = in_strength;
        pApplet = in_pApplet;
        fears = in_fears;
        foods = in_foods;
        personality = in_personality;
        priority = WANDER;
        maxHealth = in_health;

        targets = new ArrayList<>();
        family = new ArrayList<>();


        rand = new Random();

        x = spawnX;
        y = spawnY;

        id = rand.nextInt(1000000000);

        pApplet.image(image, x, y);
    }

    public Creature() {
        rand = new Random();
        id = rand.nextInt(1000000000);

        personality = new Personality();
        maxHealth = health;
        family = new ArrayList<>();
        targets = new ArrayList<>();
    }

    public void update() {

        //kill creature if health is 0
        if (health <= 0) {
            alive = false;
        }

        if (energy < 0) {
            energy = 0;
        }

        //update status if selected
        if (selected) {

            displayText = this.toString();
            pApplet.textSize(30);
            pApplet.fill(0, 200);
            pApplet.text(displayText, 0,0, 100);

            pApplet.textSize(30);
            pApplet.fill(0, 200);
            pApplet.text(personality.toString(), pApplet.width-400,pApplet.height-250, 100);

            //tint image
            pApplet.tint(255,0,0);
        } else {
            pApplet.noTint();
        }

        switch (priority) {
            case RUN -> runFromTarget();
            case EAT, FUCK, HELP -> runToTarget();
            case WANDER -> wander();
            case FOLLOW -> follow();
        }


        pApplet.image(image, x, y);
    }

    public void runFromTarget() {

        for (Creature target : targets) {
            //ensure creature does not exit the map
            //if target is to the left
            if ((target.x < x) && ((x + (speed * energy)) + image.width <= pApplet.width)) {

                //if target is below
                if ((target.y < y) && (y - (speed * energy) >= 0)) {
                    run(UP_RIGHT);

                    //if target is above
                } else if ((target.y > y) && ((y - (speed * energy)) + image.height <= pApplet.height)) {
                    run(DOWN_RIGHT);
                    //if target is directly left
                } else {
                    run(RIGHT);
                }
            //if target is to the right
            } else if ((target.x > x) && (x - (speed * energy) >= 0)) {

                //if target is below
                if ((target.y < y) && (y - (speed * energy) >= 0)) {
                    run(UP_LEFT);

                //if target is above
                } else if ((target.y > y) && (y + (speed * energy) + image.height <= pApplet.height)) {
                    run(DOWN_LEFT);

                //if target is directly left
                } else {
                    run(LEFT);
                }

            //if target is directly above or below
            } else {
                if ((target.y < y) && (y - (speed * energy) >= 0)) {
                    run(UP);
                } else if ((target.y > y) && (y + (speed * energy) + image.height <= pApplet.height)) {
                    run(DOWN);
                }
            }
        }
    }

    public void runToTarget() {

        for (Creature target : targets) {
            //ensure creature does not exit the map
            //if target is to the left
            if ((target.x < x) && (x - (speed * energy) >= 0)) {

                //if target is below
                if ((target.y < y) && ((y + (speed * energy)) + image.height >= image.height)) {
                    run(DOWN_LEFT);

                //if target is above
                } else if ((target.y > y) && (y - (speed * energy) >= 0)) {
                    run(UP_LEFT);
                //if target is directly left
                } else {
                    run(LEFT);
                }
            //if target is to the right
            } else if ((target.x > x) && (x + (speed * energy) + image.width <= pApplet.width)) {

                //if target is below
                if ((target.y < y) && (y + (speed * energy) + image.height <= pApplet.height)) {
                    run(DOWN_RIGHT);

                    //if target is above
                } else if ((target.y > y) && (y - (speed * energy) <= pApplet.height)) {
                    run(UP_RIGHT);
                    //if target is directly left
                } else {
                    run(RIGHT);
                }

            //if target is directly above or below
            } else {
                if ((target.y < y) && ((y + (speed * energy)) + image.height <= pApplet.height)) {
                    run(DOWN);
                } else if ((target.y > y) && (y - (speed * energy) >= 0)) {
                    run(UP);
                }
            }
        }
    }

    public void wander() {

        if (rand.nextInt(10) == 1) {

            if (rand.nextBoolean()) {
                direction += 1;
            } else {
                direction -= 1;
            }

            //circle around if negative
            if (direction < 0) {
                direction = DIRECTIONS.length+direction;
            } else if (direction >= DIRECTIONS.length) {
                direction -= DIRECTIONS.length;
            }
        }

        //prevent creatures from getting stuck in corners

        //top left corner
        if (calculateDistance(this, 0, 0) < image.width) {
            int[] options = {RIGHT, DOWN_RIGHT, DOWN};
            direction = options[rand.nextInt(options.length)];

        //bottom left corner
        } else if (calculateDistance(this, 0, pApplet.height) < image.width) {
            int[] options = {RIGHT, UP_RIGHT, UP};
            direction = options[rand.nextInt(options.length)];

        //top right corner
        } else if (calculateDistance(this, pApplet.width, 0) < image.width) {
            int[] options = {LEFT, DOWN_LEFT, DOWN};
            direction = options[rand.nextInt(options.length)];

        //bottom right corner
        } else if (calculateDistance(this, pApplet.width, pApplet.height) < image.width) {
            int[] options = {LEFT, UP_LEFT, UP};
            direction = options[rand.nextInt(options.length)];
        } else {

            //prevent creature from wandering off-screen up
            if ((direction == UP) || (direction == UP_LEFT) || (direction == UP_RIGHT)) {
                if (y - (speed * energy) < 0) {
                    //random direction other than up
                    int[] options = {LEFT, DOWN_RIGHT, RIGHT, DOWN, DOWN_RIGHT};
                    direction = options[rand.nextInt(options.length)];
                }
            }

            //prevent creature from wandering off-screen down
            if ((direction == DOWN) || (direction == DOWN_LEFT) || (direction == DOWN_RIGHT)) {
                if (y + (speed * energy) + image.height > pApplet.height) {
                    //random direction other than down
                    int[] options = {UP, UP_RIGHT, RIGHT, LEFT, UP_LEFT};
                    direction = options[rand.nextInt(options.length)];
                }
            }

            //prevent creature from wandering off-screen right
            if ((direction == RIGHT) || (direction == UP_RIGHT) || (direction == DOWN_RIGHT)) {
                if (x + (speed * energy) + image.width > pApplet.width) {
                    //random direction other than right
                    int[] options = {UP, UP_LEFT, LEFT, DOWN, DOWN_LEFT};
                    direction = options[rand.nextInt(options.length)];
                }
            }

            //prevent creature from wandering off-screen left
            if ((direction == LEFT) || (direction == UP_LEFT) || (direction == DOWN_LEFT)) {
                if (x - (speed * energy) < 0) {
                    //turn random direction other than LEFT

                    int[] options = {UP, UP_RIGHT, RIGHT, DOWN, DOWN_RIGHT};
                    direction = options[rand.nextInt(options.length)];
                }
            }
        }

        //walk
        walk(direction);

    }

    public void run(int d) {

        //set class variable
        direction = d;

        //running takes energy
        energy -= 0.01;

        //if energy is 0, walk instead
        if (energy <= 0) {
            walk(direction);
            return;
        }

        switch (direction) {
            case UP -> y -= (speed * energy);
            case DOWN -> y += (speed * energy);
            case LEFT -> x -= (speed * energy);
            case RIGHT -> x += (speed * energy);
            case UP_LEFT -> {
                y -= (speed * energy) / 2;
                x -= (speed * energy) / 2;
            }
            case UP_RIGHT -> {
                y -= (speed * energy) / 2;
                x += (speed * energy) / 2;
            }
            case DOWN_LEFT -> {
                y += (speed * energy) / 2;
                x -= (speed * energy) / 2;
            }
            case DOWN_RIGHT -> {
                y += (speed * energy) / 2;
                x += (speed * energy) / 2;
            }
        }
    }

    public void walk(int direction) {
        switch (direction) {
            case UP -> y -= speed;
            case DOWN -> y += speed;
            case LEFT -> x -= speed;
            case RIGHT -> x += speed;
            case UP_LEFT -> {
                y -= speed / 2;
                x -= speed / 2;
            }
            case UP_RIGHT -> {
                y -= speed / 2;
                x += speed / 2;
            }
            case DOWN_LEFT -> {
                y += speed / 2;
                x -= speed / 2;
            }
            case DOWN_RIGHT -> {
                y += speed / 2;
                x += speed / 2;
            }
        }
    }
    public void follow() {
        int FOLLOW_DISTANCE = 150;
        for (Creature target : targets) {
            if (calculateDistance(this, target) > FOLLOW_DISTANCE + 100) {
                runToTarget();
            } else {
                wander();
            }
        }
    }

    public double calculateDistance(Creature c1, Creature c2) {
        int x1 = c1.x+(c1.image.width/2);
        int y1 = c1.y+(c1.image.height/2);
        int x2 = c2.x+(c2.image.width/2);
        int y2 = c2.y+(c2.image.height/2);

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }

    public double calculateDistance(Creature c1, int x2, int y2) {
        int x1 = c1.x+(c1.image.width/2);
        int y1 = c1.y+(c1.image.height/2);

        double ac = Math.abs(y2 - y1);
        double cb = Math.abs(x2 - x1);

        return Math.hypot(ac, cb);
    }

    public void notice(ArrayList<Creature> nearby) {
        targets = new ArrayList<>();
        for (Creature c : nearby) {

            for (int scaryCreature : fears) {
                if (c.species == scaryCreature) {
                    priority = RUN;
                    targets.add(c);
                }
            }

            //all targets must be of same type
            if (!targets.isEmpty()) {
                return;
            }

            for (int tastyCreature : foods) {
                if ((c.species == tastyCreature) && ((energy < 3) || personality.greedy)) {
                    priority = EAT;
                    targets.add(c);
                }
            }

            //all targets must be of same type
            if (!targets.isEmpty()) {
                return;
            }

            if ((c.species == species) && (c.energy > 2) && !family.contains(c)) {
                priority = FUCK;
                targets.add(c);
                return;
            }

            if ((family.contains(c)) || ((c.species == species) && personality.friendly)){
                priority = FOLLOW;
                targets.add(c);
            }

        }

        //all targets must be of same type
        if (!targets.isEmpty()) {
            return;
        }

        priority = WANDER;
        targets = new ArrayList<>();
    }

    public void attack(Creature prey) {
        prey.health -= strength;
        energy -= 1;
    }

    public String toString() {

        String output;
        if (targets.isEmpty()) {
            output = String.format("""
                            ID: %s
                            Type: %s
                            Priority: %s
                            Direction: %s
                            Total Targets: 0
                            Health: %s
                            Energy: %s""",
                    id,
                    SPECIES[species],
                    PRIORITIES[priority],
                    DIRECTIONS[direction],
                    new DecimalFormat("#.##").format(health),
                    new DecimalFormat("#.##").format(energy));
        } else {
            output = String.format("""
                            ID: %s
                            Type: %s
                            Priority: %s
                            Direction: %s
                            Total Targets: %s
                            Target 1 ID: %s
                            Target 1 Type: %s
                            Target 1 Priority: %s
                            Health: %s
                            Energy: %s""",
                    id,
                    SPECIES[species],
                    PRIORITIES[priority],
                    DIRECTIONS[direction],
                    targets.size(),
                    targets.get(0).id,
                    SPECIES[targets.get(0).species],
                    PRIORITIES[targets.get(0).priority],
                    new DecimalFormat("#.##").format(health),
                    new DecimalFormat("#.##").format(energy));
        }
        return output;
    }
}
