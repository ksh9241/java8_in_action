package java_8_in_action.default_method;

public class Test {
	public static void main(String[] args) {
		Monster m = new Monster();
		m.rotateBy(180);
		m.moveVertically(10);
	}
}

class Monster implements Rotatable, Moveable, Resizable {

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWitdh(int width) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAbsoluteSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setX(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRotationAngle(int angleInDegrees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getRotationAngle() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

interface Rotatable {
	void setRotationAngle (int angleInDegrees);
	int getRotationAngle();
	
	default void rotateBy (int angleInDerees) {
		setRotationAngle((getRotationAngle() + angleInDerees) % 360);
	}
}

interface Moveable {
	int getX ();
	int getY ();
	void setX (int x);
	void setY (int y);
	
	default void moveHorizontally (int distance) {
		setX (getX() + distance);
	}
	
	default void moveVertically (int distance) {
		setY (getY() + distance);
	}
}

interface Resizable {
	int getWidth ();
	int getHeight ();
	void setWitdh (int width);
	void setHeight (int height);
	void setAbsoluteSize (int width, int height);
	
	default void setRelativeSize (int wFactor, int hFactor) {
		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
	}
}
