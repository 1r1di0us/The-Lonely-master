package lonelymod.relics;

import lonelymod.LonelyCharacter;
import lonelymod.actions.PlanAction;
import lonelymod.interfaces.AtEndOfTurnPostEndTurnCardsInterface;

import static lonelymod.LonelyMod.makeID;

public class WorldMap extends AbstractEasyRelic implements AtEndOfTurnPostEndTurnCardsInterface {
    public static final String ID = makeID("WorldMap");

    private static final int PLAN_AMT = 4;

    public WorldMap() {
        super(ID, RelicTier.RARE, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + PLAN_AMT + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfTurnPostEndTurnCards(boolean isPlayer) {
        addToBot(new PlanAction(PLAN_AMT));
    }
}
