package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main implements MouseListener, KeyListener {

    public JFrame frame;
    public JPanel panel;
    public Canvas canvas;

    public int WIDTH = 600;
    public int HEIGHT = 600;

    public int shiftX;
    public int shiftY;

    public double scale;

    public MovingObj focusedObj;

    public BufferStrategy bufferStrategy;

    public ArrayList<MovingObj> movingObjs;

    public Image sunPic;
    public Image marsPic;
    public Image earthPic;
    public Image shuttlePic;
    public Image venuspic;
    public Image mercuryPic;
    public Image saturnPic;
    public Image uranusPic;
    public Image jupiterPic;
    public Image neptunePic;


    public static void main(String[] args) {
        Main m = new Main();
        m.run();
    }

    public void run(){
        setUpGraphics();
        setUpPlanets();
        while(true){
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            calcGravity();
            render();
        }
    }

    public void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);
        canvas.addMouseListener(this);// adds the canvas to the panel.
        canvas.addKeyListener(this);

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();

        sunPic = Toolkit.getDefaultToolkit().getImage("sun.png");
        earthPic = Toolkit.getDefaultToolkit().getImage("earth.png");
        marsPic = Toolkit.getDefaultToolkit().getImage("mars.png");
        shuttlePic = Toolkit.getDefaultToolkit().getImage("shuttle.png");
        venuspic = Toolkit.getDefaultToolkit().getImage("venuspic.png");
        mercuryPic = Toolkit.getDefaultToolkit().getImage("mercury.png");
        saturnPic = Toolkit.getDefaultToolkit().getImage("saturn.png");
        uranusPic = Toolkit.getDefaultToolkit().getImage("uranus.png");
        jupiterPic = Toolkit.getDefaultToolkit().getImage("Jupiter.png");
        neptunePic = Toolkit.getDefaultToolkit().getImage("neptune.png");

        scale = 1;

        System.out.println("DONE graphic setup");
    }

    public void setUpPlanets(){
        movingObjs = new ArrayList<MovingObj>();

        MovingObj sun = new MovingObj("Sun", 10000, 80, 80, 200, 200, Color.YELLOW, 0, 0, sunPic, false);
        movingObjs.add(sun);
        MovingObj earth = new MovingObj("Earth", 600, 20, 20,200, 400, Color.BLUE, sun, earthPic, false);
        movingObjs.add(earth);
        MovingObj mars = new MovingObj("Mars", 650, 20, 20,-50, 200, Color.RED, sun, marsPic, false);
        movingObjs.add(mars);
        MovingObj shuttle = new MovingObj("Shuttle", 1 ,6, 12, 200, 420, Color.WHITE, earth, shuttlePic, true);
        movingObjs.add(shuttle);
        MovingObj venus = new MovingObj("Venus", 480, 20, 20,300, 200, Color.ORANGE, sun, venuspic,false);
        movingObjs.add(venus);
        MovingObj mercury = new MovingObj("Mercury", 480, 20, 20,400, 100, Color.pink, sun, mercuryPic,false);
        movingObjs.add(mercury);
        MovingObj jupiter = new MovingObj("Jupiter", 480, 20, 20,-250, 300, Color.yellow, sun, jupiterPic,false);
        movingObjs.add(jupiter);
        MovingObj saturn = new MovingObj("Saturn", 480, 20, 20,-350, 400, Color.white, sun, saturnPic,false);
        movingObjs.add(saturn);
        MovingObj uranus = new MovingObj("Uranus", 480, 20, 20,-450, 500, Color.blue, sun, uranusPic,false);
        movingObjs.add(uranus);
        MovingObj neptune = new MovingObj("Neptune", 480, 20, 20,-550, 600, Color.cyan, sun, neptunePic,false);
        movingObjs.add(neptune);
        focusedObj = sun;
    }

    public void calcGravity(){
        for(int i=0; i<movingObjs.size(); i++){
            for(int j=0; j<movingObjs.size(); j++){
                if(i==j){
                    continue;
                }
                double dx = (movingObjs.get(i).xPos+movingObjs.get(i).sizeX/2) - (movingObjs.get(j).xPos+movingObjs.get(j).sizeX/2);
                double dy = (movingObjs.get(i).yPos+movingObjs.get(i).sizeY/2) - (movingObjs.get(j).yPos+movingObjs.get(j).sizeY/2);
                double r = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

                double force = crunchGrav(movingObjs.get(i).mass, movingObjs.get(j).mass, r);
                double rad;

                rad = Math.atan(dy/dx);
                if(dx>0) {
                    rad += Math.PI;
                }

                Vector vect = new Vector(rad, force);
                movingObjs.get(i).vectors.add(vect);
            }
        }
    }

    public void render(){
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        shiftX = (int) (focusedObj.xPos + focusedObj.sizeXDraw/2 - WIDTH/2) * -1;
        shiftY = (int) (focusedObj.yPos + focusedObj.sizeYDraw/2 - HEIGHT/2) * -1;

        for(int i=0; i<movingObjs.size(); i++){
            movingObjs.get(i).move();
            movingObjs.get(i).scale(scale, focusedObj);
            g.setColor(movingObjs.get(i).color);
            g.drawString(movingObjs.get(i).name, (int) (movingObjs.get(i).xPosDraw + shiftX), (int) (movingObjs.get(i).yPosDraw-10 + shiftY));
            g.drawImage(movingObjs.get(i).pic, (int) (movingObjs.get(i).xPosDraw) + shiftX, (int) (movingObjs.get(i).yPosDraw) + shiftY, movingObjs.get(i).sizeXDraw, movingObjs.get(i).sizeYDraw, null);
            g.setColor(Color.YELLOW);
            g.drawString( movingObjs.get(i).name + " x: " + movingObjs.get(i).xPos + " y: " + movingObjs.get(i).yPos, 10,  20+20*i);
        }

        g.dispose();
        bufferStrategy.show();
    }

    public double crunchGrav(double m1, double m2, double r){
        double G = 0.0005;
        return(G*((m1*m2)/Math.pow(r, 2)));
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyChar()){
            case 'w':
                if(focusedObj.canMove) {
                    focusedObj.yPos -= 2;
                }
                break;
            case 's':
                if(focusedObj.canMove) {
                    focusedObj.yPos += 2;
                }
                break;
            case 'd':
                if(focusedObj.canMove) {
                    focusedObj.xPos += 2;
                }
                break;
            case  'a':
                if(focusedObj.canMove) {
                    focusedObj.xPos -= 2;
                }
                break;
            case 'r':
                int index = movingObjs.indexOf(focusedObj);
                if(index == movingObjs.size() -1){
                    index = 0;
                } else {
                    index++;
                }
                focusedObj = movingObjs.get(index);
                break;
            case 'p':
                scale += 0.1;
                break;
            case 'l':
                scale -= 0.1;
                break;
        }
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
