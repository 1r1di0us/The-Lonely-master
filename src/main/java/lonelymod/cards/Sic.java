package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CallMoveAction;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;
import lonelymod.powers.TargetPower;

public class Sic extends AbstractEasyCard {
    public final static String ID = makeID("Sic");

    public Sic() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        this.baseMagicNumber = this.magicNumber = 0;
        this.tags.add(Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            AbstractMonster targetMonster = AbstractDungeon.getRandomMonster();
            addToBot(new ApplyPowerAction(targetMonster, p, new TargetPower(targetMonster, this.magicNumber, false), this.magicNumber));
        }
        addToBot(new CallMoveAction(AbstractCompanion.ATTACK, CompanionField.currCompanion.get(AbstractDungeon.player)));
    }

    public void upp() {
        upgradeMagicNumber(2);
        uDesc();
    }
}