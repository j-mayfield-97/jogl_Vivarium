import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Shark extends Creature
{
	float tail_size;
	float bod_size;
	float scale;
	int mover;

	public Shark(float tank_size) {
		super(tank_size);
		// TODO Auto-generated constructor stub
	}
	
	public void init(GL2 gl)
	{
		//set behvaior
	    behavior = 1;
	    
	    //pull some random floats
	    rand_x = r.nextFloat();
	   	rand_y = r.nextFloat();
	   	rand_z = r.nextFloat();
	   	
	   	//constants  = which should go somewhere else
	   	float min_size_ellipse = 0.1f + (rand_x / 2);
	   	float min_size_cone = 0.3f + rand_x;
	   	float max_size_ellipse = 0.4f;
	   	float max_size_cone = 0.6f;
	   	
	   	//make sure the fish isnt too large or too small
	   	if(min_size_ellipse > max_size_ellipse)
	   		min_size_ellipse = max_size_ellipse;
	   	
	   	if(min_size_cone > max_size_cone)
	   		min_size_cone = max_size_cone;
	   	
	   	//sharks scale along X axis
	   	float scalarX = 3.0f;
	   	
	  //init creature object
	    creature_animation = gl.glGenLists(1);
	    gl.glNewList( creature_animation, GL2.GL_COMPILE );
	    
	    // Instance of utilities 
	    GLUT glut = new GLUT();
	    
	    //body formation
	    gl.glPushMatrix();
	    //make creature longer along the x-axis
	    gl.glScalef(scalarX, scalarX/2, 1.0f);
	    glut.glutSolidSphere(min_size_ellipse, 30, 30);
	    gl.glPopMatrix();
	    
	    //head
	    gl.glPushMatrix();
	    gl.glRotatef(90, 0.0f, 1.0f, 0.0f );
	    gl.glTranslatef(0.f, 0, (float) (-(min_size_ellipse * 2) + (min_size_ellipse * scalarX)));
	    gl.glScalef(scalarX / 2, 1.0f, 1.0f);
	    glut.glutSolidCylinder(min_size_ellipse, min_size_ellipse, 30, 30);
	    gl.glPopMatrix();
	    
	    gl.glEndList();

	    //Instantiate the hitbox at the coordinates with a radius of the size in the x axis
	    hitbox = new Hitboxes(x_translation, y_translation, z_translation, scalarX * min_size_ellipse);//multiply times 2 because sphere was scales by 2 

	    x_velocity = (rand_x / 25);
	    y_velocity = (rand_y / 20);
	    z_velocity = (rand_z / 25);
	    
	  //store initial velocities
	    init_x_velocity = x_velocity;
	    init_y_velocity = y_velocity;
	    init_z_velocity = z_velocity;
	    
	    position.set(x_velocity, y_velocity, z_velocity);
	    
	  //get the size of the cone and of the sphere for later use
	    bod_size = min_size_ellipse;
	    tail_size = min_size_cone;
	    scale = scalarX;
	    mover = 0;
	}
	protected void animation_ticker(GL2 gl)
	{
		GLUT glut = new GLUT();
		
		
		//init creature tail animation
	    creature_animation2 = gl.glGenLists(2);
	    gl.glNewList( creature_animation2, GL2.GL_COMPILE );
	    //tail
	    gl.glPushMatrix();
	    gl.glRotatef(90, 0.0f, 1.0f, 0.0f );
	    gl.glTranslatef(0.f, 0, (float) (-(bod_size) - (bod_size * scale)));
	    gl.glRotatef(mover, 0.0f, 0.0f, 1.0f );
	    gl.glScalef(1.0f, scale, 1.0f);
	    glut.glutSolidCone( 0.2 , tail_size, 30, 30 ); 
	    gl.glPopMatrix();
	    gl.glEndList();

		System.out.println(x_velocity);
		
		//display the animation
		gl.glCallList( creature_animation2 );
		
		ticker++;
		mover++;
		
		//reset after it circles
		if(mover > 360)
		{
			mover = 0;
		}
	}
}

