package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.LockOnPower;

public class CompanionAttackAction extends AbstractGameAction {
    private DamageInfo info;
    
    private int target;

    private AbstractMonster targetMonster;
    
    public CompanionAttackAction(DamageInfo info, AbstractGameAction.AttackEffect effect) {
      this.info = info;
      this.attackEffect = effect;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.attackEffect = AbstractGameAction.AttackEffect.NONE;
      this.target = 0;
      this.targetMonster = null;
    }
    
    public void update() {
        for (AbstractMonster m: (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.hasPower(LockOnPower.POWER_ID)) {
                if (target < m.getPower(LockOnPower.POWER_ID).amount) {
                    target = m.getPower(LockOnPower.POWER_ID).amount;
                    targetMonster = m;
                }
            }
        }
        if (target == 0) {
            targetMonster = AbstractDungeon.getRandomMonster();
            if (targetMonster != null) {
                addToTop((AbstractGameAction)new DamageAction((AbstractCreature)targetMonster, this.info, this.attackEffect, true));
            }
        }
        else {
            this.info.output = AbstractOrb.applyLockOn((AbstractCreature)targetMonster, this.info.base); 
            addToTop((AbstractGameAction)new DamageAction((AbstractCreature)targetMonster, this.info, this.attackEffect, true));
        }
        this.isDone = true;
    }
}