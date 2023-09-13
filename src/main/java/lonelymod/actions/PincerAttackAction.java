package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;


public class PincerAttackAction extends AbstractGameAction {

    public PincerAttackAction(AbstractCreature target, int damage) {
        this.target = target;
        this.amount = damage;
    }

    @Override
    public void update() {
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null) {
            if (CompanionField.currCompanion.get(AbstractDungeon.player).nextMove == AbstractCompanion.ATTACK) {
                if (target != null) {
                    //addToTop(new VFXAction(new ClashEffect(target.hb.cX, target.hb.cY), 0.1F));
                    //:(
                    addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.amount), AttackEffect.SLASH_DIAGONAL));
                    addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.amount), AttackEffect.SLASH_DIAGONAL));
                }
            } else {
                addToTop(new DamageAction(target, new DamageInfo(AbstractDungeon.player, this.amount), AttackEffect.SLASH_DIAGONAL));
            }
        }
        this.isDone = true;
    }
}
