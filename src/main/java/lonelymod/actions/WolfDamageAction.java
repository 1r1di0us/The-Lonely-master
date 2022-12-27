package lonelymod.actions;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
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
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class WolfDamageAction extends AbstractGameAction {
    private DamageInfo info;

    private boolean isTargeted = false;
    private boolean bite;
    
    public WolfDamageAction(DamageInfo info, AbstractGameAction.AttackEffect effect, boolean bite) {
      this.info = info;
      this.attackEffect = effect;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.attackEffect = AbstractGameAction.AttackEffect.NONE;
      this.target = getTarget();
      this.bite = bite;
    }
    
    public void update() {
        if (!isTargeted) {
            if (target != null) {
                if (bite) {
                    if (target.hasPower(makeID("FetchPower"))) {
                        addToBot(new RemoveSpecificPowerAction(target, target, makeID("FetchPower")));
                    }
                    addToTop((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(target.hb.cX, target.hb.cY - 40.0F * Settings.scale, Settings.LIGHT_YELLOW_COLOR.cpy()), 0.1F));
                    addToBot((AbstractGameAction) new GainBlockAction(AbstractDungeon.player,
                            Math.floorDiv(Math.max(Math.min(this.info.output, target.currentHealth) - target.currentBlock, 0), 2)));
                }
                //apply damage directly
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.NONE));
                this.target.damage(this.info);
                if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                    AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        else {
            this.info.output = AbstractOrb.applyLockOn((AbstractCreature)target, this.info.base);
            if (bite) {
                if (target.hasPower(makeID("Fetch"))) {
                    addToBot(new RemoveSpecificPowerAction(target, target, makeID("FetchPower")));
                }
                addToTop((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(target.hb.cX, target.hb.cY - 40.0F * Settings.scale, Settings.LIGHT_YELLOW_COLOR.cpy()), 0.1F));
                addToBot((AbstractGameAction) new GainBlockAction(AbstractDungeon.player,
                            Math.floorDiv(Math.max(Math.min(this.info.output, target.currentHealth) - target.currentBlock, 0), 2)));
            }
            //apply damage directly
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.NONE));
            this.target.damage(this.info);
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
        //gain focus if the wolf killed it
        if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 2), 2));
        }
        this.isDone = true;
    }

    private AbstractMonster getTarget() {
        int target = 0;
        AbstractMonster targetMonster = null;
        for (AbstractMonster m: (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m.hasPower(LockOnPower.POWER_ID)) {
                if (target < m.getPower(LockOnPower.POWER_ID).amount) {
                    target = m.getPower(LockOnPower.POWER_ID).amount;
                    targetMonster = m;
                }
            }
        }
        if (target == 0)
            targetMonster = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        else {
            this.isTargeted = true;
        }
        return targetMonster;
    }
}