package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class WolfDamageAction extends AbstractGameAction {
    private DamageInfo info;

    private boolean bite;
    
    public WolfDamageAction(DamageInfo info, AbstractMonster targetMonster, boolean bite) {
      this.info = info;
      this.actionType = AbstractGameAction.ActionType.DAMAGE;
      this.attackEffect = AbstractGameAction.AttackEffect.NONE;
      this.target = (AbstractCreature) targetMonster;
      this.bite = bite;
    }
    
    public void update() {
        if (bite) {
            addToTop((AbstractGameAction) new VFXAction((AbstractGameEffect) new BiteEffect(target.hb.cX, target.hb.cY - 40.0F * Settings.scale, Settings.LIGHT_YELLOW_COLOR.cpy()), 0.1F));
            addToBot((AbstractGameAction) new GainBlockAction(AbstractDungeon.player,
                        Math.floorDiv(Math.max(Math.min(this.info.output, target.currentHealth) - target.currentBlock, 0), 2)));
        }
        else {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        //apply damage directly
        this.target.damage(this.info);
        if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
            AbstractDungeon.actionManager.clearPostCombatActions();
        //gain focus if the wolf killed it
        if ((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead && !this.target.hasPower("Minion")) {
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 2), 2));
        }
        this.isDone = true;
    }
}