package cmode;

import mindustry.entities.units.*;

import static cmode.CommandMain.*;

public class CommandedAI extends AIController {
    @Override
    public void updateMovement() {
        int index = commands.commanded.indexOf(unit);
        float offset = (360f / commands.commanded.size) * index;

        var cmdr = commands.commander;

        unit.movePref(vec.trns(offset, unit.hitSize + cmdr.hitSize * 1.5f).add(cmdr).sub(unit).limit(unit.speed()));

        if (cmdr.activelyBuilding() && unit.canBuild()) {
            unit.clearBuilding();
            unit.plans.addFirst(cmdr.buildPlan());
        } else if (cmdr.canShoot() && unit.canShoot()) {
            unit.aimLook(cmdr.aimX, cmdr.aimY);
            unit.controlWeapons(cmdr.isShooting);
        }
    }
}
