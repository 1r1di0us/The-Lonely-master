package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import lonelymod.LonelyCharacter;
import lonelymod.actions.SummonBonesAction;

public class BonesStomach extends AbstractEasyRelic {
    public static final String ID = makeID("BonesStomach");

    public BonesStomach() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new SummonBonesAction());
    }
}
