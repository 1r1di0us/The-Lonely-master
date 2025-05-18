package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.util.UUID;

public class ExponentialForceAction extends AbstractGameAction {

    private final DamageInfo info;
    private final UUID uuid;

    public ExponentialForceAction(AbstractCreature target, DamageInfo info, UUID targetUUID) {
        this.info = info;
        setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.duration = 0.1F;
        this.uuid = targetUUID;
    }

    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            if (info.output < 40) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            } else {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AbstractGameAction.AttackEffect.SMASH));
            }
            this.target.damage(this.info);
            if (!((this.target.isDying || this.target.currentHealth <= 0) && !this.target.halfDead)) {
                for (AbstractCard c : GetAllInBattleInstances.get(this.uuid)) {
                    c.baseDamage += c.baseDamage;
                    if (c.baseDamage < 0)
                        c.baseDamage = 0;
                }
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
        this.isDone = true;
    }
}
