package lonelymod.relics;

import lonelymod.LonelyCharacter;

import static lonelymod.LonelyMod.makeID;

public class Spyglass extends AbstractEasyRelic {
    public static final String ID = makeID("Spyglass");

    public static final int EXTRA_SCRY = 3;

    public Spyglass() {
        super(ID, RelicTier.SHOP, LandingSound.CLINK, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + EXTRA_SCRY + DESCRIPTIONS[1];
    }

    //code in PlanAction.update()

}
