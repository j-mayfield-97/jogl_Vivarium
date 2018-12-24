import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import com.sun.javafx.geom.Vec3d;
import java.lang.Object;
import java.util.*;

//The class that all creatures in the scene will inherit from
public class Creature 
{
	//display list for animations
	protected int creature_animation;
	protected int creature_animation2;
	protected int creature_animation3;
	protected int creature_animation4;
	protected int creature_animation5;
	protected int creature_animation6;
	
	protected int ticker;
	  //variables relating to objects the transformations
	  protected Hitboxes hitbox;
	  public float x_translation, y_translation, z_translation;

	  protected float x_velocity;
	  protected float y_velocity;
	  protected float z_velocity;
	  
	  protected float init_x_velocity;
	  protected float init_y_velocity;
	  protected float init_z_velocity;

	  protected float rand_x, rand_y, rand_z;
	  
	  //color will be set randomly
	  private float color_r, color_g, color_b;
	  
	  //random object for rand float generation
	  protected Random r;
	  
	  //set the boundary's of the walls immutable to make sure the creature cannot leave the tank
	  private static float boundary;
	  
	  //int to decide behaviors
	  //0 is prey
	  //1 is predator
	  //2 is food
	  protected int behavior;
	  
	 // variable relating to other creatures around this crture
	  protected Vec3d position, target_pos, predator_pos;
	  
	  protected Creature current_target = null;
	  protected Creature pred_target = null;
	  protected double target_dist, predator_dist;
	  
	  //caught will determine if creature is to be despawned
	  public boolean caught;

	  
	  //constructor based on tank size
	public Creature(float tank_size)
	{
		///do some error prevention 
		  if(tank_size < 4)
		  {
			  //throw error();
			  System.out.println("Tank size Cant be less than 4 units");
		  }
		// init variables
	    
	    //counter for animations
	    ticker = 0;
	    
	    //boundary is always half tanksize
	    boundary = tank_size / 2;
	    
	    //instantiate the random object and seed based on time to get nearest to pure random
	    Random trueRandom = new Random();
	    trueRandom.setSeed(System.nanoTime());
	    r = trueRandom;
	    
	    //init the random colors
	    color_r = r.nextFloat();
	    color_g = r.nextFloat();
	    color_b = r.nextFloat();
	    
	    //its init location and color are related
	    //keep the random location with in the boundaryy
	    x_translation = (color_r * 2 * boundary)  - boundary;
	    y_translation = (color_g * 2 * boundary)  - boundary;
	    z_translation = (color_b * 2 * boundary)  - boundary;
	    
	    //this lets a random initial location get picked later in the code
	   // previous_collision = false;
	    
	    //init vectors

	    target_pos = new Vec3d(0, 0, 0);
	    position = new Vec3d(0, 0, 0);
	    predator_pos = new Vec3d(0, 0, 0);
	    
	    target_dist = boundary * 2 + 1;
	    predator_dist = boundary * 2 + 1;
	    
	    //caught starts as false true if collision with target
	    caught = false;

	}

	//potential function can attract or repel based on the input
	protected Vec3d potential(boolean attract, Vec3d other)
	{
		Vec3d p = new Vec3d(0,0,0);		
		Vec3d t = new Vec3d(0,0,0);
		Vec3d ret_vec = new Vec3d(0,0,0);
		
		//set first to this location, set second to target location
		p.set(position);
		t.set(other);
		
		//if true set vec3 to the difference between other and itself
		//else set vec3 to the reverse
		if(attract)
			ret_vec.sub(t, p);
		else
			ret_vec.sub(p, t);
		
		
		ret_vec.mul(2);
		
		//ret_vec.normalize();
		
		return ret_vec;
		
	}
	
