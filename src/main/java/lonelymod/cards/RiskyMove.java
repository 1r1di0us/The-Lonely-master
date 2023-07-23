package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;


public class RiskyMove extends AbstractEasyCard {
    public final static String ID = makeID("RiskyMove");

    public RiskyMove() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 14;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).nextMove == AbstractCompanion.PROTECT) {
            addToBot(new GainEnergyAction(this.magicNumber));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null && CompanionField.currCompanion.get(AbstractDungeon.player).nextMove == AbstractCompanion.PROTECT) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void upp() {
        upgradeDamage(4);
    }
}
