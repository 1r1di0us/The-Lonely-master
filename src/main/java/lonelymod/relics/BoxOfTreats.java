package lonelymod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.LonelyCharacter;
import lonelymod.companions.AbstractCompanion;
import lonelymod.interfaces.RelicOnSummonInterface;
import lonelymod.powers.CompanionDexterityPower;

import static lonelymod.LonelyMod.makeID;

public class BoxOfTreats extends AbstractEasyRelic implements RelicOnSummonInterface {
    public static final String ID = makeID("BoxOfTreats");

    private static final int STR_AMT = 2;
    private static final int DEX_AMT = 1;

    public BoxOfTreats() {
        super(ID, RelicTier.COMMON, LandingSound.CLINK, LonelyCharacter.Enums.YELLOW);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STR_AMT + DESCRIPTIONS[1] + DEX_AMT + DESCRIPTIONS[2];
    }

    @Override
    public void onSummon(AbstractCompanion c, boolean onBattleStart) {
        flash();
        addToBot(new ApplyPowerAction(c, c, new StrengthPower(c, STR_AMT)));
        addToBot(new ApplyPowerAction(c, c, new CompanionDexterityPower(c, DEX_AMT)));
    }
}
