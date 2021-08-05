package model;

public class Bowler {
	private String name;
	private String team;
	private int runs;
	private int prevruns;
	private int balls;
	private int prevballs;
	private int wickets;
	private int prevwickets;
	private int prevbestwickets;
	private int prevbestruns;
	private String BowlID;
	private boolean gotChance;
	private Bowler() {}
	
	public Bowler(String BowlerID,String name){
		this.name = name;
		this.BowlID = BowlerID;
		this.team = BowlerID.substring(0,3);
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setID(String ID) {
		this.BowlID = ID;
	}
	public void wicket() {
		this.wickets++;
	}
	public void runs(int a) {
		this.runs += a;
	}
	public void setprev(int a,int b) {
		this.prevbestwickets = a;
		this.prevbestruns = b;
	}
	public String getName() {
		return this.name;
	}
	public void setBowlerStats(int arr[]) {
    	this.prevruns = arr[0];
		this.prevballs = arr[1];
		this.prevwickets = arr[2];
		this.prevbestruns = arr[3];
		this.prevbestwickets = arr[4];
    }
}
