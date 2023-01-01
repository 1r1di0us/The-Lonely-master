package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import lonelymod.fields.ReturnField;

public class StandYourGround extends AbstractEasyCard {
    public final static String ID = makeID("StandYourGround");

    public StandYourGround() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = 5;
        baseMagicNumber = magicNumber = 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        if (upgraded) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber)));
        }
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeMagicNumber(3);
        uDesc();
    }
}