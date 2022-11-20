package lonelymod.cards;

import static lonelymod.ModFile.makeID;
import static lonelymod.util.Wiz.*;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import lonelymod.actions.EasyXCostAction;
import lonelymod.orbs.WolfAttackAction;

public class Frenzy extends AbstractEasyCard {
    public final static String ID = makeID("Frenzy");

    AbstractOrb oldOrb;
    boolean wasPlayed;

    public Frenzy() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 0;
        baseDamage = 8;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        wasPlayed = true;
        dmg(m, AttackEffect.SLASH_HORIZONTAL);
        oldOrb = p.orbs.get(0);
        //this will technically make FearOfTheBeast happen but when we implement actual companions this should not happen
        atb(new ChannelAction(new WolfAttackAction()));
        atb(new EasyXCostAction(this, (effect, params) -> {
            for (int i = 0; i < effect + params[0]; i++) {
                atb(new TriggerEndOfTurnOrbsAction());
            }
            return true;
        }, magicNumber));
    }
    
    @Override //make sure that the new orb is channeled after the action is completed
    public void onMoveToDiscard() {
        if (wasPlayed) {
            AbstractDungeon.actionManager.addToBottom(new ChannelAction(oldOrb));
            wasPlayed = false;
        }
    }

    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        uDesc();
    }
}