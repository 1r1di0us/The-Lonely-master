package lonelymod.relics;

import static lonelymod.ModFile.makeID;

import lonelymod.LonelyCharacter;

public class TodoItem extends AbstractEasyRelic {
    public static final String ID = makeID("TodoItem");

    public TodoItem() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, LonelyCharacter.Enums.TODO_COLOR);
    }
}
