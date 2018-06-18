package job;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CompositeBehaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Collection;
import person.Person;

public class Regular extends Person {
	
	private Behaviour solveBehaviour = new SolveTask();
	private SequentialBehaviour askHelp;
	private Behaviour currentBehaviour = null;
	
	protected void setup() {
		System.out.println("Pronto para resolver tarefas!");
		this.currentBehaviour = this.solveBehaviour;
		this.addBehaviour(this.solveBehaviour);
		
		this.askHelp = new SequentialBehaviour();
		this.askHelp.addSubBehaviour(new CallAtendant());
		this.askHelp.addSubBehaviour(new WaitAnswer());
	}
	
	public void switchTasks() {
		if (this.currentBehaviour == this.solveBehaviour) {
			this.removeBehaviour(this.currentBehaviour);
			this.currentBehaviour = this.askHelp;
		} else {
			this.removeBehaviour(this.currentBehaviour);
			this.currentBehaviour = this.solveBehaviour;
		}
		
		this.addBehaviour(this.currentBehaviour);
	}
	
	private class SolveTask extends CyclicBehaviour {
		
		private Regular thisRegular;

		@Override
		public void action() {
			this.thisRegular = (Regular) myAgent;
			
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
	
	private class CallAtendant extends OneShotBehaviour {

		@Override
		public void action() {
			// create message of help
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.setContent("MIM AJUDA!");
			
			// try to find a attendant 
			try {
				AID attendant = findAttendant();
				
				if (attendant != null) {
					msg.addReceiver(attendant);					
				} else {
					System.out.println("NAO ACHEI NENHUM ATENDENTE POXA :(");
				}
			} catch (FIPAException e) {
				System.out.println("DEU MUITO RUIM TENTANDO ACHAR O ATTENDANT!!!!!!");
			}
			
			send(msg);
		}
		
		
		private AID findAttendant() throws FIPAException {
			ServiceDescription sd = new ServiceDescription();
			sd.setType("attendant");
			
			DFAgentDescription template = new DFAgentDescription();
			template.addServices(sd);
			
			SearchConstraints sc = new SearchConstraints();
	  		sc.setMaxResults((long) 1);
	  		
	  		DFAgentDescription[] results = DFService.search(myAgent, template, sc);

	  		
	  		if (results != null && results.length >= 1) {
	  			return results[0].getName();
	  		} else {
	  			return null;
	  		}
			
		}
		
	}
	
	private class WaitAnswer extends OneShotBehaviour {
		private Regular thisRegular = (Regular) myAgent;

		@Override
		public void action() {
			ACLMessage msg = receive();

			if (msg != null) {
				
				// if message is refused (attendant did not pick up) try again
				if (msg.getPerformative() == ACLMessage.REFUSE) {
					thisRegular.getHumour().decreaseHumourLevel();
					this.reset();
				} else {
					// if picked up and solved problem, be happy and go for new task
					if (msg.getContent() == Attendant.didSolveMessage) {
						thisRegular.getHumour().increaseHumourLevel();
						thisRegular.switchTasks();
						
					// if not, be sad and try again
					} else {
						thisRegular.getHumour().decreaseHumourLevel();
						this.reset();
					}
				}
			} else {
				this.reset();
			}
		}
	}
	
}
