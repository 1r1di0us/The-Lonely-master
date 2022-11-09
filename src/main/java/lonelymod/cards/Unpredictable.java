package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Unpredictable extends AbstractEasyCard {
    public final static String ID = makeID("Unpredictable");

    public Unpredictable() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = 6;
        baseMagicNumber = 3;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (m != null && m.getIntentBaseDmg() < 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
          if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() < 0) {
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