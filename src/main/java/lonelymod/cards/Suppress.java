package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;
import static lonelymod.util.Wiz.*;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.AttackDamageRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.actions.EasyXCostAction;

public class Suppress extends AbstractEasyCard {
    public final static String ID = makeID("Suppress");

    public Suppress() {
        super(ID, -1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 0;
        baseDamage = 5;
        baseBlock = 4;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            for (int i = 0; i < effect + params[0]; i++) {
                addToBot(new AttackDamageRandomEnemyAction(this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                blck();
            }
            return true;
        }, magicNumber));
    }

    public void upp() {
        upgradeDamage(2);
        upgradeBlock(2);
    }
}