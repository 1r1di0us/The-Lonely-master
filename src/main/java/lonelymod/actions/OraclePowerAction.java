package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import lonelymod.powers.CompanionVigorPower;

public class OraclePowerAction extends AbstractGameAction {

    private final AbstractCreature owner;
    private final int CalmAmt;
    private final int WrathAmt;
    private final int DivAmt;

    public OraclePowerAction(AbstractCreature owner, int CalmAmt, int WrathAmt, int DivAmt) {
        this.owner = owner;
        this.CalmAmt = CalmAmt;
        this.WrathAmt = WrathAmt;
        this.DivAmt = DivAmt;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.stance.ID.equals(CalmStance.STANCE_ID)) {
            addToBot(new ApplyPowerAction(owner, owner, new CompanionVigorPower(owner, this.CalmAmt)));
        } else if (AbstractDungeon.player.stance.ID.equals(WrathStance.STANCE_ID)) {
            addToBot(new ApplyPowerAction(owner, owner, new CompanionVigorPower(owner, this.WrathAmt)));
        } else if (AbstractDungeon.player.stance.ID.equals(DivinityStance.STANCE_ID)) {
            addToBot(new ApplyPowerAction(owner, owner, new CompanionVigorPower(owner, this.DivAmt)));
        }
        this.isDone = true;
    }
}
