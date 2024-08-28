

import static lonelymod.LonelyMod.makeID;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.companions.AbstractCompanion;
import lonelymod.fields.CompanionField;


public class StrikeBack extends AbstractEasyCard {
    public final static String ID = makeID("StrikeBack");

    public StrikeBack() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = 8;
        baseMagicNumber = magicNumber = 1;
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        boolean bonusE = false;
        for (AbstractPower pow : p.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                bonusE = true;
            }
        }
        if (bonusE) {
            addToBot(new GainEnergyAction(this.magicNumber));
        }
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        boolean bonusE = false;
        for (AbstractPower pow : AbstractDungeon.player.powers) {
            if (pow.type == AbstractPower.PowerType.DEBUFF) {
                bonusE = true;
            }
        }
        if (bonusE) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void upp() {
        upgradeDamage(4);
    }
}
