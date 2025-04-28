package lonelymod.cards.colorlesscommands;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.cards.AbstractEasyCard;
import lonelymod.powers.CopyCommandPower;

import static lonelymod.LonelyMod.makeID;

public class CommandCopy extends AbstractEasyCard {
    public final static String ID = makeID("CommandCopy");

    public CommandCopy() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF, CardColor.COLORLESS);
        this.tags.add(AbstractEasyCard.Enums.COMPANION);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new CopyCommandPower(p, this.upgraded)));
    }

    public void upp() {
        uDesc();
    }
}
