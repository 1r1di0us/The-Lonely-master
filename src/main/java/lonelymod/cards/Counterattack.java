package lonelymod.cards;

import static lonelymod.ModFile.makeID;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.ReflectionHacks;

public class Counterattack extends AbstractEasyCard {
    public final static String ID = makeID("Counterattack");
    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(makeID("CounterattackMessage"));
    public static final String[] TEXT = uistring.TEXT;

    //public static final Logger logger = LogManager.getLogger(Counterattack.class.getName());

    public Counterattack() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.getIntentBaseDmg() >= 0 && !m.isDeadOrEscaped()) {
            if (magicNumber <= 1) {
                magicNumber = 1; //so it attacks at least once.
            }
            AttackEffect effect;
            if (damage <= 9)
                effect = AttackEffect.SLASH_DIAGONAL;
            else if (damage > 9 && damage <= 19)
                effect = AttackEffect.SLASH_HEAVY;
            else if (damage > 19 && damage <= 29)
                effect = AttackEffect.BLUNT_HEAVY;
            else if (damage > 29)
                effect = AttackEffect.SMASH;
            else
                effect = AttackEffect.NONE;
            for (int i = 0; i < magicNumber; i++) {
                addToBot(new DamageAction(m, new DamageInfo(m, damage, damageTypeForTurn), effect));
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else if (m == null) {
            return false;
        } else if (m.isDeadOrEscaped()) {
            return false;
        } else if (m.getIntentBaseDmg() < 0) {
            canUse = false;
            this.cantUseMessage = TEXT[0];
        }
        return canUse;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (mo.getIntentBaseDmg() >= 0 && !mo.isDeadOrEscaped()) {
            this.baseDamage = mo.getIntentBaseDmg();
            this.baseMagicNumber = this.magicNumber = (int)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
            super.calculateCardDamage(mo);
            //logger.info("Counterattack: " + this.damage + ", " + this.magicNumber + ".");
            if (this.magicNumber <= 1) { //mo is attacking once
                this.baseMagicNumber = this.magicNumber = 1;
                this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
                this.initializeDescription();
            }
            else if (this.magicNumber >= 2) { //mo is attacking many times
                this.rawDescription = cardStrings.DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[1];
                this.initializeDescription();
            }
        }
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!cardStrings.DESCRIPTION.equals(this.rawDescription)) {
            //this.baseDamage = -1;
            //this.baseMagicNumber = this.magicNumber = 0;
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void upp() {
        upgradeBaseCost(2);
    }
}