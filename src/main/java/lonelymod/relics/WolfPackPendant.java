package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

//import hlysine.friendlymonsters.utils.MinionUtils;
import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonBonesAction;
import lonelymod.companions.Bones;
//import lonelymod.companions.Bones2;

public class WolfPackPendant extends AbstractEasyRelic {
    public static final String ID = makeID("WolfPackPendant");

    public WolfPackPendant() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new SummonBonesAction());
    }
}
