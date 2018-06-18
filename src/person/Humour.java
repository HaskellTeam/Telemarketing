package person;

public class Humour {
	private double humourLevel = Math.random();
	
	public double getHumourLevel() {
		return this.humourLevel;
	}
	
	public void increaseHumourLevel() {
		Double humourIncrement = this.humourLevel * 0.3;
		this.humourLevel += humourIncrement;
	}
	
	public void decreaseHumourLevel() {
		Double humourIncrement = this.humourLevel * 0.15;
		this.humourLevel -= humourIncrement;
	}
}