	//get prey and predator locations
	public void obtain_target_location(Creature t)
	{
		//distance calculation uptop
		double xdif = (t.position.x - position.x) * (t.position.x - position.x);
		double ydif = (t.position.y - position.y) * (t.position.y - position.y);
		double zdif = (t.position.z - position.z) * (t.position.z - position.z);
		double dist = Math.sqrt(xdif + ydif + zdif);
		
		//shark behavior towards fish
		if(behavior == 1 && t.behavior == 0)
		{
			//set current target if not already set
			if(current_target == null)
			{
				current_target = t;
				target_dist = dist;
				target_pos.set(t.position.x, t.position.x, t.position.x);
			}
			
			else
			{
				if(dist < target_dist)
				{
					//if the new prey is closer then it becomes the target
					
					current_target = t;
					target_dist = dist;
					target_pos.set(t.position.x, t.position.x, t.position.x);
				}
			}
		}
		
		 /// for fish to just go after nearest food
		if(behavior == 0 && t.behavior == 2)
		{
			//set current target if not already set
			if(current_target == null)
			{
				current_target = t;
				target_dist = dist;
				target_pos.set(t.position.x, t.position.x, t.position.x);
			}
			else
			{
				if(dist < target_dist)
				{
					//if the new prey is closer then it become the target
					
					current_target = t;
					target_dist = dist;
					target_pos.set(t.position.x, t.position.x, t.position.x);
				}
			}
		}
		//set predator distance
		if(behavior == 0 && t.behavior == 1)
		{
			predator_pos.set(t.x_translation, t.y_translation, t.z_translation);
			predator_dist = dist;

		}
	}

	//get the angle the creature needs to turn to face its target
	protected float turn_to_target()
	{
		double Xdir = position.x - target_pos.x;
		double ydir = position.y - target_pos.y;
		
		return (float) Math.atan2(-ydir, Xdir);
	}

	
	//this will need to be called on each creature for every other creature every frame.
	// seems inefficient
	  public void collision_decision(Creature other_creature)
	  {
		  Hitboxes hb = other_creature.hitbox;
		  //this basically just makes the creature reverse direction if a collision happens
			  if(hitbox.collision_check(hb) && !(other_creature.behavior == 2 && behavior == 1))
			  {
				  //handle food being eaten
				  if(behavior == 0 && other_creature.behavior == 2)
				  {
					  other_creature.caught = true;
				  }
				  //handle fish being eaten
				  if(behavior == 1 && other_creature.behavior == 0)
				  {
					  other_creature.caught = true;
				  }
				  
					  //to prevent getting caught in a corner and a creature
				  if((y_translation >= boundary || y_translation <= -boundary)||
						  (x_translation >= boundary || x_translation <= -boundary)||
						  z_translation >= boundary || z_translation <= -boundary)
				{
					  x_translation -= rand_x/2 + hb.radius ; 
					  y_translation -= rand_x/2 + hb.radius ; 
					  z_translation -= rand_x/2 + hb.radius ;
				}
				  else 
				  {
					  //reflect the creatures velocity
				  x_velocity = -x_velocity;
				  y_velocity = -y_velocity;
				  z_velocity = -z_velocity;
				  }

			  }
	  }
	
	
	public void init(GL2 gl)
	{
		//function only meant to be a virtual function in child classes
	}
	
	protected void animation_ticker(GL2 gl)
	{
		//function only meant to be a virtual function in child classes
	}
	
