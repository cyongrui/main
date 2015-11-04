package doordonote.logic;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import doordonote.common.Task;

public interface CommandToController {
	String add(String taskDescription, Date startDate, Date endDate) throws IOException;

	String delete(int taskId) throws Exception;

	String find(List<String> keywords) throws IOException;
	
	String finish(int taskId) throws IOException, Exception;
	
	String help();
	
	String help(String commandType);
	
	String redo() throws IOException;
	
	String undo() throws IOException;
	
	String update(int taskId, String taskDescription, Date startDate, Date endDate) throws Exception;
	
	String home() throws IOException;

	String restore(int taskId) throws IOException, Exception;

	String displayFinished() throws IOException;

	String displayDeleted() throws IOException;

	String getTaskID(int taskID) throws Exception;

	UIState getState();

	List<Task> getUserTaskList() throws IOException;
}
