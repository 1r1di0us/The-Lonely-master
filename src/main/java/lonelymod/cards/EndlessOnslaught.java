package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.GashAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.fields.ReturnField;

public class EndlessOnslaught extends AbstractEasyCard {
    public final static String ID = makeID("EndlessOnslaught");

    public EndlessOnslaught() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        AbstractDungeon.actionManager.addToBottom(new GashAction(this, magicNumber));
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
