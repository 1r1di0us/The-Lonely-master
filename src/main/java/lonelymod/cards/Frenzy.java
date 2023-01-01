package lonelymod.cards;

import static lonelymod.ModFile.makeID;
import static lonelymod.util.Wiz.*;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.actions.CompanionAttackAbilityAction;
import lonelymod.actions.EasyXCostAction;

public class Frenzy extends AbstractEasyCard {
    public final static String ID = makeID("Frenzy");

    //AbstractOrb oldOrb;
    boolean wasPlayed;

    public Frenzy() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 0;
        baseDamage = 5;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        wasPlayed = true;
        dmg(m, AttackEffect.SLASH_HORIZONTAL);
        //oldOrb = p.orbs.get(0);
        atb(new CompanionAttackAbilityAction()); //why are we using atb all of a sudden?
        atb(new EasyXCostAction(this, (effect, params) -> {
            for (int i = 0; i < effect + params[0]; i++) {
                atb(new TriggerEndOfTurnOrbsAction());
            }
            return true;
        }, magicNumber));
    }
    
    //originally this card would channel the old orb after it was done.
    /*@Override
    public void onMoveToDiscard() {
        if (wasPlayed) {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(oldOrb));
            wasPlayed = false;
        }
    }*/

    public void upp() {
        upgradeDamage(3);
        uDesc();
    }
}