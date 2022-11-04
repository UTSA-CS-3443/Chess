package application.model;

import application.model.Pieces.Chessmen;

public class Tile {
		 
	    private int x;
	    private int y;
	 
	  
	    public Tile(int x, int y, Chessmen piece) {
	        this.setX(x);
	        this.setY(y);
		}

		
	  








		public int getX()
	    {
	        return this.x;
	    }
	  
	    public void setX(int x)
	    {
	        this.x = x;
	    }
	  
	    public int getY()
	    {
	        return this.y;
	    }
	  
	    public void setY(int y)
	    {
	        this.y = y;
	    }
	}





