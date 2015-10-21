package doordonote.command;

import java.util.Date;

import doordonote.logic.CommandToController;

public class UpdateCommand implements Command {
	protected int taskID = -1;
	protected String taskDescription = null;
	protected Date startDate = null;
	protected Date endDate = null;
	
	public UpdateCommand(int taskID, String taskDescription, Date startDate, Date endDate) {
		
		// UpdateHandler should have checked that taskID > 0
		assert(taskID > 0); 				
		// UpdateHandler should have checked that taskDescription is not null and not empty
		assert(taskDescription != null && !taskDescription.isEmpty()); 	
		
		this.taskDescription = taskDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.taskID = taskID;
	}

	@Override
	public String execute(CommandToController controller) {
		return controller.update(taskID, taskDescription, startDate, endDate);
	}
	



}
