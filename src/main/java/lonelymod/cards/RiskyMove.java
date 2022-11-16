package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import lonelymod.orbs.WolfProtectAction;

public class RiskyMove extends AbstractEasyCard {
    public final static String ID = makeID("RiskyMove");

    public RiskyMove() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 15;
        baseMagicNumber = magicNumber = 2;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        if (AbstractDungeon.player.orbs.get(0) instanceof WolfProtectAction) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
        }
    }

    public void upp() {
        upgradeDamage(5);
    }
}
