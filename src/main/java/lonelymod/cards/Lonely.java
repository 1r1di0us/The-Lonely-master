package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;

import lonelymod.actions.AutoplayWaitAction;
import lonelymod.actions.LonelyAction;

public class Lonely extends AbstractEasyCard {
    public static final String ID = makeID("Lonely");
    
    public Lonely() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        AutoplayField.autoplay.set(this, true);
        //no MultiCardPreview because that looks ugly as heck
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction((AbstractGameEffect) new FastingEffect(p.hb.cX, p.hb.cY, Color.YELLOW)));
        addToBot(new AutoplayWaitAction(1.0f));
        addToBot(new LonelyAction(upgraded));
    }
    
    public void upp() {
        upgradeBaseCost(1);
    }
}
