package job;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import person.Person;

public class Regular extends Person {
	
	private Behaviour solveBehaviour = new SolveTask();
	private Behaviour callBehaviour = new CallAtendant();
	private Behaviour currentBehaviour = null;
	
	public Regular() {
		super();
	}
	
	protected void setup() {
		System.out.println("Pronto para resolver tarefas!");
		this.currentBehaviour = this.solveBehaviour;
		
		this.addBehaviour(currentBehaviour);
	}
	
	public void switchTasks() {
		if (this.currentBehaviour == this.solveBehaviour) {
			this.currentBehaviour = this.callBehaviour;
		} else {
			this.currentBehaviour = this.solveBehaviour;
		}
	}
	
	private class SolveTask extends CyclicBehaviour {
		
		private Regular thisRegular = (Regular) myAgent;

		@Override
		public void action() {
			
			// if could solve the task, be happy and go for a new task
			if (this.couldSolve()) {
				thisRegular.getHumour().increaseHumourLevel();
				block(2000); // wait some time before the new task
				
			// if not, be sad and call for help
			} else {
				thisRegular.getHumour().decreaseHumourLevel();
				thisRegular.switchTasks();
			}
		}
		
		private boolean couldSolve() {
			// wait a little while trying to solve the task
			try {
				Thread.sleep(3000);
			} catch(InterruptedException error) {
				System.out.println("DEU RUIM AQUI NO SLEEP");
			}
			
			return thisRegular.canDo();
		}
	}
	
	private class CallAtendant extends CyclicBehaviour {

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setContent("MIM AJUDA!");
			
			
		}
		
		private AID findAttendant() {
			ServiceDescription sc = new ServiceDescription();
			sc.setType("attendant");
			
			DFAgentDescription template = new DFAgentDescription();
			
		}
		
	}
	
	
}
