package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import lonelymod.powers.GainStrengthBuffPower;

import java.util.ArrayList;

import static lonelymod.LonelyMod.makeID;

public class DarkRitualAction extends AbstractGameAction {
    public static final String ID = makeID("DarkRitualAction");
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;

    private AbstractPlayer p = AbstractDungeon.player;
    //private ArrayList<AbstractCard> unplayable = new ArrayList<>();
    private int mult;
    private int bonusAmt;

    public DarkRitualAction(int mult, int bonusAmt) {
        this.mult = mult;
        this.bonusAmt = bonusAmt;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.EXHAUST;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            // all this was for the removed non-unplayable clause
            /*for (AbstractCard c : this.p.hand.group) {
                if (c.costForTurn == -2) {
                    unplayable.add(c);
                }
            }
            if (unplayable.size() - this.p.hand.group.size() == 0) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.group.size() - this.unplayable.size() == 1) {
                for (AbstractCard c : this.p.hand.group) {
                    if (c.costForTurn == -1) { // x cost
                        if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt < 0) {
                            addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt))));
                        } else if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt > 0) {
                            addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult * EnergyPanel.getCurrentEnergy() + this.bonusAmt)));
                        }
                    } else if (c.costForTurn >= 0) { // not x cost
                        if (this.mult*c.costForTurn + this.bonusAmt < 0) {
                            addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*c.costForTurn + this.bonusAmt))));
                        } else if (this.mult*c.costForTurn + this.bonusAmt > 0) {
                            addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult*c.costForTurn + this.bonusAmt)));
                        }
                    } else if (c.costForTurn == -2) { // unplayable, count as 0 cost
                        if (this.bonusAmt < 0) {
                            addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -this.bonusAmt)));
                        } else if (this.bonusAmt > 0) {
                            addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.bonusAmt)));
                        }
                    }
                    this.p.hand.moveToExhaustPile(c);
                    this.isDone = true;
                    return;
                }
            }*/
            //this.p.hand.group.removeAll(unplayable);
            if (this.p.hand.group.size() == 1) {
                if (this.p.hand.getBottomCard().costForTurn == -1) { // x cost
                    if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt))));
                    } else if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult * EnergyPanel.getCurrentEnergy() + this.bonusAmt)));
                    }
                } else if (this.p.hand.getBottomCard().costForTurn >= 0) { // not x cost
                    if (this.mult*this.p.hand.getBottomCard().costForTurn + this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*this.p.hand.getBottomCard().costForTurn + this.bonusAmt))));
                    } else if (this.mult*this.p.hand.getBottomCard().costForTurn + this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult * this.p.hand.getBottomCard().costForTurn + this.bonusAmt)));
                    }
                } else if (this.p.hand.getBottomCard().costForTurn == -2) { // unplayable, treat as 0 cost
                    if (this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -this.bonusAmt)));
                    } else if (this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.bonusAmt)));
                    }
                }
                this.p.hand.moveToExhaustPile(this.p.hand.getBottomCard());
                //returnCards();
                this.isDone = true;
                //why not return here?
            } else if (this.p.hand.group.size() > 1) {
                AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false);
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                if (c.costForTurn == -1) { // x cost
                    if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt))));
                    } else if (this.mult*EnergyPanel.getCurrentEnergy() + this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult * EnergyPanel.getCurrentEnergy() + this.bonusAmt)));
                    }
                } else if (c.costForTurn >= 0) { // not x cost
                    if (this.mult*c.costForTurn + this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -(this.mult*c.costForTurn + this.bonusAmt))));
                    } else if (this.mult*c.costForTurn + this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.mult*c.costForTurn + this.bonusAmt)));
                    }
                } else if (c.costForTurn == -2) { // unplayable, treat as 0 cost
                    if (this.bonusAmt < 0) { //doesn't happen but who knows
                        addToTop(new ApplyPowerAction(p, p, new LoseStrengthPower(p, -this.bonusAmt)));
                    } else if (this.bonusAmt > 0) {
                        addToTop(new ApplyPowerAction(p, p, new GainStrengthBuffPower(p, this.bonusAmt)));
                    }
                }
                this.p.hand.moveToExhaustPile(c);
            }
            //returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }
        tickDuration();
    }

    /*private void returnCards() {
        for (AbstractCard c : this.unplayable) {
            this.p.hand.addToTop(c);
        }
        this.p.hand.refreshHandLayout();
    }*/
}
