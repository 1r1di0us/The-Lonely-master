package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public class TriggerPlatedArmorAction extends AbstractGameAction {

    private AbstractCreature owner;
    public TriggerPlatedArmorAction(AbstractCreature owner) {
        this.owner = owner;
    }

    public void update() {
        if (owner.hasPower(PlatedArmorPower.POWER_ID)) {
            addToBot(new GainBlockAction(this.owner, this.owner, AbstractDungeon.player.getPower(PlatedArmorPower.POWER_ID).amount));
        }
        this.isDone = true;
    }
}
