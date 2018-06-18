package job;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import person.Person;

public class Attendant extends Person {
	
	public static String didSolveMessage = "Resolvemos seu problema, senhor";
	public static String didNotSolveMessage = "Boa sorte";

	public Attendant() {
		super();
	}
	
	protected void setup() {
		System.out.println("Pronto para atender!");
		this.registerToDF();
		
		this.addBehaviour(new PickUpCall());	
	}
	
	private void registerToDF() {
		String serviceName = "unknown";
	  	
	  	// Read the name of the service to register as an argument
	  	Object[] args = getArguments();
	  	if (args != null && args.length > 0) {
	  		serviceName = (String) args[0];
	  	}
	  	
		try {
	  		DFAgentDescription dfd = new DFAgentDescription();
	  		dfd.setName(getAID());
	  		ServiceDescription sd = new ServiceDescription();
	  		sd.setName(serviceName);
	  		sd.setType("attendant");
	  		dfd.addServices(sd);
	  		
	  		DFService.register(this, dfd);
	  	}
	  	catch (FIPAException fe) {
	  		fe.printStackTrace();
	  	}
	}
	
	private class PickUpCall extends CyclicBehaviour {
		Person thisAttendant;
		
		@Override
		public void action() {
			this.thisAttendant = (Attendant) myAgent;
			// receiving the message
			ACLMessage msg = receive();
			
			if (msg != null) {
				System.out.println("RECEBIIII");
				// replying message if not null
				ACLMessage reply =  msg.createReply();
				
				// chance of picking up the call
				boolean didPickUp = thisAttendant.canDo();
				// if picked up
				if (didPickUp) {
					boolean didSolveProblem = thisAttendant.canDo();
					
					// inform if could solve the problem
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(didSolveProblem? didSolveMessage : didNotSolveMessage);
					
					// trying to solve problems is stressful
					thisAttendant.getHumour().decreaseHumourLevel();
				// if not
				} else {
					System.out.println("UEEEEEEEE :(");
					// just refuse the call
					reply.setPerformative(ACLMessage.REFUSE);
					// and answer NOTHING MUAHAHA
					reply.setContent("");
					
					// so good to skip responsabilities!
					thisAttendant.getHumour().increaseHumourLevel();
				}
				
				send(reply);
			} else {/*do nothing*/}
			
			
		}
	}
	
}
