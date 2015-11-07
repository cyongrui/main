package doordonote.commandfactory;

import doordonote.command.Command;
import doordonote.command.UndoCommand;
import doordonote.common.Util;

//@@author A0131436N

public class UndoHandler extends CommandHandler {

	protected UndoHandler(String commmandBody) throws ExcessArgumentException {
		super(commmandBody);
		if (!Util.isEmptyOrNull(commandBody)) {
			throw new ExcessArgumentException();
		}
	}

	@Override
	public Command generateCommand() {
		return new UndoCommand();
	}

}
