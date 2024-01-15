package cmode;

import arc.*;
import mindustry.mod.*;

public class CommandMain extends Mod {
    public static CommandListener commands;

    public CommandMain() {
        commands = new CommandListener();
        Core.app.addListener(commands);
    }
}
