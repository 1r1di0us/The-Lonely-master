package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;

import lonelymod.orbs.WolfAttackAction;

public class StrikeTogetherAction extends AbstractGameAction {
    int additionalAmt;
  
    public StrikeTogetherAction(AbstractCreature target, int damage, int additional) {
        this.target = target;
        this.amount = damage;
        this.additionalAmt = additional;
    }

    @Override
    public void update() {
        if (AbstractDungeon.player.orbs.get(0) instanceof WolfAttackAction) {
            if (target != null) {
                addToTop(new VFXAction(new ClashEffect(target.hb.cX, target.hb.cY), 0.1F));
            }
            addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.amount + this.additionalAmt)));
        }
        else {
            addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.amount), AttackEffect.SLASH_DIAGONAL));
        }
        this.isDone = true;
    }
    
}
