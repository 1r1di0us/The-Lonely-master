package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LockOnPower;

import lonelymod.actions.CompanionAttackAbilityAction;

public class MarkedArrow extends AbstractEasyCard {
    public final static String ID = makeID("MarkedArrow");

    public MarkedArrow() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseDamage = 4;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new LockOnPower(m, magicNumber)));
        AbstractDungeon.actionManager.addToBottom(new CompanionAttackAbilityAction());
    }

    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
    }
}
