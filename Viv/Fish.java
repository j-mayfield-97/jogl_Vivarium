

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Fish extends Creature
{
	float tail_size;
	float bod_size;
	float scale;
	int mover;

	public Fish(float tank_size) {
		super(tank_size);
		// TODO Auto-generated constructor stub
	}
	public void init(GL2 gl)
	{
	    //set behavior
	    behavior = 0;
	    
	    //get random floats
	    rand_x = r.nextFloat();
	   	rand_y = r.nextFloat();
	   	rand_z = r.nextFloat();
	   	
	   	//constants relating to size of creature should go somewhere else probably
	   	float min_size_shp = 0.07f + (rand_x / 2);
	   	float min_size_con = 0.3f + rand_x;
	   	float max_size_shp = 0.4f;
	   	float max_size_con = 0.6f;
	   	
	   	//make sure the fish isnt too large or too small
	   	if(min_size_shp > max_size_shp)
	   		min_size_shp = max_size_shp;
	   	if(min_size_con > max_size_con)
	   		min_size_con = max_size_con;
	   	
		//fish scale along X axis
	   	float scalarX = 2.0f;
	   	
	  //init creature object
	    creature_animation = gl.glGenLists(1);
	    gl.glNewList( creature_animation, GL2.GL_COMPILE );
	    // create the creatures object 
	    GLUT glut = new GLUT();
	    
	  //body formation
	    gl.glPushMatrix();
	    //make creature longer along the x-axis
	    gl.glScalef(scalarX, 1.0f, 1.0f);
	    glut.glutSolidSphere(min_size_shp, 30, 30);
	    gl.glPopMatrix();
	    
	    //tail
	    gl.glPushMatrix();
	    gl.glRotatef(90, 0.0f, 1.0f, 0.0f );
	    gl.glTranslatef(0.f, 0, (float) (-(min_size_shp) - (min_size_shp * 1.5)));
	    glut.glutSolidCone( 0.2, min_size_con, 30, 30 ); 
	    gl.glPopMatrix();
	    //end main body
	    gl.glEndList();
	    
	  
	    
	    //instantite the hitbox at the coordinates with a radius of the size in the x axis
	    hitbox = new Hitboxes(x_translation, y_translation, z_translation, min_size_shp * 1.5f);//multiply times 2 because sphere was scales by 2 
	    
	    //set initial velocities
	    x_velocity = (rand_x / 25);
	    y_velocity = (rand_y / 20);
	    z_velocity = (rand_z / 25);
	    
	  //store initial velocities
	    init_x_velocity = x_velocity;
	    init_y_velocity = y_velocity;
	    init_z_velocity = z_velocity;
	    
	    //set initial position
	    position.set(x_translation, y_translation, x_translation);
	    
	    //get the size of the cone and of the sphere for later use
	    bod_size = min_size_shp;
	    tail_size = min_size_con;
	    scale = scalarX;
	    mover = 0;

	}
	
	protected void animation_ticker(GL2 gl)
	{
		GLUT glut = new GLUT();
		
		//init animation
	    creature_animation2 = gl.glGenLists(1);
	    gl.glNewList( creature_animation2, GL2.GL_COMPILE );
	  //fin 1
	    gl.glPushMatrix();
	    gl.glRotatef(mover, 1.0f, 0.0f, 0.0f );
	    gl.glTranslatef(0.0f, bod_size - 0.2f, 0.0f);
	    glut.glutSolidCone( tail_size / 4, tail_size * 1.5, 30, 30 ); 
	    gl.glPopMatrix();
	  //fin 2
	    gl.glPushMatrix();
	    gl.glRotatef(90, 0.0f, 1.0f, 0.0f );
	    gl.glRotatef(mover, 1.0f, 0.0f, 0.0f );
	    gl.glTranslatef(0.0f, bod_size - 0.2f, 0.0f);
	    glut.glutSolidCone( tail_size / 4, tail_size * 1.5, 30, 30 ); 
	    gl.glPopMatrix();
	  //end main body
	    gl.glEndList();
		
		//play first animation or second depending on interval
			gl.glCallList( creature_animation2 );

		
		ticker++ ;
		mover++;

		if(mover > 360)
		{
			mover = 0;
		}
	}
}
	