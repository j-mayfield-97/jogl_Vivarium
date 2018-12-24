import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Food extends Creature
{

	public Food(float tank_size) {
		super(tank_size);
		// TODO Auto-generated constructor stub
	}
	
	public void init(GL2 gl)
	{
	    //set behavior
	    behavior = 2;
	    
	    //get random floats
	    rand_x = r.nextFloat();
	   	rand_y = r.nextFloat();
	   	rand_z = r.nextFloat();
	   	
	   	//constants relating to size of creature should go somewhere else probably
	   	float min_size_shp = 0.2f;
	   	
		//fish scale along X axis
	   	float scalarX = 0.5f;
	   	
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
	    //end main body
	    gl.glEndList();
	    
	    //instantite the hitbox at the coordinates with a radius of the size in the x axis
	    hitbox = new Hitboxes(x_translation, y_translation, z_translation, min_size_shp);//multiply times 2 because sphere was scales by 2 
	    
	    x_velocity = (0);
	    y_velocity = (0);
	    z_velocity = (0);
	    
	    position.set(x_translation, y_translation, x_translation);

	}

}
