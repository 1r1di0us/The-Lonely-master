package lonelymod.cards.summonmoves;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lonelymod.cards.AbstractEasyCard;

import static lonelymod.LonelyMod.makeID;
import static lonelymod.LonelyMod.modID;

public class Dive extends CustomCard {
    public final static String ID = makeID("Dive");
    public final static String ImageID = makeID("TheRavyn");
    protected final CardStrings cardStrings;

    public Dive() {
        super(ID, "", AbstractEasyCard.getCardTextureString(ImageID.replace(modID + ":", ""), AbstractEasyCard.CardType.SKILL),
                -2, "", AbstractEasyCard.CardType.SKILL, AbstractEasyCard.CardColor.COLORLESS, AbstractEasyCard.CardRarity.SPECIAL, AbstractEasyCard.CardTarget.SELF);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        rawDescription = cardStrings.DESCRIPTION;
        name = originalName = cardStrings.NAME;
        initializeTitle();
        initializeDescription();
    }

    @Override
    public void upgrade() {}

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {}
}

