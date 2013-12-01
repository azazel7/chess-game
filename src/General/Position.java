package General;
/**
 * 
 * @author Akkarin
 *
 */
public class Position
{
	/**
	 * correspond a l'abscisse
	 */
	protected int x;
	/**
	 * correspond a l'ordonnae
	 */
	protected int y;
	/**
	 * constructeur pour initialiser x et y
	 * @param xx
	 * @param yy
	 */
	public Position(int xx, int yy)
	{
		x = xx;
		y = yy;
	}
	/**
	 * constructeur de base
	 */
	public Position()
	{
		x = y = 0;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean equals(Position obj)
	{
		if(obj == null)
		{
			return false;
		}
		if(obj.getX() == x && obj.getY() == y)
		{
			return true;
		}
		return false;
	}
	public Position clonage()
	{
		return new Position(x,y);
	}
	public String toString()
	{
		return new String("(" + x + "," + y + ")");
	}
}