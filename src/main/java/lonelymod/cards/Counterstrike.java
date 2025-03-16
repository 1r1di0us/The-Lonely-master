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
    private static final UIStrings uistring = CardCrawlGame.languagePack.getUIString(makeID("CounterstrikeMessage"));
    public static final String[] TEXT = uistring.TEXT;

    public Counterstrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 0;
        baseMagicNumber = magicNumber = 0;
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.getIntentBaseDmg() >= 0 && !m.isDeadOrEscaped()) {
            if (magicNumber <= 1) {
                magicNumber = 1; //so it attacks at least once.
            }
            AttackEffect effect;
            if (damage <= 9)
                effect = AttackEffect.SLASH_DIAGONAL;
            else if (damage <= 19)
                effect = AttackEffect.SLASH_HEAVY;
            else if (damage <= 29)
                effect = AttackEffect.BLUNT_HEAVY;
            else
                effect = AttackEffect.SMASH;
            for (int i = 0; i < magicNumber; i++) {
                addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), effect));
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