package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.CompanionAttackAbilityAction;
import lonelymod.fields.ReturnField;

public class Kill extends AbstractEasyCard {
    public final static String ID = makeID("Kill");

    public Kill() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
        ReturnField.willReturn.set(this, true);
    }

    public void upp() {
        upgradeDamage(3);
    }
}