import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;

import javax.swing.*;


public class ChromeDinosaur extends JPanel implements ActionListener , KeyListener{
    int boardWidth = 750;
    int boardHeight = 250;
 
    Image dinosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image birdRunning;
    Image birdDeadImage;



    class Block{
        int x;
        int y;
        int height;
        int width;
        Image img;

        Block(int x, int y, int width, int height,Image img ){
            this.x = x;
            this.y= y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //dinosaur
    int dinosaurWidth = 88 ;
    int dinosaurHeight = 94;
    int dinosaurX =  50;
    int dinosaurY = boardHeight - dinosaurHeight;

    Block dinosaur;

    //cactus 
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray ;


    //bird 
    int birdWidth = 30;
    int birdHeight = 30;
    int birdX = boardWidth - birdWidth;
    int birdY =  10; 
    Block bird;

    //physics 
    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;
    int birdVelocityX = 0;
    int birdSpeedX = -1;

    boolean gameOver = false;
    int score = 0;

    Timer gameLoop;
    Timer placeCactusTimer;
    Timer birdLoop;
    
    public ChromeDinosaur(){
        setPreferredSize(new Dimension (boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);   //this JPanel is allowed to listen to the keyevent 
        addKeyListener(this);

        loadImages();

        //dinosaur
        dinosaur = new Block(dinosaurX,dinosaurY,dinosaurWidth,dinosaurHeight,dinosaurImg);

        //cactus
        cactusArray = new ArrayList<Block>();

        //bird 
        bird = new Block(birdX,birdY,birdWidth,birdHeight,birdRunning);

        //game timer
        gameLoop = new Timer(1000/60 , this); //1000/60 = 60 frames per 1000ms(1s) , update
        gameLoop.start();

        //cactus timer
        placeCactusTimer = new Timer(1500,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                placeCactus();
            }
        });
        placeCactusTimer.start();

        //bird timer
        birdLoop = new Timer(1000000000,this);
        birdLoop.start();
    }

    void loadImages(){
        dinosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino-jump.png")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();
        birdRunning = new ImageIcon(getClass().getResource("./img/bird.gif")).getImage();
        birdDeadImage = new ImageIcon(getClass().getResource("./img/bird1.png")).getImage();
    }

    void placeCactus(){
        if(gameOver) return;

        double placeCactusChance = Math.random();  //0-0.99999
        if(placeCactusChance > .90){
            Block cactus = new Block(cactusX,cactusY,cactus3Width,cactusHeight,cactus3Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance >.70){
            Block cactus = new Block (cactusX,cactusY, cactus2Width,cactusHeight,cactus2Img);
            cactusArray.add(cactus);
        }
        else if(placeCactusChance > .50){
            Block cactus = new Block(cactusX,cactusY,cactus1Width,cactusHeight,cactus1Img);
            cactusArray.add(cactus);
        }
        if(cactusArray.size() > 10){
            cactusArray.remove(0); //remove the first cactus from ArrayList
        }
    }

    public void paintComponent(Graphics g ){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //dinosaur
        g.drawImage(dinosaur.img,dinosaur.x,dinosaur.y,dinosaur.width,dinosaur.height,null);

        //cactus
        for(int i =0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img,cactus.x,cactus.y,cactus.width,cactus.height,null);
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Courier",Font.PLAIN,32));
        if(gameOver){
            g.drawString("Game Over : " + String.valueOf(score),10,35);
        }
        else{
            g.drawString(String.valueOf(score),10,35);
        }

        //bird
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

    }

    public void move(){
        //dinosaur movement
        velocityY += gravity;
        dinosaur.y += velocityY;

        if(dinosaur.y > dinosaurY){
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = dinosaurImg;
        }

        //cactus
        for(int i = 0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if(collision(dinosaur , cactus)){
                gameOver = true;
                dinosaur.img = dinosaurDeadImg;
            }
        }

        //score
        score++;

        //bird 
        birdVelocityX += birdSpeedX;
        bird.x += birdVelocityX;
        if(bird.x <0){
            bird.x = birdX;
            birdVelocityX = 0;
            bird.img = birdDeadImage;
        }
        
    }


    //collison 
    boolean collision(Block a , Block b){
        return a.x < b.x + b.width && 
               a.x + a.width > b.x &&
               a.y < b.y + b.height &&
               a.y + a.height > b.y; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placeCactusTimer.stop();
            gameLoop.stop();
            birdLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(dinosaur.y == dinosaurY){
                velocityY = -17;
                dinosaur.img = dinosaurJumpImg;
            }
            if(gameOver){
                //restart game by resetting conditions 
                dinosaur.y = dinosaurY;
                dinosaur.img = dinosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
