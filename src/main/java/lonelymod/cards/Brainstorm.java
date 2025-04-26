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
        baseDamage = 13;
        this.isMultiDamage = true;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        DamageInfo info = new DamageInfo(p, this.baseDamage);
        float speedTime = 0.2F / AbstractDungeon.player.orbs.size();
        if (Settings.FAST_MODE)
            speedTime = 0.0F;
        addToTop(new DamageAllEnemiesAction(p,
                DamageInfo.createDamageMatrix(info.base, true, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE));
        for (AbstractMonster m3 : (AbstractDungeon.getMonsters()).monsters) {
            if (!m3.isDeadOrEscaped() && !m3.halfDead)
                addToTop(new VFXAction(new LightningEffect(m3.drawX, m3.drawY), speedTime));
        }
        addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        addToBot(new BrainstormAction(this.magicNumber));
    }

    public void upp() {
        upgradeDamage(4);
    }
}
