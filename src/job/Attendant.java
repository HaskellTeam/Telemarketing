package job;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import person.Person;

public class Attendant extends Person {
	
	public static String didSolveMessage = "Resolvemos seu problema, senhor";
	public static String didNotSolveMessage = "Boa sorte";

	public Attendant() {
		super();
	}
	
	
	private class PickUpCall extends CyclicBehaviour {
		Person thisAttendant = (Attendant) myAgent;
		
		@Override
		public void action() {
			// receiving the message
			ACLMessage msg = receive();
			
			if (msg != null) {
				// replying message if not null
				ACLMessage reply =  msg.createReply();
				
				// chance of picking up the call
				boolean didPickUp = canDo();
				// if picked up
				if (didPickUp) {
					boolean didSolveProblem = canDo();
					
					// inform if could solve the problem
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(didSolveProblem? didSolveMessage : didNotSolveMessage);
					
					// trying to solve problems is stressful
					thisAttendant.getHumour().decreaseHumourLevel();
				// if not
				} else {
					// just refuse the call
					reply.setPerformative(ACLMessage.REFUSE);
					// and answer NOTHING MUAHAHA
					reply.setContent("");
					
					// so good to skip responsabilities!
					thisAttendant.getHumour().increaseHumourLevel();
				}
				
				send(reply);
			} else {
				block();
			}
			
			
		}
		
		private boolean canDo() {
			Double chance = Math.random();
			
			return chance <= thisAttendant.getHumour().getHumourLevel();
		}
		
	}
	
}
