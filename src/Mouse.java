import processing.core.PApplet;
import processing.core.PImage;

public class Mouse extends Creature {
    public Mouse(PApplet in_pApplet, PImage img, int spawnX, int spawnY) {
        species = MOUSE;
        health = 2;
        image = img;
        speed = 3;
        fears = new int[] {LION, DOG, CAT};
        foods = new int[] {BURGER};
        pApplet = in_pApplet;
        energy = 4;
        strength = 2;

        x = spawnX;
        y = spawnY;

        pApplet.image(image, x, y);
    }
}
