package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.FireArrowPower;

public class FireArrow extends AbstractEasyCard {
    public final static String ID = makeID("FireArrow");

    public FireArrow() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new FireArrowPower(m, 3, this.damage)));
    }

    public void upp() {
        upgradeDamage(2);
    }
}