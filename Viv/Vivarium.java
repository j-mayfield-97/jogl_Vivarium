

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium
{
	//tank what keeps all the creatures
  private Tank tank;
  //size of the tank, it starts at the origin so half span in positive and negative directions
  ////you can make a tank depth with and height instead
  public float TANK_SIZE;
  
  	//declare all the objects in the tank
  private Creature fish;
  private Creature fish2;
  private Fish fish3;
  private Fish fish4;
  private Fish fish5;
  private Shark shark;
  private Shark shark2;
  
  //main vector to store creatures that will be iterated over
  Vector<Creature> all_creatures = new Vector<Creature>();

  public Vivarium()
  {
	  
	  //Instantiate the objects in the scene
	TANK_SIZE = 10.0f;
	
	//only allowing for a cube tank
    tank = new Tank(TANK_SIZE, TANK_SIZE, TANK_SIZE);
    
    fish = new Fish(TANK_SIZE);
    fish2 = new Fish(TANK_SIZE);
    fish3 = new Fish(TANK_SIZE);
    fish4 = new Fish(TANK_SIZE);
    fish5 = new Fish(TANK_SIZE);
    
    shark = new Shark(TANK_SIZE);
    shark2 = new Shark(TANK_SIZE);
    
    //Add creatures to the main vector 
    
    //all_creatures.add(shark2);
    
    all_creatures.add(fish3);
    all_creatures.add(fish4);
    all_creatures.add(fish5);
    
    // add extra creatures to test out 
    // all_shark.add(fish);
    //all_shark.add(fish2);
    //all_creatures.add(shark);
  }
/*
 * All of tanks functions get run uptop
 * plan to have the for each loops iterate over a vector of all creatures
 * */
  public void init( GL2 gl )
  {
    tank.init( gl );
    
    	for(Creature c : all_creatures)
        {
        	c.init( gl );
        }
  }

  public void update( GL2 gl )
  {
    tank.update( gl );
    
    Vector<Creature> remove_list = new Vector<Creature>();
    
    for(Creature C : all_creatures)
    {	    
		for(Creature other_C: all_creatures)
        {
    		if (C != other_C)
    		{
    			C.obtain_target_location(other_C);
    			C.collision_decision(other_C);
    		}
        }
		
    	C.update( gl );
    	if(C.caught)
    	{
    		remove_list.add(C);
    	}
    }
    //remove the sublist of creatures from the main vector
    all_creatures.removeAll(remove_list);

  }

  //drawing functions
  public void draw( GL2 gl )
  {
    tank.draw( gl );
    
    for(Creature c : all_creatures)
    {
    	c.draw( gl );
    }
  }
  
  //function to delete creature at index if index is in range
  //right now it only deletes the first creature in the first veector
  public void destroy(int index)
  {		  
  }
  
  public void add_creature(boolean bool, GL2 gl)
  {
	  Creature food = new Food(TANK_SIZE);
	  
	  if(bool) 
	  {
  		all_creatures.add(food);
  		food.init(gl);
	  }
  }
}
