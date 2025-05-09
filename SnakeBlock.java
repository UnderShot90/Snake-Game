package SnakeGame;

public class SnakeBlock {
	private int xaxis;
	private int yaxis;
	
	public static final int block_height = 20;
	public static final int block_width = 20;
	
	public SnakeBlock( int xaxis, int yaxis) {// get pos of snake block
		this.xaxis = xaxis;
		this.yaxis = yaxis;
	}
	public boolean collision(SnakeBlock r2) {
		// this returns if x and y are the same as r2
		
		return this.xaxis == r2.getXaxis() && this.yaxis == r2.getYaxis();
	}
	
	public void setXaxis(int increment) {
		this.xaxis= this.xaxis+increment; 
	}
	public void setYaxis(int increment) {
		this.yaxis= this.yaxis+increment; 
	}
	public int getXaxis() {
		return this.xaxis;
	}
	public int getYaxis() {
		return this.yaxis;
	}

    public void setPosx(int increment) { this.xaxis = this.xaxis + increment; }

    public void setPosy(int increment) { this.yaxis =  this.yaxis + increment; }
}