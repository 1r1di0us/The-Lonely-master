package lonelymod.relics;

import lonelymod.LonelyCharacter;

import static lonelymod.LonelyMod.makeID;

public class PaperDaug extends AbstractEasyRelic {
    public static final String ID = makeID("PaperDaug");

    private final static int EXTRA_PERCENT = 100;
    public final static float MULT = 2.f;

    public PaperDaug() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
    }

    //code in AbstractCompanion.calculateDamage

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + EXTRA_PERCENT + DESCRIPTIONS[1];
    }

}
