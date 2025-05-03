package lonelymod.cards.colorlesscommands;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.actions.CallMoveAction;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.cards.colorlesssummons.CommandDestroy;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.TargetPower;

import static lonelymod.LonelyMod.makeID;

public class CommandTarget extends AbstractEasyCard {
    public final static String ID = makeID("CommandTarget");

    public CommandTarget() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF_AND_ENEMY, CardColor.COLORLESS);
        this.magicNumber = this.baseMagicNumber = 3;
        this.secondMagic = this.baseSecondMagic = 1;
        this.cardsToPreview = new CommandDestroy();
        this.tags.add(AbstractEasyCard.Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new TargetPower(m, this.magicNumber, false), this.magicNumber));
        addToBot(new CallMoveAction(AbstractCompanion.PROTECT, CompanionField.currCompanion.get(p)));
        AbstractCard destroy = new CommandDestroy();
        if (upgraded) destroy.upgrade();
        addToBot(new MakeTempCardInHandAction(destroy, this.secondMagic));
    }

    public void upp() {
        AbstractCard destroy = new CommandDestroy();
        destroy.upgrade();
        this.cardsToPreview = destroy;
        uDesc();
    }
}
