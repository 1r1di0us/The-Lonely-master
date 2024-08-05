package lonelymod.relics;

import lonelymod.LonelyCharacter;

import static lonelymod.LonelyMod.makeID;

public class PaperDaug extends AbstractEasyRelic {
    public static final String ID = makeID("PaperDaug");

    public PaperDaug() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
    }

    //code in AbstractCompanion.calculateDamage

}
