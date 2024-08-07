package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class FakeOutAction extends AbstractGameAction {
    private DamageInfo info;
    private int block;

    public FakeOutAction(AbstractCreature target, DamageInfo info, int block) {
        this.info = info;
        this.block = block;
        setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    public void update() {
        if (shouldCancelAction()) {
            this.isDone = true;
            return;
        }
        tickDuration();
        if (this.isDone) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_LIGHT, false));
            this.target.damage(this.info);
            if (this.target.lastDamageTaken > 0) {
                addToTop(new GainBlockAction(this.source, this.block - this.target.lastDamageTaken));
                if (this.target.hb != null)
                    addToTop(new VFXAction(new WallopEffect(this.block, this.source.hb.cX, this.source.hb.cY)));
            }
            else {
                addToTop (new GainBlockAction(this.source, this.block));
                if (this.target.hb != null)
                    addToTop(new VFXAction(new WallopEffect(this.block, this.source.hb.cX, this.source.hb.cY)));
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            } else {
                addToTop(new WaitAction(0.1F));
            }
        }
    }
}
