package lonelymod.cards.colorlesscommands;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeID;

public class CommandTarget extends AbstractEasyCard {
    public final static String ID = makeID("CommandTarget");

    public CommandTarget() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY, CardColor.COLORLESS);
        this.magicNumber = this.baseMagicNumber = 3;
        this.tags.add(AbstractEasyCard.Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new TargetPower(m, this.magicNumber, false), this.magicNumber));
        if (m.currentHealth < m.maxHealth / 2) {
            addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(p)));
        } else {
            addToBot(new CallMoveAction(AbstractCompanion.PROTECT, CompanionField.currCompanion.get(p)));
        }
    }

    public void upp() {
        upgradeMagicNumber(2);
    }
}
