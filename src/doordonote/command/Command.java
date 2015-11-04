//@@author A0131436N

package doordonote.command;

import java.io.IOException;

import doordonote.logic.CommandToController;

/**
 * @author yunpeng 
 */
public interface Command {
	/**
	 * @param 	controller
	 * 			methods in the controller will be called
	 * @return 	feedback that gives information about the outcome of the execution of the command
	 * @throws IOException 
	 * @throws Exception 
	 */
	String execute(CommandToController controller) throws IOException, Exception;
}
