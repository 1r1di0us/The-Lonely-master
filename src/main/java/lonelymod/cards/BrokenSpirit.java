package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.actions.CallMoveAction;
import lonelymod.fields.CompanionField;
import lonelymod.powers.CompanionVigorPower;

public class BrokenSpirit extends AbstractEasyCard {
    public final static String ID = makeID("BrokenSpirit");

    public BrokenSpirit() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new Primal();
        this.baseMagicNumber = this.magicNumber = 3;
        this.baseSecondMagic = this.secondMagic = 5;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(new Primal(), 1));
        if (CompanionField.currCompanion.get(p) != null)
            addToBot(new ApplyPowerAction(CompanionField.currCompanion.get(p), p, new CompanionVigorPower(CompanionField.currCompanion.get(p), this.secondMagic)));
        else
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, CallMoveAction.TEXT[0], true));
        addToBot(new ApplyPowerAction(p, p, new FrailPower(p, this.magicNumber, false), this.magicNumber));
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}
