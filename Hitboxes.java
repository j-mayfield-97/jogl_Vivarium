
public class Hitboxes 
{
	//coordinates of the center of the hitbox
	//which is NOT a box, but is a SPHERE.
	private float x_coord;
	private float y_coord;
	private float z_coord;
	public float radius;
	
	public Hitboxes(float x, float y, float z, float r)
	{
		//set the initial variables
		x_coord = x;
		y_coord = y;
		z_coord = z;
		radius = r;
	}
	//hitbox needs to update every frame
	public void update_coords(float x, float y, float z)
	{
		x_coord = x;
		y_coord = y;
		z_coord = z;
	}
	//main hitbox collision check
	public boolean collision_check(Hitboxes hb)
	{
		/*//check if the distance of the centers is less tan the sum of the radii 
		if(
				((hb.x_coord < this.x_coord + this.radius) && (hb.x_coord > this.x_coord - this.radius)) &&
				((hb.y_coord < this.y_coord + this.radius) && (hb.y_coord > this.y_coord - this.radius)) &&
				((hb.z_coord < this.z_coord + this.radius) && (hb.z_coord > this.z_coord - this.radius))
		  )
			return true;*/
		boolean check = (float) Math.sqrt(
						Math.pow((this.x_coord - hb.x_coord), 2) +
						Math.pow((this.y_coord - hb.y_coord), 2) +
						Math.pow((this.z_coord - hb.z_coord), 2) ) < this.radius + hb.radius;
		//the pow and sqrt are pretty expensive, maybe try to reduce this later
		return check;
	}

}
