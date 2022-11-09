package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.powers.FireArrowsPower;

public class FireArrows extends AbstractEasyCard {
    public final static String ID = makeID("FireArrows");

    public FireArrows() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new FireArrowsPower(m, 3, this.damage)));
    }

    public void upp() {
        upgradeDamage(2);
    }
}