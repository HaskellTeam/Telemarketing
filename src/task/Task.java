package task;

public class Task {
	private boolean solved = false;
	
	public boolean isSolved() {
		return this.solved;
	}
	
	public void solve() {
		this.solved = true;
	}
}
