package person;

import jade.core.Agent;

public abstract class Person extends Agent {
	private Humour humour = new Humour();
	
	public Person() {
		super();
	}
	
	public Humour getHumour() {
		return this.humour;
	}

	
	public boolean canDo() {
		Double chance = Math.random();
		
		return chance <= this.getHumour().getHumourLevel();
	}
}
