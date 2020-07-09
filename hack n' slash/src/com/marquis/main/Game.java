package com.marquis.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.marquis.entities.Entity;
import com.marquis.entities.Player;
import com.marquis.graphics.Spritesheet;


public class Game extends Canvas implements Runnable,KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	public static JFrame frame;
	private final int WIDTH = 230;
	private final int HEIGHT = 120;
	private final int SCALE = 3;
	private Thread thread;
	private boolean isRunning = true;
	private BufferedImage image;
	public List<Entity> entities;
	public Spritesheet spritesheet;
	public static Player p;

	public Game() {
		addKeyListener(this);
		image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("/spritesheet1.png");
		Player p = new Player(0, 0, 32, 32, spritesheet.getSprite(0, 96, 32, 32));
		entities.add(p);
	}
	public void initFrame() {
		frame = new JFrame("Aventuras de um GOY");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Game game = new Game();
		game.start();		
	}
	public void tick() {
		for(int i = 0; i < entities.size(); i ++) {			
			Entity e = entities.get(i);
			e.tick();
		}
	}
	public void render() {
		//Criando buffer
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		//definindo g como parte do package Graphics.
		Graphics g = image.getGraphics();
		//Definindo a cor do canvas		
		g.setColor(Color.green);
		g.fillRect(0, 0, WIDTH, HEIGHT);		 
		//renderizando entities;
		for(int i = 0; i < entities.size(); i ++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		//definindo g2 como parte da extensão Graphics2D do package Graphics.
		//Graphics2D g2 = (Graphics2D) g;
		//Renderizando o jogador manualmente (sem a classe Player para definir o player.render();
		//g.drawImage(player,xI,yI, null);
		//definindo g como parte do bufferstrategy.
		g = bs.getDrawGraphics();
		//limpando a tela para corrigir a ''piscação'' do java
		g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		bs.show();
	}
	public void run() {
		//definindo o último momento em nano segundos
		long lastTime = System.nanoTime();
		//quantidade de ticks
		double amountOfTicks = 60.0;
		//variável de nanosegundos para definir o máx de fps
		double ns = 1000000000 / amountOfTicks;
		//controle
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		//loop principal
		while(isRunning) {
			long now = System.nanoTime();
			delta+=(now - lastTime)/ns;
			lastTime = now;
			if (delta>= 1){
				tick();
				render();
				frames++;
				delta--;				
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS "+frames); 
				frames = 0;
				timer = System.currentTimeMillis();
			}			
		}
		stop();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT
				||e.getKeyCode() == KeyEvent.VK_D){
			p.setRight(true);
		}		
	}
	@Override
	public void keyReleased(KeyEvent e) {				
		// TODO Auto-generated method stub		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
	