package com.gmail.robmadeyou;

import java.util.ArrayList;
import java.util.Random;

import com.gmail.robmadeyou.Effects.Animate;
import com.gmail.robmadeyou.Effects.Emitter;
import com.gmail.robmadeyou.Entity.Entity;

public class Engine {
	
	static ArrayList<Emitter> Emitters = new ArrayList<Emitter>();
	public static Emitter addNewEmitter(Emitter e){
		Emitters.add(e);
		return Emitters.get(Emitters.size() - 1);
	}
	public static void updateAllEmitters(int delta){
		for(int i = 0; i < Emitters.size(); i++){
			Emitters.get(i).onUpdate(delta);
		}
	}
	public static void update(int delta){
		updateAllEmitters(delta);
	}
	/*
	 * 
	 * 
	 * 	CODE FOR ADDING ENITITES
	 * 
	 * 
	 * 
	 */
	public static ArrayList<Entity> entityList = new ArrayList<Entity>();
	
	public static void addEntity(Entity e){
		entityList.add(e);
		Random ran = new Random();
		int id = ran.nextInt(1024);
		boolean hasIDSet = false;
		while(!hasIDSet){
			for(int i = 0; i < entityList.size(); i++){
				if(entityList.get(i).getNumber() == id){
					id = ran.nextInt(1024);
					break;
				}
			}
			entityList.get(entityList.size() - 1).setNumber(id);
			hasIDSet = true;
		}
	}
	public static void updateAllEntities(int delta){
		for(int i = 0; i < entityList.size(); i++){
			entityList.get(i).onUpdate(delta);
		}
	}
	public static Entity getEntityByNumber(int number){
		for(int i = 0; i < entityList.size(); i++){
			if(entityList.get(i).getNumber() == number){
				return entityList.get(i).getType();
			}
		}
		return null;
	}
	
	/*
	 * 
	 * CODE FOR ANIMATION
	 * 
	 * 
	 */
	public static ArrayList<Animate> animID = new ArrayList<Animate>();
	
	public static Animate createAnimation(Animate animation){
		animID.add(animation);
		return animID.get(animID.size() - 1); 
	}
	
}