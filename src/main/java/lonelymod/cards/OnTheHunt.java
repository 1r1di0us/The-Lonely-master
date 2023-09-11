package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.fields.ReturnField;

import static lonelymod.LonelyMod.makeID;

public class OnTheHunt extends AbstractEasyCard {
    public final static String ID = makeID("OnTheHunt");

    public OnTheHunt() {
        super(ID, 1, AbstractCard.CardType.ATTACK, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.ENEMY);
        baseDamage = 7;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (this.damage > m.currentBlock && this.damage > 0)
            addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(AbstractDungeon.player)));
        ReturnField.willReturn.set(this, true);
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            if ((m.hasPower(VulnerablePower.POWER_ID) && this.damage * 1.5 > m.currentBlock && this.damage * 1.5 > 0)
                    || (!m.hasPower(VulnerablePower.POWER_ID) && this.damage > m.currentBlock && this.damage > 0)) {
                this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                break;
            }
        }
    }

    public void upp() {
        upgradeDamage(3);
    }
}
