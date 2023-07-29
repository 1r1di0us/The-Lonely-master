package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import lonelymod.actions.CallMoveAction;
import lonelymod.fields.CompanionField;
import lonelymod.powers.CompanionDexterityPower;


public class Teamwork extends AbstractEasyCard {
    public final static String ID = makeID("Teamwork");

    public Teamwork() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, this.magicNumber)));
        if (CompanionField.currCompanion.get(AbstractDungeon.player) != null)
            addToBot(new ApplyPowerAction(CompanionField.currCompanion.get(AbstractDungeon.player), p, new CompanionDexterityPower(CompanionField.currCompanion.get(AbstractDungeon.player), this.secondMagic)));
        else
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, CallMoveAction.TEXT[0], true));
    }
    
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
    }
}
