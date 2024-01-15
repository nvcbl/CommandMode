package cmode;

import arc.*;
import arc.math.*;
import arc.util.*;
import arc.input.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.game.EventType.*;

import static mindustry.Vars.*;

public class CommandListener implements ApplicationListener {
    public Seq<Unit> commanded = new Seq<>();
    public @Nullable Unit commander, lastCommander;
    public int commandLimit;
    public boolean commanding;

    @Override
    public void init() {
        Events.on(WorldLoadEvent.class, e -> {
            commanded.clear();
        });
    }

    public void tryCommand(Unit unit) {
        if (unit == commander ||
            commanded.size >= commandLimit ||
            !commanding ||
            !unit.type.playerControllable
        ) return;

        commanded.add(unit);
        unit.controller(new CommandedAI());
        Fx.unitControl.at(unit.x, unit.y, 0f, unit);
    }

    public void clearCommanded() {
        commanded.each(u -> u.controller(u.type.controller.get(u)));
        commanded.clear();
    }

    @Override
    public void update() {
        if (player == null) return;
        commander = player.unit();
        if (commander == null) return;
        if (commander != lastCommander) clearCommanded();

        commandLimit = Mathf.floor(commander.hitSize / 4f);

        if (commander.spawnedByCore) commandLimit += 3;
        if (!commander.type.targetable) commandLimit -= 2;
        if (commander.hitSize < 16f) commandLimit += 2;

        if (Core.input.keyTap(KeyCode.y)) {
            commanding = !commanding;
            Fx.unitControl.at(commander.x, commander.y, 0f, commander);

            if (commanding) Units.nearby(commander.team, commander.x, commander.y, commander.hitSize * 5f, this::tryCommand);
            else {
                clearCommanded();
            }
        }
        lastCommander = commander;
    }
}
