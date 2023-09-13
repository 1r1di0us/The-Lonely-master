package lonelymod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class LongerWaitAction extends AbstractGameAction {
  public LongerWaitAction(float setDur) {
    setValues(null, null, 0);
    if (Settings.FAST_MODE && setDur > 0.25F) {
      this.duration = 0.25F;
    } else {
      this.duration = setDur;
    } 
    this.actionType = AbstractGameAction.ActionType.WAIT;
  }
  
  public void update() {
    tickDuration();
  }
}