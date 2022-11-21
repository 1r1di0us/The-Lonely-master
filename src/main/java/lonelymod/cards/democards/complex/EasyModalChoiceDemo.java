package lonelymod.cards.democards.complex;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import lonelymod.actions.EasyModalChoiceAction;
import lonelymod.cards.AbstractEasyCard;

import static lonelymod.ModFile.makeID;
import static lonelymod.util.Wiz.*;

import java.util.ArrayList;

public class EasyModalChoiceDemo extends AbstractEasyCard {
    public final static String ID = makeID(EasyModalChoiceDemo.class.getSimpleName());
    // intellij stuff skill, self, uncommon, , , , , , 

    public EasyModalChoiceDemo() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 3;
        baseSecondMagic = secondMagic = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> easyCardList = new ArrayList<>();
        easyCardList.add(new EasyModalChoiceCard(cardStrings.EXTENDED_DESCRIPTION[0], cardStrings.EXTENDED_DESCRIPTION[1] + magicNumber + cardStrings.EXTENDED_DESCRIPTION[2], () -> att(new DrawCardAction(magicNumber))));
        easyCardList.add(new EasyModalChoiceCard(cardStrings.EXTENDED_DESCRIPTION[3], cardStrings.EXTENDED_DESCRIPTION[4]+ secondMagic + cardStrings.EXTENDED_DESCRIPTION[5], () -> applyToSelfTop(new StrengthPower(p, secondMagic))));
        atb(new EasyModalChoiceAction(easyCardList));
    }

    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}