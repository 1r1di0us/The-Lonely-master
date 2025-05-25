package lonelymod.relics;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import lonelymod.LonelyCharacter;
import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.RelicOnSummonInterface;

import static lonelymod.LonelyMod.makeID;

public class ShortLeash extends AbstractEasyRelic implements RelicOnSummonInterface {
    public static final String ID = makeID("ShortLeash");

    public ShortLeash() {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public void onSummon(AbstractCompanion c, boolean onBattleStart) {
        if (onBattleStart) {
            flash();
            addToBot(new CallMoveAction(AbstractCompanion.PROTECT, c));
        }
    }
}
