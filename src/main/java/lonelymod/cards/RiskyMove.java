package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.orbs.ByrdProtectAbility;
import lonelymod.orbs.WolfProtectAbility;

public class RiskyMove extends AbstractEasyCard {
    public final static String ID = makeID("RiskyMove");

    public RiskyMove() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = 12;
        baseMagicNumber = magicNumber = 1;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        AbstractOrb currOrb = AbstractDungeon.player.orbs.get(0);
        if (currOrb instanceof WolfProtectAbility || currOrb instanceof ByrdProtectAbility) { // || currOrb instanceof BearProtectAbility || currOrb instanceof SquirrelProtectAbility
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(this.magicNumber));
        }
    }

    public void upp() {
        upgradeBaseCost(1);
    }
}
