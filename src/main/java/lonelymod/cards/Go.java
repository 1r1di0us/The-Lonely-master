package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.orbs.WolfAttackAction;

public class Go extends AbstractEasyCard {
    public final static String ID = makeID("Go");

    public Go() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.channelOrb((AbstractOrb) new WolfAttackAction());
    }

    public void upp() {
        upgradeBaseCost(0);
    }
}