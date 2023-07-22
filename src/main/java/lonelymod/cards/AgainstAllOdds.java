package lonelymod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static lonelymod.LonelyMod.makeID;

public class AgainstAllOdds extends AbstractEasyCard {
    public final static String ID = makeID("AgainstAllOdds");

    public AgainstAllOdds() {
        super(ID, 3, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        baseDamage = 24;
        this.isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean gainEnergy = true;
        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!mo.isDeadOrEscaped() && mo.getIntentBaseDmg() > 0) {
                gainEnergy = false;
            }
        }
        if (gainEnergy) {
            addToBot(new GainEnergyAction(3));
        }
        allDmg(AbstractGameAction.AttackEffect.SMASH);
    }

    public void upp() {
        upgradeDamage(8);
    }
}
