package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

public class CompanionAttackAction extends AbstractGameAction {
    private DamageInfo info;
    
    private int target;

    private AbstractMonster targetMonster;
    private boolean bite;
    
    public CompanionAttackAction(DamageInfo info, AbstractGameAction.AttackEffect effect, boolean bite) {
      this.info = info;
      this.attackEffect = effect;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.attackEffect = AbstractGameAction.AttackEffect.NONE;
      this.target = 0;
      this.targetMonster = null;
      this.bite = bite;
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
                if (bite) { //If this was called by WolfAttack because bite needs its own thing to happen
                    addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect)new BiteEffect(targetMonster.hb.cX, targetMonster.hb.cY - 40.0F * Settings.scale, Settings.LIGHT_YELLOW_COLOR.cpy()), 0.1F));
                    addToBot((AbstractGameAction) new GainBlockAction(AbstractDungeon.player, Math.floorDiv(Math.max(this.info.output - targetMonster.currentBlock, 0), 2)));
                }
                addToTop((AbstractGameAction)new DamageAction((AbstractCreature)targetMonster, this.info, this.attackEffect, true));
            }
        }
        else {
            this.info.output = AbstractOrb.applyLockOn((AbstractCreature)targetMonster, this.info.base);
            if (bite) { // WolfAttack special treatment
                addToTop((AbstractGameAction)new VFXAction((AbstractGameEffect)new BiteEffect(targetMonster.hb.cX, targetMonster.hb.cY - 40.0F * Settings.scale, Settings.LIGHT_YELLOW_COLOR.cpy()), 0.1F));
                addToBot((AbstractGameAction) new GainBlockAction(AbstractDungeon.player, Math.floorDiv(Math.max(this.info.output - targetMonster.currentBlock, 0), 2)));
            }
            addToTop((AbstractGameAction)new DamageAction((AbstractCreature)targetMonster, this.info, this.attackEffect, true));
        }
        // SPECIFICALLY FOR THE WOLF:
        if ((this.targetMonster.isDying || this.targetMonster.currentHealth <= 0) && !this.targetMonster.halfDead && !this.targetMonster.hasPower("Minion")) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 2), 2));
        }
        this.isDone = true;
    }
}