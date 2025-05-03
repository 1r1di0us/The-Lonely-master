package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import lonelymod.actions.BrainstormAction;

public class Brainstorm extends AbstractEasyCard {
    public final static String ID = makeID("Brainstorm");

    public Brainstorm() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        this.baseDamage = 13;
        this.isMultiDamage = true;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        float speedTime = 0.2F;
        if (Settings.FAST_MODE)
            speedTime = 0.0F;
        addToTop(new DamageAllEnemiesAction(p, this.baseDamage, DamageInfo.DamageType.NORMAL, AbstractGameAction.AttackEffect.NONE));
        for (AbstractMonster mon : (AbstractDungeon.getMonsters()).monsters) {
            if (!mon.isDeadOrEscaped() && !mon.halfDead)
                addToTop(new VFXAction(new LightningEffect(mon.drawX, mon.drawY), speedTime));
        }
        addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new BrainstormAction(this.magicNumber));
    }

    public void upp() {
        upgradeDamage(4);
    }
}