	public void update(GL2 gl)
	{
		Vec3d wallX = new Vec3d(boundary,0,0);
		Vec3d wallNegX = new Vec3d(-boundary,0,0);
		Vec3d wallY = new Vec3d(0,boundary,0);
		Vec3d wallNegY = new Vec3d(0,-boundary,0);
		Vec3d wallZ = new Vec3d(0,0,boundary);
		Vec3d wallNegZ = new Vec3d(0,0,-boundary);
		
		x_velocity += (float) (potential(false, wallX).x ) / 1000;
		y_velocity += (float) (potential(false, wallY).y )/ 1000;
		z_velocity += (float) (potential(false, wallZ).z )/ 1000;
		
		x_velocity += (float) (potential(false, wallNegX).x )/ 1000;
		y_velocity += (float) (potential(false, wallNegY).y )/ 1000;
		z_velocity += (float) (potential(false, wallNegZ).z )/ 1000;
		
		float minV = 0.01f;
		float maxV = 1.0f;
		
		if(x_velocity > maxV || Math.abs(y_velocity) > maxV || Math.abs(z_velocity) > maxV)
		{
			
		}
		
	    ///////////////////this can be cleaned
	    //for now if the origin point of the creature passes the boundary set it at boundary and have the velocity flip 
	    if(x_translation > boundary )
	    {
	    	x_velocity = (float) (potential(false, wallX).x )/ 500;
	    	x_translation = boundary;
	    //	x_velocity = -x_velocity;
	    }
	    if(x_translation < -boundary)
	    {
			x_velocity = (float) (potential(false, wallNegX).x )/ 500;

	    	x_translation = -boundary;
	    	//x_velocity = -x_velocity;
	    }
	    if(y_translation > boundary )
	    {
	    	y_translation = boundary;
	    	y_velocity = (float) (potential(false, wallY).y )/ 500;	    
	    }
	    if(y_translation < -boundary)
	    {
	    	y_translation = -boundary;
	    	y_velocity = (float) (potential(false, wallNegY).y )/ 500;
	    }
	    if(z_translation > boundary )
	    {
	    	z_translation = boundary;
	    	x_velocity = (float) (potential(false, wallX).x )/ 550;	    
	    }
	    if(z_translation < -boundary)
	    {
	    	z_translation = -boundary;
	    	z_velocity = (float) (potential(false, wallNegZ).z )/ 500;
	    }
	    //food can't move but the other creatures can
	    if(behavior != 2) 
	    {
	    //make translations more smooth later
	    x_translation += x_velocity;
	    y_translation += y_velocity;
	    z_translation += z_velocity;
	    }
	    
	    position.set(x_translation, y_translation, z_translation);
	    
	    if(Math.abs(target_dist) < (boundary * 2) / 4 ) 
	    {
		    if(behavior == 0)
		    {
			    x_velocity += (float) (potential(false, target_pos).x / 1500);
				y_velocity += (float) (potential(true, target_pos).y / 1500);
				z_velocity += (float) (potential(true, target_pos).z / 1500);
		    }
	    } 
		    if(behavior == 1 && Math.abs(target_dist) < (boundary * 2) )
		    {
			    x_velocity += (float) (potential(true, target_pos).x / 3000);
				y_velocity += (float) (potential(true, target_pos).y / 3000);
				z_velocity += (float) (potential(true, target_pos).z / 3000);
		    }
		    else {
		    	if(x_velocity < 0.00001 && y_velocity < 0.00001 && z_velocity < 0.00001)
			    {
			    	 x_velocity = init_x_velocity;
					   y_velocity = init_y_velocity;
					   z_velocity = init_z_velocity;
			    }
		    }
		    
	   
	    //&& predator_dist < (boundary * 2)
	    if(behavior == 0 && predator_dist < (boundary * 2) / 2)
	    {
	    	x_velocity += (float) (potential(false, predator_pos).x / 2000);
			y_velocity += (float) (potential(false, predator_pos).y / 2000);
			z_velocity += (float) (potential(false, predator_pos).z / 2000);
	    }

		//update hitbox coords
	   hitbox.update_coords(x_translation, y_translation, z_translation);
	   
	   if(current_target != null )
	   {
		   if(current_target.caught) {
		 //store initial velocities
		   x_velocity = init_x_velocity;
		   y_velocity = init_y_velocity;
		   z_velocity = init_z_velocity;
		   current_target = null;
		   }
	   }
	   else
	   {
		   if(Math.abs(x_velocity) < minV)
			{
				//	x_velocity += (r.nextFloat() - 0.5f)/100 ;
			}
			if(Math.abs(z_velocity) < minV)
			{
				//z_velocity += (r.nextFloat() - 0.5f)/100 ;

			}
			if(Math.abs(y_velocity) < minV)
			{
			//	y_velocity += (r.nextFloat() - 0.5f)/100 ;

			}
	   }
	   
	}
	
	public void draw(GL2 gl)
	{
		//start matrix to adjust creature in space
		gl.glPushMatrix();
	    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
	    
	    //move the creature
	    gl.glTranslatef(x_translation, y_translation, z_translation);
	    
	    //turn the creature in direction of prey
	     gl.glRotatef(turn_to_target() * 57, 0, 1, 0);
	    
	    //i dont have a way to make sure the creatures color isn't pure black yet but it's very unlikely
	     //if(colors near black) throw error or jsut get a new random 
	    gl.glColor3f( color_r, color_g, color_b); // draw color
	    
	    //main unchanging body
	    gl.glCallList( creature_animation );
	    
	    //ticker for changing animations
	    animation_ticker( gl );
	    
	    //end matrix
	    gl.glPopAttrib();
	    gl.glPopMatrix();
	}
}
