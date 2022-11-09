package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class QuickDraw extends AbstractEasyCard {
    public final static String ID = makeID("QuickDraw");

    public QuickDraw() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 7;
        baseMagicNumber = 2;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (m != null && m.getIntentBaseDmg() >= 0) {
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
          if (!m.isDeadOrEscaped() && m.getIntentBaseDmg() >= 0) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
            break;
          } 
        } 
      }

    public void upp() {
        upgradeDamage(3);
    }
}
