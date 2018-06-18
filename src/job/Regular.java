package job;

import person.Person;
import task.TaskSolver;
import task.Task;

public class Regular extends Person implements TaskSolver {
	
	public Regular() {
		super();
	}
	
	public boolean solveTask(Task task) {
		return task.isSolved();
	}
}
