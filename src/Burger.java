import processing.core.PApplet;
import processing.core.PImage;

public class Burger extends Creature {
    public Burger(PApplet in_pApplet, PImage img, int spawnX, int spawnY) {
        species = BURGER;
        health = 5;
        image = img;
        speed = 0;
        fears = new int[] {};
        foods = new int[] {};
        pApplet = in_pApplet;
        energy = 1;
        strength = 1;

        x = spawnX;
        y = spawnY;

        pApplet.image(image, x, y);
    }
}
