package lonelymod.cards;

import static lonelymod.ModFile.makeID;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.orbs.WolfSpecialAction;

public class PrimalInstinct extends AbstractEasyCard {
    public final static String ID = makeID("PrimalInstinct");

    public PrimalInstinct() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.player.channelOrb((AbstractOrb) new WolfSpecialAction());
    }

    public void upp() {
        if (!this.upgraded) {
            this.exhaust = false;
            upgradeName();
            this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}