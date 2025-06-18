package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.relics.StrikeDummy;

import java.util.Objects;

public class Counterstrike extends AbstractEasyCard {
    public final static String ID = makeID("Counterstrike");

    public Counterstrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 0;
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!m.isDeadOrEscaped()) {
            if (magicNumber <= 1) {
                magicNumber = 1; //so it attacks at least once.
            }
            AttackEffect effect;
            if (damage <= 0)
                effect = AttackEffect.BLUNT_LIGHT;
            else if (damage <= 9)
                effect = AttackEffect.SLASH_DIAGONAL;
            else if (damage <= 19)
                effect = AttackEffect.SLASH_HORIZONTAL;
            else if (damage <= 29)
                effect = AttackEffect.SLASH_HEAVY;
            else
                effect = AttackEffect.BLUNT_HEAVY;
            for (int i = 0; i < magicNumber; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
            }
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (mo.getIntentBaseDmg() >= 0 && !mo.isDeadOrEscaped()) {
            this.baseDamage = mo.getIntentDmg();
            this.baseMagicNumber = this.magicNumber = ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
            super.calculateCardDamage(mo);
            if (this.magicNumber <= 1) { //mo is attacking once
                this.baseMagicNumber = this.magicNumber = 1;
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
                this.initializeDescription();
            }
            else { //mo is attacking many times
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[1];
                this.initializeDescription();
            }
        }
        else if (!mo.isDeadOrEscaped()) {
            this.baseDamage = 0;
            this.baseMagicNumber= this.magicNumber = 1;
            this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[0];
            this.initializeDescription();
        }
    }

    @Override
    public void initializeDescription() {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(StrikeDummy.ID)) {
            if (Objects.equals(this.rawDescription, "")) {
                this.rawDescription = "";
            } else if (Objects.equals(this.rawDescription, cardStrings.DESCRIPTION)) {
                this.rawDescription = cardStrings.EXTENDED_DESCRIPTION[2];
            }
        }
        super.initializeDescription();
    }
    
    @Override
    public void applyPowers() {
        super.applyPowers();

        if (!cardStrings.DESCRIPTION.equals(this.rawDescription)) {
            this.rawDescription = cardStrings.DESCRIPTION;
            initializeDescription();
        }
    }

    public void onMoveToDiscard() {
        this.rawDescription = cardStrings.DESCRIPTION;
        initializeDescription();
    }

    public void upp() {
        upgradeBaseCost(1);
    }
}