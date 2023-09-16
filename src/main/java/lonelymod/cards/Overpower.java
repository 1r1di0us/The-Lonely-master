package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.OverpowerAction;

public class Overpower extends AbstractEasyCard {
    public final static String ID = makeID("Overpower");

    public Overpower() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseBlock = 8;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new OverpowerAction(this.magicNumber, m));
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() < 0) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
            int multiAmt = ReflectionHacks.getPrivate(m, AbstractMonster.class, "intentMultiAmt");
            if (!m.isDeadOrEscaped()
                    && ((multiAmt <= 1 && AbstractDungeon.player.currentBlock + this.block >= m.getIntentDmg())
                    || (multiAmt > 1 && AbstractDungeon.player.currentBlock + this.block >= m.getIntentDmg() * multiAmt))) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }

    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
    }
}
