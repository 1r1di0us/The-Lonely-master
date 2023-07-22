package lonelymod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.LonelyCharacter;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.RelicOnSummonInterface;
import lonelymod.powers.CompanionDexterityPower;

import static lonelymod.LonelyMod.makeID;

public class TreatBox extends AbstractEasyRelic implements RelicOnSummonInterface {
    public static final String ID = makeID("TreatBox");

    private static final int POWER_AMT = 1;

    public TreatBox() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + POWER_AMT + DESCRIPTIONS[1] + POWER_AMT + DESCRIPTIONS[2];
    }

    @Override
    public void onSummon(AbstractCompanion c, boolean onBattleStart) {
        flash();
        addToBot(new ApplyPowerAction(c, c, new StrengthPower(c, 1)));
        addToBot(new ApplyPowerAction(c, c, new CompanionDexterityPower(c, 1)));
    }
}
