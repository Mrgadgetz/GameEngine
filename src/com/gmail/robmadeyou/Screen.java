package com.gmail.robmadeyou;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.gmail.robmadeyou.Gui.Interface;
import com.gmail.robmadeyou.Input.Keyboard;
import com.gmail.robmadeyou.Input.Keyboard.Key;
import com.gmail.robmadeyou.Input.Mouse;
import com.gmail.robmadeyou.World.World;
import com.gmail.robmadeyou.Effects.Color;
import com.gmail.robmadeyou.Effects.Textures;
import com.gmail.robmadeyou.Entity.Entity;
import com.gmail.robmadeyou.Entity.EntityList;
import com.gmail.robmadeyou.Entity.Player;
import com.gmail.robmadeyou.Gui.Fonts;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

/*
 * So all this stuff is made by Robmadeyou.
 * 
 * I'm okay with you using any of this. I've deliberately commented on the code to hopefully
 * show you what I tried doing and in hopes of you being able to learn something from the code.
 * I have used the LWJGL library for openGL rendering and such. I also used Slick for texture loading
 * and sound. This way the coding of the game is going to be a bit easier than having to code it all
 * yourself and just wasting hours trying to figure out how things are done.
 * 
 * I build this for CoderDojo in Armagh. Jamie Kelly is somewhat responsible for all of this happening ((https://github.com/jamiekelly))
 * as he is the one suggested that something like this would be nice if was made.
 */

public class Screen {
	
	public static String engineName = "Rob\'s Engine: ";
	public static String version = "0.1";
	
	public static boolean detailsActive = false;
	private static long lastFrame;
	private static long lastFPS;
	public static int fps = 0;
	public static int actualFps = 0;
	
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			actualFps = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	/*
	 * Delta time is the time taken between each frame
	 * 
	 * Delta time is used for animations and movement to add a 
	 * frame independent movement.
	 * 
	 * For example instead of adding + 5 every frame and if the user is using a 
	 * quite fast end PC it will move faster than for someone with a lower end PC, where 
	 * as frame independent movement would add the same amount each time to make the movement 
	 * seem fluid for both low end and high end computers. This is very useful when doing things through
	 * Multi-Player as one person might have a very fast computer and the other might have a slow one, the slow one
	 * is at a disadvantage as they can't move as fast
	 */
	private static int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}
	
	/*
	 * These two methods simply return the screen dimensions;
	 */
	public static int getWidth(){
		return Display.getWidth();
	}
	public static int getHeight(){
		return Display.getHeight();
	}
	public static boolean isAskedToClose(){
		if(Display.isCloseRequested()){
			return true;
		}
		return false;
	}
	public static GameType TypeOfGame = GameType.SIDE_SCROLLER;
	
	public static double translate_x = 0;
	public static double translate_y = 0;
	
	public static int WorldTileSize = 32;
	
	public static int WorldWidth = 0;
	public static int WorldHeight = 0;
	
	public static void createScreen(int dimensionX, int dimensionY, String name, GameType typeOfGame, boolean Minimalistic){
		long startTimer = getTime();
		TypeOfGame = typeOfGame;
		try{
			/*
			 * Here the Display that we are going to be using is created with the 
			 * dimensions called in the parameters when the method was called
			 */
			Display.setDisplayMode(new DisplayMode(dimensionX, dimensionY));
			/*
			 * Sets the title of the Display to be what ever the parameter in the method
			 */
			Display.setTitle(name);
			Display.create();
			Display.setResizable(false);
		
		}
		/*
		 * Catching an exception in case a Display has already been created or some other error.
		 */
		catch(LWJGLException e){
			e.printStackTrace();
			System.out.println("Error. Unable to create a Display");
		}
		lastFPS = getTime();
		if(!Minimalistic){
			glEnable(GL_TEXTURE_2D);
			glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
			glViewport(0,0,dimensionX,dimensionY);
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			glOrtho(0, dimensionX, dimensionY, 0, 1, -1);
			glMatrixMode(GL_MODELVIEW);
		}
		/*
		 * Initialising WorldWidth, and Height as before that
		 * the screen wasn't created and we couldn't actually initialise the variables :S
		 */
		if(WorldWidth == 0 && WorldHeight == 0){
			WorldWidth = Math.round(getWidth() / 32);
			WorldHeight = Math.round(getHeight() / 32) + 1;
		}
		Fonts.setUpTextures();
		System.out.println(engineName + "Font set up: " + Fonts.texSetUp);
		Textures.setUpTextures();
		System.out.println(engineName + "Textures set up: " + Textures.texSetUp);
		
		World.setWorldDimensions(WorldWidth, WorldHeight);
		World.setArrayListClear();
		
		long endTimer = getTime() - startTimer;
		double finishTime = endTimer / 1000;
		System.out.println(engineName +  "v" + version + " Loaded in: " + finishTime + " seconds");
	}
	public static void setWorldDimensionsInBlocks(int numOfBlocksOnXAxis, int numOfBlocksOnYAxis){
		WorldWidth = numOfBlocksOnXAxis;
		WorldHeight = numOfBlocksOnYAxis;
	}
	public static void setWorldBlockSizeInPixels(int size){
		WorldTileSize = size;
	}
	/*
	 * This method will be called from the Main class in the game.
	 * This method will update everything to do with the screen.
	 * 
	 * It will be synchronised at the rate of the int rate is set to be at
	 */
	public static void screenUpdate(int rate){
		//This will clear the screen before drawing it again.
		glClear(GL_COLOR_BUFFER_BIT);
			updateFPS();
			World.onUpdate();
			Mouse.onUpdate();
			Interface.onUpdate();
			EntityList.updateAllEntities(getDelta());
			
			if(Keyboard.isKeyPressed(Key.Grave)){
				if(detailsActive == false){
					detailsActive = true;
				}else{
					detailsActive = false;
				}
			}
			
			if(detailsActive){
				Fonts.drawString("camX " + translate_x, 0, 20, 1, Color.Red);
				Fonts.drawString("camY " + translate_y, 0, 30, 1, Color.Red);
				Fonts.drawString("FPS: " + actualFps, 5, 40, 1, Color.Black);
			}
		Display.sync(60);
		Display.update();
	}
	
	public enum GameType{
		SIDE_SCROLLER,
		RPG_STYLE,
		FREE,
		CUSTOM;
		GameType(){
		}
	}
}
