package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.AgainstAllOddsAction;

import static lonelymod.LonelyMod.makeID;

public class AgainstAllOdds extends AbstractEasyCard {
    public final static String ID = makeID("AgainstAllOdds");

    public AgainstAllOdds() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = 3;
        baseDamage = 24;
        this.isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new AgainstAllOddsAction(this.magicNumber));
        allDmg(AbstractGameAction.AttackEffect.SMASH);
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        boolean glow = true;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (m != null && !m.isDeadOrEscaped() && m.getIntentBaseDmg() < 0) {
                glow = false;
            }
        }
        if (glow) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void upp() {
        upgradeDamage(8);
    }
}
