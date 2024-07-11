package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import lonelymod.actions.FakeOutAction;

import static lonelymod.LonelyMod.makeID;

public class FakeOut extends AbstractEasyCard {
    public final static String ID = makeID("FakeOut");

    public FakeOut() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = 1;
        baseBlock = 12;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!upgraded)
            addToBot(new FakeOutAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), this.block));
        else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            blck();
        }

    }

    public void upp() {
        upgradeBlock(2);
        uDesc();
    }
}
