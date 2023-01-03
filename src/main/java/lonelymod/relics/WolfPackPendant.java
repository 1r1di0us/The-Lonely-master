package lonelymod.relics;

import static lonelymod.LonelyMod.makeID;

import lonelymod.LonelyCharacter;

public class WolfPackPendant extends AbstractEasyRelic {
    public static final String ID = makeID("WolfPackPendant");

    public WolfPackPendant() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }

    @Override
    public void atBattleStart() {
        flash();
    }
}
