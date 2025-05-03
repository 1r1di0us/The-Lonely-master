package lonelymod.cards;

import static lonelymod.LonelyMod.makeID;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FastingEffect;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import lonelymod.actions.LonelyAction;
import lonelymod.powers.LonelyPower;

import java.util.ArrayList;


public class Lonely extends AbstractEasyCard {
    public static final String ID = makeID("Lonely");
    
    public Lonely() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        cardToPreview.addAll(CardTips);
        this.baseMagicNumber = this.magicNumber = 16;
    }
    
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new VFXAction(new FastingEffect(p.hb.cX, p.hb.cY, Color.YELLOW)));
        addToBot(new LonelyAction(this.magicNumber));
    }
    
    public void upp() {
        upgradeBaseCost(2);
    }

    public static final ArrayList<AbstractCard> CardTips = new ArrayList<AbstractCard>() {
        {
            add(new Bravery());
            add(new Desperation());
            add(new Resolve());
        }
    };
}
